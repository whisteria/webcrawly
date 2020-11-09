package org.webcrawly.report;

import org.webcrawly.crawler.CrawlerResult;

public interface Reporter {
    void report(CrawlerResult result);
}
