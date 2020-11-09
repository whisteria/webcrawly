package org.webcrawly;

import org.webcrawly.Pages.PageResult;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        final ExecutionEnvironment env = new ExecutionEnvironment(
                Executors.newFixedThreadPool(3),
                1,
                TimeUnit.MINUTES
        );
        final Map<URI, PageResult> crawl = SiteCrawler.crawl(URI.create("http://www.tagesspiegel.de"), new JSoupPageCrawler(), env);
        System.out.println(crawl);
    }
}
