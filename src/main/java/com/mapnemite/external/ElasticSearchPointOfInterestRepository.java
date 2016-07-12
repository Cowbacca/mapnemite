package com.mapnemite.external;

import com.mapnemite.boundary.PointOfInterestRepository;
import com.mapnemite.domain.PointOfInterest;
import com.mapnemite.domain.location.Circle;
import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.PutMapping;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.core.StringFieldMapper;
import org.elasticsearch.index.mapper.object.RootObjectMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Repository
public class ElasticSearchPointOfInterestRepository implements PointOfInterestRepository{

    public static final String INDEX_NAME = "points_of_interest";
    private static final String INDEX_TYPE = "point_of_interest";
    private final JestClient jestClient;

    @Inject
    public ElasticSearchPointOfInterestRepository(JestClient jestClient) {
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
    public void save(PointOfInterest pointOfInterest) {
            Index indexAction = new Index.Builder(new IndexedPointOfInterest(pointOfInterest)).index(INDEX_NAME).type(INDEX_TYPE).build();
            execute(indexAction);
    }

    @Override
    public Set<PointOfInterest> findByLocationWithin(Circle circle) {
        Search search = new Search.Builder(byLocationWithinQuery(circle))
                .addIndex(INDEX_NAME)
                .build();
        SearchResult results = execute(search);

        return results.getHits(IndexedPointOfInterest.class).stream()
                .map(hit -> hit.source)
                .map(IndexedPointOfInterest::toPointOfInterest)
                .collect(toSet());
    }

    private String byLocationWithinQuery(Circle circle) {
        return new SearchSourceBuilder().query(
                QueryBuilders.boolQuery()
                .must(QueryBuilders.matchAllQuery())
                .filter(QueryBuilders.geoDistanceQuery("location")
                        .point(circle.getX(), circle.getY())
                        .distance(circle.getRadius(), DistanceUnit.KILOMETERS))
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
