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
        LinkType type();
    }

    public static record Error(String error, String errorClass) {
    }

    /**
     * represents a valid link
     */
    public static record Link(URI uri, LinkType type) implements LinkResult {
    }

    /**
     *  represents a link with incorrect syntax
     */
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
        URI uri();
    }

    /**
     * correspondents to a web page with all its links
     */
    public static record Page(URI uri, Set<LinkResult> links) implements PageResult {
    }

    public static record PageError(URI uri, Error error) implements PageResult {
    }

    interface PageResultCallback {
        void process(PageResult result);
    }

    interface PageFetcher {
        void fetch(URI uri, PageResultCallback callback);
    }

}
