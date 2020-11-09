package org.webcrawly.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class JSoupPageCrawlerTest {

    private final static String html = """
                <a href='https://www.bbc.co.uk/news'/>
                <a href='https://www.bbc.co.uk/sport'/>
                <a href='https://www.bbc.co.uk/weather'/>
                <img src="img1.jpg">
                <img src="index/img2.jpg">
                """;

    private final static Document document = Jsoup.parseBodyFragment(html);

    @Test
    public void getLinks() {
        final Set<String> expected = Set.of(
                "https://www.bbc.co.uk/news",
                "https://www.bbc.co.uk/sport",
                "https://www.bbc.co.uk/weather"
        );
        assertEquals(expected, JSoupPageCrawler.getAnchors(document));
    }

    @Test
    public void getMedia() {
        final Set<String> expected = Set.of(
                "img1.jpg",
                "index/img2.jpg"
        );
        assertEquals(expected, JSoupPageCrawler.getMedia(document));
    }
}