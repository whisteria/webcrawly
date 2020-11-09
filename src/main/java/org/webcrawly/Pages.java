package org.webcrawly;

import org.webcrawly.Links.LinkResult;

import java.net.URI;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.webcrawly.Links.toAbsolute;

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
    // todo deal with potential exceptions during crawling
    interface PageCrawler {
        void fetch(URI uri, PageResultCallback callback);
    }

}
