package org.webcrawly;

import java.net.URI;

public class Pages {

    public enum LinkType {
        Page,
        Image
    }

    interface PageLink {
    }

    public static record Error(String error, String errorClass){
    }

    /**
     * the valid link
     *
     * @param uri an absolute uri
     */
    public static record Link(URI uri, LinkType type) implements PageLink {
    }

    public static record LinkError(String url, LinkType type, Error error) implements PageLink {
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
            return new LinkError(url, type, new Error(e.getMessage(), e.getClass().getSimpleName()));
        }
    }

    static String rootDomain(URI uri) {
        return null;
    }

}
