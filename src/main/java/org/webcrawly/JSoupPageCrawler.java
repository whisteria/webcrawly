package org.webcrawly;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.webcrawly.Links.LinkResult;
import org.webcrawly.Pages.Page;
import org.webcrawly.Pages.PageCrawler;
import org.webcrawly.Pages.PageResultCallback;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.webcrawly.Links.LinkType.Image;
import static org.webcrawly.Links.LinkType.Page;

public class JSoupPageCrawler implements PageCrawler {

    static Set<String> getLinks(Document document) {
        return document.select("a[href]").stream().map(n -> n.attr("href")).collect(toSet());
    }

    static Set<String> getImages(Document document) {
        return document.select("img").stream().map(n -> n.attr("src")).collect(toSet());
    }

    @Override
    public void fetch(URI uri, PageResultCallback callback) throws Exception {
        // todo millis are hard coded
        final Document document = Jsoup.parse(uri.toURL(), 1000);
        final Set<LinkResult> links = new HashSet<>();
        getLinks(document).forEach(url -> links.add(Links.createLink(uri, url, Page)));
        getImages(document).forEach(url -> links.add(Links.createLink(uri, url, Image)));
        callback.process(new Page(uri, links));
    }
}
