package org.webcrawly;

import java.net.URI;

public class Pages {

    public enum LinkType {
        Page,
        Image
    }

    /**
     * the valid link
     * @param uri an absolute uri
     */
    public static record Link(URI uri, LinkType type) {

    }


    /**
     * @param source the page where the url was found
     * @param url
     * @param type
     * @return
     */
    public static Link create(URI source, String url, LinkType type) {
        return null;
    }

}
