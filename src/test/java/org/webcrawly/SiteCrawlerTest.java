package org.webcrawly;

import org.junit.Test;
import org.webcrawly.Links.Link;
import org.webcrawly.Pages.Page;
import org.webcrawly.Pages.PageError;
import org.webcrawly.Pages.PageResult;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.net.URI.create;
import static org.junit.Assert.assertEquals;
import static org.webcrawly.MockCrawler.NOT_FOUND;

public class SiteCrawlerTest {

    private static final ExecutionEnvironment env = new ExecutionEnvironment(
            Executors.newFixedThreadPool(3),
            1,
            TimeUnit.SECONDS
    );

    private static final URI startUri = URI.create("http://www.webcrawly.com");
    private static final URI weather = URI.create("http://weather.webcrawly.com");
    private static final URI news = URI.create("http://news.webcrawly.com");
    private static final URI newsUsElections = URI.create("http://news.webcrawly.com/us-elections");
    private static final URI sport = URI.create("http://sport.webcrawly.com");
    private static final URI weatherLondon = URI.create("http://weather.webcrawly.com/london");
    private static final URI weatherBrazil = URI.create("http://weather.webcrawly.com/brazil");

    private static Link page(String link) {
        return new Link(create(link), Links.LinkType.Page);
    }

    private static Link img(String link) {
        return new Link(create(link), Links.LinkType.Image);
    }

    private static final Map<URI, Page> worldWideWeb = Map.of(
            startUri,
            new Page(startUri, Set.of(page("http://weather.webcrawly.com"), page("http://news.webcrawly.com"), page("http://sport.webcrawly.com"))),
            weather,
            new Page(weather, Set.of(img("/weather.png"), page("http://www.webcrawly.com"), page("http://weather.webcrawly.com/brazil"), page("/london"))),
            news,
            new Page(news, Set.of(img("/news.png"), page("http://www.webcrawly.com"), page("http://news.webcrawly.com/us-elections"), page("http://bloomberg.com")))
    );

    private final static MockCrawler mockFetcher = new MockCrawler(worldWideWeb);

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
        assertEquals(expected, SiteCrawler.crawl(startUri, mockFetcher, env));
        mockFetcher.assertCalledExactlyOnce(expected.keySet());
    }
}