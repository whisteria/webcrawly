package org.webcrawly.domain;

import org.webcrawly.CrawlerError;
import org.webcrawly.domain.Links.LinkType;

import java.net.URI;
import java.util.Map;
import java.util.Set;

public interface Pages {

    interface PageResult {
        URI uri();
    }

    /**
     * represents a web page with all its links by type
     */
    record Page(URI uri, Map<LinkType, Set<String>> links) implements PageResult {
    }

    /**
     * represents a page which could not be crawled
     */
    record PageError(URI uri, CrawlerError error) implements PageResult {
    }

    interface PageResultCallback {
        void process(PageResult result);
    }

    /**
     * a crawler crawls one single page (URI) and calls the callback with the result
     */
    interface PageCrawler {
        void fetch(URI uri, PageResultCallback callback) throws Exception;

        default void start(URI uri, PageResultCallback callback) {
            try {
                fetch(uri, callback);
            } catch (Exception e) {
                callback.process(new PageError(uri, CrawlerError.error(e)));
            }
        }
    }

}
