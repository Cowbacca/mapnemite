package com.mapnemite.notification.boundary;

import com.mapnemite.common.location.domain.location.Circle;
import com.mapnemite.notification.domain.subscriber.Subscriber;
import com.mapnemite.notification.domain.subscriber.SubscriberRepository;
import com.mapnemite.pointofinterest.boundary.ElasticSearchException;
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
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.util.stream.Stream;

@Component
@Slf4j
public class ElasticSearchSubscriberRepository implements SubscriberRepository {
    public static final String INDEX_NAME = "subscribers";
    private static final String INDEX_TYPE = "subscriber";
    private final JestClient jestClient;

    @Inject
    public ElasticSearchSubscriberRepository(JestClient jestClient) {
        this.jestClient = jestClient;
        boolean indexExists = execute(new IndicesExists.Builder(INDEX_NAME).build()).isSucceeded();
        if (!indexExists) {
            createIndex();
        }
    }

    private void createIndex() {
        CreateIndex createIndexAction = new CreateIndex.Builder(INDEX_NAME).build();
        execute(createIndexAction);

        PutMapping putMappingAction = new PutMapping.Builder(
                INDEX_NAME,
                INDEX_TYPE,
                "{ \"properties\" : { \"lastKnownLocation\" : {\"type\" : \"geo_point\"} } }"
        ).build();
        execute(putMappingAction);
    }


    @Override
    public void save(Subscriber subscriber) {
        IndexedSubscriber source = new IndexedSubscriber(subscriber);
        Index indexAction = new Index.Builder(source).index(INDEX_NAME).type(INDEX_TYPE).id(source.getPublicKey()).build();
        execute(indexAction);
    }

    @Override
    public Stream<Subscriber> findByLocationWithin(Circle circle) {
        return findBy(circleGeoFilter(circle));
    }

    private GeoDistanceQueryBuilder circleGeoFilter(Circle circle) {
        return QueryBuilders.geoDistanceQuery("lastKnownLocation")
                .point(circle.getX(), circle.getY())
                .distance(circle.getRadius(), DistanceUnit.KILOMETERS);
    }

    private Stream<Subscriber> findBy(QueryBuilder filter) {
        Search search = new Search.Builder(filterBy(filter))
                .addIndex(INDEX_NAME)
                .build();
        SearchResult results = execute(search);

        return results.getHits(IndexedSubscriber.class).stream()
                .map(hit -> hit.source)
                .map(IndexedSubscriber::toSubscriber);
    }


    private String filterBy(QueryBuilder geoFilter) {
        return new SearchSourceBuilder().query(
                QueryBuilders.boolQuery()
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
