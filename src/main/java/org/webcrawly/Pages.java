package org.webcrawly;

import com.google.common.net.InternetDomainName;

import java.net.URI;
import java.util.Set;

public class Pages {

    public enum LinkType {
        Page,
        Image
    }

    interface LinkResult {
    }

    public static record Error(String error, String errorClass) {
    }

    /**
     * the valid link
     *
     * @param uri an absolute uri
     */
    public static record Link(URI uri, LinkType type) implements LinkResult {
    }

    public static record LinkError(String url, LinkType type, Error error) implements LinkResult {
    }

    /**
     * @param source the page where the url was found
     * @param url
     * @param type
     * @return
     */
    public static LinkResult create(URI source, String url, LinkType type) {
        try {
            final URI uri = source.resolve(url);
            // this checks if uri is valid for us
            uri.toURL();
            return new Link(uri, type);
        } catch (Exception e) {
            return new LinkError(url, type, new Error(e.getMessage(), e.getClass().getSimpleName()));
        }
    }

    static String rootDomain(URI uri) {
        return InternetDomainName.from(uri.getAuthority()).topPrivateDomain().toString();
    }


    interface PageResult {
    }

    /**
     * correspondents to a web page with all its links
     */
    public static record Page(Set<LinkResult> links) implements PageResult {
    }

    public static record PageError(Error error) implements PageResult {
    }

    interface PageFetcher {
        PageResult fetch(Link link);
    }

}
