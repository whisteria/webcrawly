package org.webcrawly;

import org.junit.Test;
import org.webcrawly.Pages.LinkError;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.webcrawly.Pages.Link;
import static org.webcrawly.Pages.LinkType.Image;
import static org.webcrawly.Pages.LinkType.Page;

public class PagesTest {

    private static final String source = "http://www.webcrawly.com";
    private static final String news = "http://news.webcrawly.com";
    private static final String external = "http://www.external.com";
    private static final String newsIndex = "http://news.webcrawly.com/index";
    private static final String logo = "/images/logo.png";

    @Test
    public void createValidLink() {
        assertEquals(new Link(URI.create(news), Page), Pages.create(URI.create(source), news, Page));
        assertEquals(new Link(URI.create(source + logo), Image), Pages.create(URI.create(source), logo, Image));
        assertEquals(new Link(URI.create(news + logo), Image), Pages.create(URI.create(newsIndex), logo, Image));
        assertEquals(new Link(URI.create(external + logo), Page), Pages.create(URI.create(external), logo, Page));
    }

    @Test
    public void createInvalidLink() {
        assertTrue(Pages.create(URI.create(source), "not a valid link", Page) instanceof LinkError);

    }


    @Test
    public void rootDomain() {
        assertEquals("abc.com", Pages.rootDomain(URI.create("http://www.abc.com?name=bob#tag")));
        assertEquals("abc.com", Pages.rootDomain(URI.create("http://weather.abc.com/index.html")));
    }

}