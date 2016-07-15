package com.mapnemite.pointofinterest.boundary;

public class ElasticSearchException extends RuntimeException {
    public ElasticSearchException(String message) {
        super(message);
    }

    public ElasticSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
