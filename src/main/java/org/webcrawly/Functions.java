package org.webcrawly;

import com.google.common.net.InternetDomainName;

import java.net.URI;
import java.util.Set;

public interface Functions {

    Set<String> http = Set.of("http", "https");

    static String rootDomain(URI uri) {
        return InternetDomainName.from(uri.getAuthority()).topPrivateDomain().toString();
    }

    static boolean isInternal(String rootDomain, URI uri) {
        return rootDomain.equals(rootDomain(uri));
    }

    static URI absolute(URI base, URI uri) {
        return base.resolve(uri);
    }

    /**
     * @return URI without query and fragments
     */
    static URI crawlerUri(URI uri) {
        return URI.create(String.format("%s://%s%s", uri.getScheme(), uri.getAuthority(), uri.getPath()));
    }

    static boolean isHttp(URI uri) {
        return http.contains(uri.getScheme());
    }
}
