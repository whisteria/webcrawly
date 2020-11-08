package org.webcrawly;

import org.webcrawly.Pages.Error;
import org.webcrawly.Pages.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MockFetcher implements PageFetcher {

    private final Map<URI, Page> worldWideWeb;

    // to ensure we are called max once per URI
    final Map<URI, Integer> counts = new HashMap<>();

    public MockFetcher(Map<URI, Page> worldWideWeb) {
        this.worldWideWeb = worldWideWeb;
    }


    @Override
    public PageResult fetch(Link link) {
        inc(link.uri());
        final Page page = worldWideWeb.get(link.uri());
        if (page != null) {
            return page;
        } else {
            return new PageError(new Error("not found", IOException.class.getSimpleName()));
        }
    }

    private void inc(URI uri) {
        counts.put(uri, counts.containsKey(uri) ? counts.get(uri) + 1 : 1);
    }
}
