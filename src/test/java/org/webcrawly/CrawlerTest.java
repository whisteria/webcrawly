package org.webcrawly;

import org.junit.Test;
import org.webcrawly.Pages.Link;
import org.webcrawly.Pages.Page;
import org.webcrawly.Pages.PageError;
import org.webcrawly.Pages.PageResult;

import java.util.Map;
import java.util.Set;

import static java.net.URI.create;
import static org.junit.Assert.assertEquals;
import static org.webcrawly.MockFetcher.NOT_FOUND;

public class CrawlerTest {

    private static final String startUrl = "http://www.webcrawly.com";

    private static Link page(String link) {
        return new Link(create(link), Pages.LinkType.Page);
    }

    private static Link img(String link) {
        return new Link(create(link), Pages.LinkType.Image);
    }

    private static final Map<String, Page> worldWideWeb = Map.of(
            startUrl,
            new Page(Set.of(page("http://weather.webcrawly.com"), page("http://news.webcrawly.com"), page("http://sport.webcrawly.com"))),
            "http://weather.webcrawly.com",
            new Page(Set.of(img("/weather.png"), page("http://www.webcrawly.com"), page("http://weather.webcrawly.com/brazil"), page("/london"))),
            "http://news.webcrawly.com",
            new Page(Set.of(img("/news.png"), page("http://www.webcrawly.com"), page("http://news.webcrawly.com/us-elections"), page("http://bloomberg.com")))
    );

    private final static MockFetcher mockFetcher = new MockFetcher(worldWideWeb);

    @Test
    public void crawl() {
        Map<String, PageResult> expected = Map.of(
                startUrl,
                new Page(Set.of(page("http://weather.webcrawly.com"), page("http://news.webcrawly.com"), page("http://sport.webcrawly.com"))),
                "http://weather.webcrawly.com",
                new Page(Set.of(img("http://weather.webcrawly.com/weather.png"), page("http://www.webcrawly.com"), page("http://weather.webcrawly.com/brazil"), page("http://weather.webcrawly.com/london"))),
                "http://news.webcrawly.com",
                new Page(Set.of(img("http://news.webcrawly.com/news.png"), page("http://www.webcrawly.com"), page("http://news.webcrawly.com/us-elections"), page("http://bloomberg.com"))),
                "http://weather.webcrawly.com/london",
                new PageError(NOT_FOUND),
                "http://sport.webcrawly.com",
                new PageError(NOT_FOUND)

        );
        assertEquals(expected, Crawler.crawl(startUrl, mockFetcher));
    }
}