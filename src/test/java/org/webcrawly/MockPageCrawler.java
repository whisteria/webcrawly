package org.webcrawly;

import org.webcrawly.Pages.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MockPageCrawler implements PageCrawler {

    public static final CrawlerError NOT_FOUND = new CrawlerError("not found", IOException.class.getSimpleName());

    private final Map<URI, Page> worldWideWeb;

    // to ensure we are called max once per URI
    final Map<URI, Integer> counts = new HashMap<>();

    public MockPageCrawler(Map<URI, Page> worldWideWeb) {
        this.worldWideWeb = worldWideWeb;
    }

    @Override
    public void fetch(URI uri, PageResultCallback callback) {
        inc(uri);
        final Page page = worldWideWeb.get(uri);
        if (page != null) {
            callback.process(page);
        } else {
            callback.process(new PageError(uri, NOT_FOUND));
        }
    }

    private void inc(URI uri) {
        counts.put(uri, counts.containsKey(uri) ? counts.get(uri) + 1 : 1);
    }

    public void assertCalledExactlyOnce(Set<URI> uris) {
        // all uris were called
        assertEquals(uris.size(), counts.size());
        // and only once
        uris.forEach(uri -> assertEquals(counts.get(uri), Integer.valueOf(1)));
    }
}
