package com.mapnemite.pointofinterest.boundary;

import com.mapnemite.common.location.boundary.ElasticSearchIndex;
import com.mapnemite.common.location.domain.Circle;
import com.mapnemite.common.location.domain.Rectangle;
import com.mapnemite.pointofinterest.domain.Lure;
import com.mapnemite.pointofinterest.domain.LureRepository;
import io.searchbox.client.JestClient;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.Set;

import static com.mapnemite.common.location.boundary.ElasticSearchIndex.circleGeoFilter;
import static com.mapnemite.common.location.boundary.ElasticSearchIndex.rectangleGeoFilter;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Repository
public class ElasticSearchLureRepository implements LureRepository {

    public static final String INDEX_NAME = "lures";
    private static final String INDEX_TYPE = "lure";
    private final ElasticSearchIndex<Lure, IndexedLure> elasticSearchIndex;

    @Inject
    public ElasticSearchLureRepository(JestClient jestClient) {
        this.elasticSearchIndex = new ElasticSearchIndex<>(jestClient, INDEX_NAME, INDEX_TYPE, IndexedLure.class);
        elasticSearchIndex.createIndexIfNoExists("{ \"properties\" : { \"location\" : {\"type\" : \"geo_point\"} } }");
    }


    @Override
    public void save(Lure lure) {
        elasticSearchIndex.save(lure, IndexedLure::new, IndexedLure::indexedId);
    }

    @Override
    public Set<Lure> findByLocationWithinAndNotExpired(Circle circle) {
        SearchSourceBuilder searchSourceBuilder = notExpiredAndFilterBy(circleGeoFilter("location", circle));
        return elasticSearchIndex.searchBy(searchSourceBuilder, IndexedLure::toLure)
                .collect(toSet());
    }

    @Override
    public Set<Lure> findByLocationWithinAndNotExpired(Rectangle rectangle) {
        GeoBoundingBoxQueryBuilder geoFilter = rectangleGeoFilter("location", rectangle);
        SearchSourceBuilder searchSourceBuilder = notExpiredAndFilterBy(geoFilter);
        return elasticSearchIndex.searchBy(searchSourceBuilder, IndexedLure::toLure)
                .collect(toSet());
    }

    private SearchSourceBuilder notExpiredAndFilterBy(QueryBuilder geoFilter) {
        return new SearchSourceBuilder().query(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.rangeQuery("expiresAt").gt(System.currentTimeMillis()))
                        .filter(geoFilter)
        );
    }
}
