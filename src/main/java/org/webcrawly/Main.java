package org.webcrawly;

import org.webcrawly.crawler.CrawlerResult;
import org.webcrawly.crawler.ExecutionEnvironment;
import org.webcrawly.crawler.JSoupPageCrawler;
import org.webcrawly.crawler.SiteCrawler;
import org.webcrawly.report.ConsoleReporter;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        if (args.length != 2) {
            usageWithExit();
        }

        final ExecutionEnvironment env = new ExecutionEnvironment(
                Executors.newFixedThreadPool(getThreads(args[1])),
                2,
                TimeUnit.MINUTES
        );
        final String url = args[0];
        System.out.println("starting to crawl " + url);
        final CrawlerResult crawl = SiteCrawler.crawl(URI.create(url), new JSoupPageCrawler(), env);
        new ConsoleReporter().report(crawl);
    }

    public static int getThreads(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (Exception e) {
            System.err.println("could not get number of threads from " + arg);
            usageWithExit();
            return 0;
        }

    }

    private static void usageWithExit() {
        System.err.println("usage: <url> <numbers of threads>");
        System.err.println("example: http://wiprodigital.com 15");
        System.exit(-1);
    }
}
