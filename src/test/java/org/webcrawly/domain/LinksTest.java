package org.webcrawly.domain;

import org.junit.Test;
import org.webcrawly.domain.Links.LinkError;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.webcrawly.domain.Links.Link;
import static org.webcrawly.domain.Links.LinkType.ANCHOR;
import static org.webcrawly.domain.Links.LinkType.MEDIA;
import static org.webcrawly.domain.Links.createLink;

public class LinksTest {

    private static final String source = "http://www.webcrawly.com";
    private static final String news = "http://news.webcrawly.com";
    private static final String external = "http://www.external.com";
    private static final String newsIndex = "http://news.webcrawly.com/index";
    private static final String logo = "/images/logo.png";

    @Test
    public void createValidLink() {
        assertEquals(new Link(URI.create(news), ANCHOR), createLink(URI.create(source), news, ANCHOR));
        assertEquals(new Link(URI.create(source + logo), MEDIA), createLink(URI.create(source), logo, MEDIA));
        assertEquals(new Link(URI.create(news + logo), MEDIA), createLink(URI.create(newsIndex), logo, MEDIA));
        assertEquals(new Link(URI.create(external + logo), ANCHOR), createLink(URI.create(external), logo, ANCHOR));
    }

    @Test
    public void createInvalidLink() {
        assertTrue(createLink(URI.create(source), "not a valid link", ANCHOR) instanceof LinkError);
    }

}