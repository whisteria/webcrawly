package org.webcrawly.domain;

import org.junit.Test;
import org.webcrawly.domain.Links.LinkError;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.webcrawly.domain.Links.Link;
import static org.webcrawly.domain.Links.LinkType.Image;
import static org.webcrawly.domain.Links.LinkType.Page;
import static org.webcrawly.domain.Links.createLink;

public class LinksTest {

    private static final String source = "http://www.webcrawly.com";
    private static final String news = "http://news.webcrawly.com";
    private static final String external = "http://www.external.com";
    private static final String newsIndex = "http://news.webcrawly.com/index";
    private static final String logo = "/images/logo.png";

    @Test
    public void createValidLink() {
        assertEquals(new Link(URI.create(news), Page), createLink(URI.create(source), news, Page));
        assertEquals(new Link(URI.create(source + logo), Image), createLink(URI.create(source), logo, Image));
        assertEquals(new Link(URI.create(news + logo), Image), createLink(URI.create(newsIndex), logo, Image));
        assertEquals(new Link(URI.create(external + logo), Page), createLink(URI.create(external), logo, Page));
    }

    @Test
    public void createInvalidLink() {
        assertTrue(createLink(URI.create(source), "not a valid link", Page) instanceof LinkError);
    }

}