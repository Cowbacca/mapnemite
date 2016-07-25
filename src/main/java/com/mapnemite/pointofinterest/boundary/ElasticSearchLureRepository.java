package com.mapnemite.pointofinterest.boundary;

import com.mapnemite.common.location.domain.Circle;
import com.mapnemite.common.location.domain.Rectangle;
import com.mapnemite.pointofinterest.domain.Lure;
import com.mapnemite.pointofinterest.domain.LureRepository;
import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.PutMapping;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Repository
public class ElasticSearchLureRepository implements LureRepository {

    public static final String INDEX_NAME = "lures";
    private static final String INDEX_TYPE = "lure";
    private final JestClient jestClient;

    @Inject
    public ElasticSearchLureRepository(JestClient jestClient) {
        this.jestClient = jestClient;
        boolean indexExists = execute(new IndicesExists.Builder(INDEX_NAME).build()).isSucceeded();
        if(!indexExists) {
            createIndex();
        }
    }

    private void createIndex() {
            CreateIndex createIndexAction = new CreateIndex.Builder(INDEX_NAME).build();
            execute(createIndexAction);

            PutMapping putMappingAction = new PutMapping.Builder(
                    INDEX_NAME,
                    INDEX_TYPE,
                    "{ \"properties\" : { \"location\" : {\"type\" : \"geo_point\"} } }"
            ).build();
            execute(putMappingAction);
    }


    @Override
    public void save(Lure lure) {
        Index indexAction = new Index.Builder(new IndexedLure(lure)).index(INDEX_NAME).type(INDEX_TYPE).build();
            execute(indexAction);
    }

    @Override
    public Set<Lure> findByLocationWithinAndNotExpired(Circle circle) {
        GeoDistanceQueryBuilder geoFilter = QueryBuilders.geoDistanceQuery("location")
                .point(circle.getX(), circle.getY())
                .distance(circle.getRadius(), DistanceUnit.KILOMETERS);

        return findByNotExpiredAndFilterBy(geoFilter);
    }

    @Override
    public Set<Lure> findByLocationWithinAndNotExpired(Rectangle rectangle) {
        GeoBoundingBoxQueryBuilder geoFilter = QueryBuilders.geoBoundingBoxQuery("location")
                .topRight(rectangle.getTopRight().getLatitude(), rectangle.getTopRight().getLongitude())
                .bottomLeft(rectangle.getBottomLeft().getLatitude(), rectangle.getBottomLeft().getLongitude());

        return findByNotExpiredAndFilterBy(geoFilter);
    }

    private Set<Lure> findByNotExpiredAndFilterBy(QueryBuilder filter) {
        Search search = new Search.Builder(queryByNotExpiredAndFilterBy(filter))
                .addIndex(INDEX_NAME)
                .build();
        SearchResult results = execute(search);

        return results.getHits(IndexedLure.class).stream()
                .map(hit -> hit.source)
                .map(IndexedLure::toPointOfInterest)
                .collect(toSet());
    }

    private String queryByNotExpiredAndFilterBy(QueryBuilder geoFilter) {
        return new SearchSourceBuilder().query(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.rangeQuery("expiresAt").gt(System.currentTimeMillis()))
                        .filter(geoFilter)
        ).toString();
    }

    private <T extends JestResult> T execute(Action<T> action) {
        try {
            T result = jestClient.execute(action);
            if (!result.isSucceeded()) {
               log.warn(result.getErrorMessage());
            }
            return result;
        } catch (IOException e) {
            throw new ElasticSearchException(e.getMessage(), e);
        }
    }
}
