package org.webcrawly.crawler;

import org.junit.Test;
import org.webcrawly.MockPageCrawler;
import org.webcrawly.domain.Links.Link;
import org.webcrawly.domain.Pages.Page;
import org.webcrawly.domain.Pages.PageError;
import org.webcrawly.domain.Pages.PageResult;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.net.URI.create;
import static org.junit.Assert.assertEquals;
import static org.webcrawly.MockPageCrawler.NOT_FOUND;
import static org.webcrawly.domain.Links.LinkType.ANCHOR;
import static org.webcrawly.domain.Links.LinkType.MEDIA;

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
        return new Link(create(link), ANCHOR);
    }

    private static Link media(String link) {
        return new Link(create(link), MEDIA);
    }

    private static final Map<URI, Page> worldWideWeb = Map.of(
            startUri,
            new Page(startUri, Set.of(page("http://weather.webcrawly.com"), page("http://news.webcrawly.com"), page("http://sport.webcrawly.com"))),
            weather,
            new Page(weather, Set.of(media("/weather.png"), page("http://www.webcrawly.com"), page("http://weather.webcrawly.com/brazil"), page("/london"))),
            news,
            new Page(news, Set.of(media("/news.png"), page("http://www.webcrawly.com"), page("http://news.webcrawly.com/us-elections"), page("http://bloomberg.com")))
    );

    private final static MockPageCrawler mockFetcher = new MockPageCrawler(worldWideWeb);

    @Test
    public void crawl() {
        Map<URI, PageResult> expected = Map.of(
                startUri,
                new Page(startUri, Set.of(page("http://weather.webcrawly.com"), page("http://news.webcrawly.com"), page("http://sport.webcrawly.com"))),
                weather,
                new Page(weather, Set.of(media("/weather.png"), page("http://www.webcrawly.com"), page("http://weather.webcrawly.com/brazil"), page("/london"))),
                news,
                new Page(news, Set.of(media("/news.png"), page("http://www.webcrawly.com"), page("http://news.webcrawly.com/us-elections"), page("http://bloomberg.com"))),
                weatherLondon,
                new PageError(weatherLondon, NOT_FOUND),
                weatherBrazil,
                new PageError(weatherBrazil, NOT_FOUND),
                sport,
                new PageError(sport, NOT_FOUND),
                newsUsElections,
                new PageError(newsUsElections, NOT_FOUND)
        );
        assertEquals(expected, SiteCrawler.crawl(startUri, mockFetcher, env).results());
        mockFetcher.assertCalledExactlyOnce(expected.keySet());
    }
}