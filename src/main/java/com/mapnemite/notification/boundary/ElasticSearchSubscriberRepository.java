package com.mapnemite.notification.boundary;

import com.mapnemite.common.location.boundary.ElasticSearchIndex;
import com.mapnemite.common.location.domain.Circle;
import com.mapnemite.notification.domain.subscriber.Subscriber;
import com.mapnemite.notification.domain.subscriber.SubscriberRepository;
import io.searchbox.client.JestClient;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.stream.Stream;

import static com.mapnemite.common.location.boundary.ElasticSearchIndex.circleGeoFilter;

@Component
@Slf4j
public class ElasticSearchSubscriberRepository implements SubscriberRepository {
    public static final String INDEX_NAME = "subscribers";
    private static final String INDEX_TYPE = "subscriber";
    private final ElasticSearchIndex<Subscriber, IndexedSubscriber> elasticSearchIndex;

    @Inject
    public ElasticSearchSubscriberRepository(JestClient jestClient) {
        this.elasticSearchIndex = new ElasticSearchIndex<>(jestClient, INDEX_NAME, INDEX_TYPE, IndexedSubscriber.class);
        elasticSearchIndex.createIndexIfNoExists("{ \"properties\" : { \"lastKnownLocation\" : {\"type\" : \"geo_point\"} } }");
    }

    @Override
    public void save(Subscriber subscriber) {
        elasticSearchIndex.save(subscriber, IndexedSubscriber::new, IndexedSubscriber::getPublicKey);
    }

    @Override
    public Stream<Subscriber> findByLocationWithin(Circle circle) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(
                QueryBuilders.boolQuery()
                        .filter(circleGeoFilter("lastKnownLocation", circle))
        );
        return elasticSearchIndex.searchBy(searchSourceBuilder, IndexedSubscriber::toSubscriber);
    }

}
