package org.webcrawly;

import org.jsoup.nodes.Document;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class JSoupPageCrawler {

    static Set<String> getLinks(Document document) {
        return document.select("a[href]").stream().map(n -> n.attr("href")).collect(toSet());
    }

    static Set<String> getImages(Document document) {
        return document.select("img").stream()
                .map(n -> n.attr("src")).collect(toSet());
    }


}
