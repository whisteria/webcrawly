package org.webcrawly;

public record CrawlerError(String error, String type) {

    public static CrawlerError error(Exception e) {
        return new CrawlerError(e.getMessage(), e.getClass().getSimpleName());
    }
}
