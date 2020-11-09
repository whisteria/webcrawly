## summary
Little java program to web-crawl a domain.
it uses java 14 with enabled feature preview to get the benefit of some a newer java version, in particular records.

## design rationales
- small classes with well-defined concerns
- where ever possible classes are either stateless or immutable, or have a controlled access (like the [SiteCrawler](src/main/java/org/webcrawly/crawler/SiteCrawler.java))
- most of the behaviour is defined in functions rather than classes
- crawling is done concurrently

## execution

This is a gradle project so usual tasks (`test`, `run`, ...) apply.
The following two arguments are needed for an execution:
- startUrl
- number of threads

Example execution:
```
./gradlew run --args "http://wiprodigital.com 15"
```

See also the [Main](src/main/java/org/webcrawly/Main.java) class.

### timeout
there is currently a (generous) timeout (2m) hardcoded in the main class.

## further todos
- increase test coverage
- add logging
- some invalid urls could mbe fixed via url encoding, they currently fail
- not all possible httml links are processed
    - for example `<link>` and `<base>` are ignored
    - java script sources are tagged as media
- output file rather than the console