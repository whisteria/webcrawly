package org.webcrawly.crawler;

import org.webcrawly.Functions;
import org.webcrawly.domain.Links;
import org.webcrawly.domain.Links.Link;
import org.webcrawly.domain.Pages.Page;
import org.webcrawly.domain.Pages.PageCrawler;
import org.webcrawly.domain.Pages.PageResult;
import org.webcrawly.domain.Pages.PageResultCallback;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.webcrawly.Functions.rootDomain;
import static org.webcrawly.domain.Links.LinkType.ANCHOR;

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
    private int counter = 0;

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


    private CrawlerResult execute() {
        submit(startUri);
        final boolean await = env.await();
        return new CrawlerResult(result, await);
    }

    private void submit(URI uri) {
        executing.add(uri);
        env.submit(() -> pageCrawler.start(uri, this));
    }

    @Override
    public synchronized void process(PageResult pageResult) {
        counter++;
        if (counter % 10 == 0) {
            System.out.print('.');
        }
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
        page.links().get(ANCHOR)
                .stream()
                .map(url -> Links.createLink(page.uri(), url, ANCHOR))
                .filter(linkResult -> linkResult instanceof Link)
                .map(linkResult -> ((Link) linkResult).uri())
                .filter(uri -> Functions.isInternal(rootDomain, uri))
                .filter(Functions::isHttp)
                .map(Functions::crawlerUri)
                .filter(this::stillToDo)
                .forEach(this::submit);
    }

    private boolean stillToDo(URI uri) {
        return !result.containsKey(uri) && !executing.contains(uri);
    }

    /**
     * public method to trigger the crawl
     */
    public static CrawlerResult crawl(URI startUri, PageCrawler fetcher, ExecutionEnvironment env) {
        return new SiteCrawler(startUri, fetcher, env).execute();
    }

}
