package org.webcrawly;

import org.webcrawly.Pages.*;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Crawler implements PageResultCallback {

    private final URI startUri;
    private final String rootDomain;
    private final PageFetcher pageFetcher;

    // todo hard coded
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    // mutable state
    private final Set<URI> executing = new HashSet<>();
    private final Map<URI, PageResult> result = new HashMap<>();

    private Crawler(URI startUri, PageFetcher pageFetcher) {
        this.startUri = startUri;
        this.rootDomain = Pages.rootDomain(startUri);
        this.pageFetcher = pageFetcher;
    }


    private Map<URI, PageResult> execute() {
        executing.add(startUri);
        submit(startUri);
        // todo hard coded
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // todo
        }
        return result;
    }

    private void submit(URI uri) {
        executorService.submit(() -> pageFetcher.fetch(uri, this));
    }

    @Override
    public synchronized void process(PageResult pageResult) {
        if (executing.remove(pageResult.uri())) {
            result.put(pageResult.uri(), pageResult);
            if (pageResult instanceof Page page) {
                submitMissingResources(page);
            }
            checkCompletion();
        }
    }

    private void checkCompletion() {
        if (executing.isEmpty()) {
            executorService.shutdown();
        }
    }

    private void submitMissingResources(Page page) {
        page.links()
                .stream()
                .filter(linkResult -> LinkType.Page.equals(linkResult.type()))
                .filter(linkResult -> linkResult instanceof Link)
                .map(linkResult -> ((Link) linkResult).uri())
                .map(uri -> absolute(page.uri(), uri))
                .filter(this::isInternal)
                .filter(this::stillToDo)
                .forEach(uri -> {
                    executing.add(uri);
                    submit(uri);
                });
    }

    private boolean isInternal(URI uri) {
        return rootDomain.equals(Pages.rootDomain(uri));
    }

    private boolean stillToDo(URI uri) {
        return !result.containsKey(uri) && !executing.contains(uri);
    }

    public static Map<URI, PageResult> crawl(URI startUri, PageFetcher fetcher) {
        return new Crawler(startUri, fetcher).execute();
    }

    public static URI absolute(URI base, URI uri) {
        return base.resolve(uri);
    }
}
