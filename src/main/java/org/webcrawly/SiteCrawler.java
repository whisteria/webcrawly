package org.webcrawly;

import org.webcrawly.Links.LinkType;
import org.webcrawly.Pages.Page;
import org.webcrawly.Pages.PageCrawler;
import org.webcrawly.Pages.PageResult;
import org.webcrawly.Pages.PageResultCallback;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.webcrawly.Functions.rootDomain;

/**
 * the site crawler
 */
public class SiteCrawler implements PageResultCallback {

    private final URI startUri;
    private final String rootDomain;
    private final PageCrawler pageCrawler;
    private final ExecutionEnvironment env;


    // mutable state
    private final Set<URI> executing = new HashSet<>();
    private final Map<URI, PageResult> result = new HashMap<>();

    /**
     * stateful class so we need ot use one instance per execution.
     * this is ensured by this private constructor
     */
    private SiteCrawler(URI startUri, PageCrawler pageCrawler, ExecutionEnvironment env) {
        this.startUri = startUri;
        this.rootDomain = rootDomain(startUri);
        this.pageCrawler = pageCrawler;
        this.env = env;
    }


    private Map<URI, PageResult> execute() {
        executing.add(startUri);
        submit(startUri);
        env.await();
        return result;
    }

    private void submit(URI uri) {
        env.submit(() -> pageCrawler.start(uri, this));
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
            env.shutdown();
        }
    }

    private void submitMissingResources(Page page) {
        page.links()
                .stream()
                .filter(linkResult -> LinkType.Page.equals(linkResult.type()))
                .filter(linkResult -> linkResult instanceof Links.Link)
                .map(linkResult -> ((Links.Link) linkResult).uri())
                .filter(uri -> Functions.isInternal(rootDomain, uri))
                .filter(this::stillToDo)
                .forEach(uri -> {
                    executing.add(uri);
                    submit(uri);
                });
    }

    private boolean stillToDo(URI uri) {
        return !result.containsKey(uri) && !executing.contains(uri);
    }

    /**
     * public method to trigger the crawl
     */
    public static Map<URI, PageResult> crawl(URI startUri, PageCrawler fetcher, ExecutionEnvironment env) {
        return new SiteCrawler(startUri, fetcher, env).execute();
    }

}
