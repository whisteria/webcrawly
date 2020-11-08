package org.webcrawly;

import java.net.URI;

public class Pages {

    public enum LinkType {
        Page,
        Image
    }

    interface PageLink {
    }

    /**
     * the valid link
     *
     * @param uri an absolute uri
     */
    public static record Link(URI uri, LinkType type) implements PageLink {
    }

    public static record LinkError(String url, LinkType type, String error, String errorClass) implements PageLink {
    }

    /**
     * @param source the page where the url was found
     * @param url
     * @param type
     * @return
     */
    public static PageLink create(URI source, String url, LinkType type) {
        try {
            final URI uri = source.resolve(url);
            return new Link(uri, type);
        } catch (Exception e) {
            return new LinkError(url, type, e.getMessage(), e.getClass().getSimpleName());
        }
    }

}
