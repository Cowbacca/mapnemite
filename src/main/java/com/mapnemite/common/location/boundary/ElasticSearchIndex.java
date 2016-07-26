package com.mapnemite.common.location.boundary;

import com.mapnemite.common.location.domain.Circle;
import com.mapnemite.common.location.domain.Rectangle;
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
import io.searchbox.params.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public class ElasticSearchIndex<M, I> {
    private final JestClient jestClient;
    private final String name;
    private final String type;
    private final Class<? extends I> indexedType;

    public ElasticSearchIndex(JestClient jestClient, String name, String type, Class<I> indexedType) {
        this.jestClient = jestClient;
        this.name = name;
        this.type = type;
        this.indexedType = indexedType;
    }

    public void createIndexIfNoExists(String mapping) {
        boolean indexExists = execute(new IndicesExists.Builder(name).build()).isSucceeded();
        if (!indexExists) {
            createIndex(mapping);
        }
    }

    private void createIndex(String mapping) {
        CreateIndex createIndexAction = new CreateIndex.Builder(name).build();
        execute(createIndexAction);

        PutMapping putMappingAction = new PutMapping.Builder(
                name,
                type,
                mapping
        ).build();
        execute(putMappingAction);
    }

    public void save(M model, Function<M, I> indexedModelBuilder, Function<I, String> idFunction) {
        I source = indexedModelBuilder.apply(model);
        Index indexAction = new Index.Builder(source)
                .index(name)
                .type(type)
                .id(idFunction.apply(source))
                .build();
        execute(indexAction);
    }

    public Stream<M> searchBy(SearchSourceBuilder searchSourceBuilder, Function<I, M> indexedTypeToModelTypeFunction) {
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(name)
                .setParameter(Parameters.SIZE, 100)
                .build();
        SearchResult results = execute(search);

        return results.getHits(indexedType).stream()
                .map(hit -> hit.source)
                .map(indexedTypeToModelTypeFunction::apply);
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

    public static GeoDistanceQueryBuilder circleGeoFilter(String field, Circle circle) {
        return QueryBuilders.geoDistanceQuery(field)
                .point(circle.getX(), circle.getY())
                .distance(circle.getRadius(), DistanceUnit.KILOMETERS);
    }

    public static GeoBoundingBoxQueryBuilder rectangleGeoFilter(String field, Rectangle rectangle) {
        return QueryBuilders.geoBoundingBoxQuery(field)
                .topRight(rectangle.getTopRight().getLatitude(), rectangle.getTopRight().getLongitude())
                .bottomLeft(rectangle.getBottomLeft().getLatitude(), rectangle.getBottomLeft().getLongitude());
    }
}
