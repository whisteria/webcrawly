package org.webcrawly.report;

import org.webcrawly.crawler.CrawlerResult;
import org.webcrawly.domain.Pages.Page;
import org.webcrawly.domain.Pages.PageError;
import org.webcrawly.domain.Pages.PageResult;

public class ConsoleReporter implements Reporter {

    public static final String newSection = "--------------------------------";

    @Override
    public void report(CrawlerResult result) {
        System.out.println("completed: " + result.completed() + " <" + result.results().size() + ">");
        result.results().values().forEach(this::reportPageResult);
    }

    private void reportPageResult(PageResult pageResult) {
        System.out.println(newSection + " " + pageResult.uri());
        if (pageResult instanceof Page page) {
            reportPage(page);
        } else {
            reportError((PageError) pageResult);
        }
    }

    private void reportError(PageError pageError) {
        System.out.println("❌ " + pageError.error());
    }

    private void reportPage(Page page) {
        page.links().values().forEach(links -> links.forEach(this::showLink));
    }

    private void showLink(String link) {
        System.out.println("❇️ " + link);
    }

}
