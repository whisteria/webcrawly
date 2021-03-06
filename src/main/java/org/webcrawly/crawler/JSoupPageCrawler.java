package org.webcrawly.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.webcrawly.domain.Links.LinkType;
import org.webcrawly.domain.Pages.Page;
import org.webcrawly.domain.Pages.PageCrawler;
import org.webcrawly.domain.Pages.PageResultCallback;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.webcrawly.domain.Links.LinkType.ANCHOR;
import static org.webcrawly.domain.Links.LinkType.MEDIA;

public class JSoupPageCrawler implements PageCrawler {

    static Set<String> getAnchors(Document document) {
        return document.select("a[href]").stream().map(n -> n.attr("href")).collect(toSet());
    }

    static Set<String> getMedia(Document document) {
        return document.select("[src]").stream().map(n -> n.attr("src")).collect(toSet());
    }

    @Override
    public void fetch(URI uri, PageResultCallback callback) throws Exception {
        // todo millis are hard coded
        final Document document = Jsoup.parse(uri.toURL(), 1000);
        final Map<LinkType, Set<String>> links = Map.of(
                ANCHOR, getAnchors(document),
                MEDIA, getMedia(document)
        );
        callback.process(new Page(uri, links));
    }
}
