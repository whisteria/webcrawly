package org.webcrawly;

import com.google.common.net.InternetDomainName;

import java.net.URI;

public interface Functions {

    static String rootDomain(URI uri) {
        return InternetDomainName.from(uri.getAuthority()).topPrivateDomain().toString();
    }

    static boolean isInternal(String rootDomain, URI uri) {
        return rootDomain.equals(rootDomain(uri));
    }

    static URI absolute(URI base, URI uri) {
        return base.resolve(uri);
    }

    static URI crawlerUri( URI uri) {
        return null;
    }
}
