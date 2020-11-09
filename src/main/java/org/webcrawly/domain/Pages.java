package org.webcrawly.domain;

import org.webcrawly.CrawlerError;
import org.webcrawly.domain.Links.LinkResult;

import java.net.URI;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.webcrawly.domain.Links.toAbsolute;

public interface Pages {

    interface PageResult {
        URI uri();
    }

    /**
     * represents a web page with all its links
     * valid links will be converted to absolute links if needed
     */
    record Page(URI uri, Set<LinkResult> links) implements PageResult {

        public Page {
            this.uri = uri;
            this.links = links.stream().map(link -> toAbsolute(uri, link)).collect(toSet());
        }

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
