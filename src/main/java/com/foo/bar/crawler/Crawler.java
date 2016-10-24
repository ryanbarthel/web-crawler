package com.foo.bar.crawler;

import com.foo.bar.crawler.model.Internet;
import com.foo.bar.crawler.model.Page;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by rbarthel on 10/24/16.
 */
public class Crawler implements Callable<Crawler.CrawlerResults> {
    private Map<String, Page> visitedMap = new HashMap<>();
    private Map<String, Page> duplicateMap = new HashMap<>();
    private Set<String> invalidSet = new HashSet<>();
    private Internet internet;
    private final Map<String, Page> addresses = new HashMap<>();
    private int id;

    public Crawler(Internet internet, int id) {
        this.internet = internet;
        this.id = id;
        internet.getPages().forEach(p -> addresses.put(p.getAddress(), p));
    }

    @Override
    public CrawlerResults call() throws Exception {
        visitPage(internet.getPages().get(0));
        return publishResults();
    }
    private void visitPage(Page page) {
        if (visitedMap.putIfAbsent(page.getAddress(), page) != null) {
            registerDuplicateVisit(page);
        } else {
            for (String link  : page.getLinks()) {
                if (addresses.get(link) != null) {
                    visitPage(addresses.get(link));
                } else {
                    registerBadLink(link);
                }
            }
        }
    }

    private void registerDuplicateVisit(Page page) {
        duplicateMap.put(page.getAddress(), page);
    }

    private void registerBadLink(String link) {
        invalidSet.add(link);
    }

    private CrawlerResults publishResults() {
        System.out.println("Success: [ " + String.join(", ", visitedMap.keySet()) + " ]"  );
        System.out.println("Skipped: [ " + String.join(", ", duplicateMap.keySet()) + " ]"  );
        System.out.println("Error: [ " + String.join(", ", invalidSet) + " ]"  );
        return new CrawlerResults(visitedMap.keySet(), duplicateMap.keySet(), invalidSet, this.id);
    }



    public static class CrawlerResults {


        private Set<String> visitedSet = new LinkedHashSet<>();
        private Set<String> duplicateSet = new LinkedHashSet<>();
        private Set<String> invalidSet = new HashSet<>();
        private int crawlerId;

        public CrawlerResults(Set<String> visitedSet, Set<String> duplicateSet, Set<String> invalidSet,int crawlerId) {
            this.visitedSet = visitedSet;
            this.duplicateSet = duplicateSet;
            this.invalidSet = invalidSet;
            this.crawlerId = crawlerId;
        }

        public Set<String> getVisitedSet() {
            return visitedSet;
        }

        public Set<String> getDuplicateSet() {
            return duplicateSet;
        }

        public Set<String> getInvalidSet() {
            return invalidSet;
        }

        public int getCrawlerId() {
            return crawlerId;
        }

    }
}
