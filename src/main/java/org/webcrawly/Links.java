package org.webcrawly;

import java.net.URI;

public class Links {

    public enum LinkType {
        Page,
        Image
    }

    interface LinkResult {
        LinkType type();
    }

    /**
     * represents a valid link
     */
    public static record Link(URI uri, LinkType type) implements LinkResult {
    }

    /**
     * represents a link with incorrect syntax
     */
    public static record LinkError(String url, LinkType type, CrawlerError error) implements LinkResult {
    }


    /**
     * @param source the page where the url was found (needed as the url can be relative)
     * @return a (valid or invalid) link for the url
     */
    static LinkResult createLink(URI source, String url, LinkType type) {
        try {
            final URI uri = source.resolve(url);
            // this checks if uri is valid for us
            uri.toURL();
            return new Link(uri, type);
        } catch (Exception e) {
            return new LinkError(url, type, CrawlerError.error(e));
        }
    }

    static LinkResult toAbsolute(URI uri, LinkResult linkResult) {
        if(linkResult instanceof Link link){
            return new Link(Functions.absolute(uri, link.uri()), link.type());
        }
        return linkResult;
    }
}
