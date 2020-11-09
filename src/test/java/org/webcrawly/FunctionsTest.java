package org.webcrawly;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

public class FunctionsTest {

    @Test
    public void rootDomain() {
        assertEquals("abc.com", Functions.rootDomain(URI.create("http://abc.com")));
        assertEquals("abc.com", Functions.rootDomain(URI.create("http://www.abc.com?name=bob#tag")));
        assertEquals("abc.com", Functions.rootDomain(URI.create("http://weather.abc.com/gstindex.html")));
    }

    @Test
    public void isInternal() {
        assertTrue(Functions.isInternal("abc.com", URI.create("http://www.abc.com?name=bob#tag")));
        assertFalse(Functions.isInternal("b.com", URI.create("http://www.abc.com?name=bob#tag")));
    }

    @Test
    public void absolute() {
        assertEquals(
                URI.create("http://abc.com/index/main.png"),
                Functions.absolute(URI.create("http://abc.com/"), URI.create("index/main.png"))
        );
        assertEquals(
                URI.create("http://abc.com/index/main.png"),
                Functions.absolute(URI.create("http://abc.com/sport.html"), URI.create("index/main.png"))
        );
    }


    @Test
    public void crawlerUri() {

        assertEquals(
                URI.create("http://www.ts.de/suchergebnis/"),
                Functions.crawlerUri(URI.create("http://www.ts.de/suchergebnis/?search-ressort=2984&search-day=20200722"))
        );
    }
}