package org.webcrawly;

import org.webcrawly.Pages.Error;
import org.webcrawly.Pages.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MockFetcher implements PageFetcher {

    public static final Error NOT_FOUND = new Error("not found", IOException.class.getSimpleName());

    private final Map<String, Page> worldWideWeb;

    // to ensure we are called max once per URI
    final Map<String, Integer> counts = new HashMap<>();

    public MockFetcher(Map<String, Page> worldWideWeb) {
        this.worldWideWeb = worldWideWeb;
    }


    @Override
    public PageResult fetch(String url) {
        inc(url);
        final Page page = worldWideWeb.get(url);
        if (page != null) {
            return page;
        } else {
            return new PageError(NOT_FOUND);
        }
    }

    private void inc(String url) {
        counts.put(url, counts.containsKey(url) ? counts.get(url) + 1 : 1);
    }
}
