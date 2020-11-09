package org.webcrawly.crawler;

import org.webcrawly.domain.Pages.PageResult;

import java.net.URI;
import java.util.Map;

/**
 * @param completed whether crawl was completed in time
 */
public record CrawlerResult(Map<URI, PageResult> results, boolean completed) {
}
