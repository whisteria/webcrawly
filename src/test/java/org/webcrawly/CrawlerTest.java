package org.webcrawly;

import org.junit.Test;
import org.webcrawly.Pages.Link;
import org.webcrawly.Pages.Page;
import org.webcrawly.Pages.PageError;
import org.webcrawly.Pages.PageResult;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import static java.net.URI.create;
import static org.junit.Assert.assertEquals;
import static org.webcrawly.MockFetcher.NOT_FOUND;

public class CrawlerTest {

    private static final URI startUri = URI.create("http://www.webcrawly.com");
    private static final URI weather = URI.create("http://weather.webcrawly.com");
    private static final URI news = URI.create("http://news.webcrawly.com");
    private static final URI newsUsElections = URI.create("http://news.webcrawly.com/us-elections");
    private static final URI sport = URI.create("http://sport.webcrawly.com");
    private static final URI weatherLondon = URI.create("http://weather.webcrawly.com/london");
    private static final URI weatherBrazil = URI.create("http://weather.webcrawly.com/brazil");

    private static Link page(String link) {
        return new Link(create(link), Pages.LinkType.Page);
    }

    private static Link img(String link) {
        return new Link(create(link), Pages.LinkType.Image);
    }

    private static final Map<URI, Page> worldWideWeb = Map.of(
            startUri,
            new Page(startUri, Set.of(page("http://weather.webcrawly.com"), page("http://news.webcrawly.com"), page("http://sport.webcrawly.com"))),
            weather,
            new Page(weather, Set.of(img("/weather.png"), page("http://www.webcrawly.com"), page("http://weather.webcrawly.com/brazil"), page("/london"))),
            news,
            new Page(news, Set.of(img("/news.png"), page("http://www.webcrawly.com"), page("http://news.webcrawly.com/us-elections"), page("http://bloomberg.com")))
    );

    private final static MockFetcher mockFetcher = new MockFetcher(worldWideWeb);

    @Test
    public void crawl() {
        Map<URI, PageResult> expected = Map.of(
                startUri,
                new Page(startUri, Set.of(page("http://weather.webcrawly.com"), page("http://news.webcrawly.com"), page("http://sport.webcrawly.com"))),
                weather,
                new Page(weather, Set.of(img("/weather.png"), page("http://www.webcrawly.com"), page("http://weather.webcrawly.com/brazil"), page("/london"))),
                news,
                new Page(news, Set.of(img("/news.png"), page("http://www.webcrawly.com"), page("http://news.webcrawly.com/us-elections"), page("http://bloomberg.com"))),
                weatherLondon,
                new PageError(weatherLondon, NOT_FOUND),
                weatherBrazil,
                new PageError(weatherBrazil, NOT_FOUND),
                sport,
                new PageError(sport, NOT_FOUND),
                newsUsElections,
                new PageError(newsUsElections, NOT_FOUND)
        );
        assertEquals(expected, Crawler.crawl(startUri, mockFetcher));
        mockFetcher.assertCalledExactlyOnce(expected.keySet());
    }
}