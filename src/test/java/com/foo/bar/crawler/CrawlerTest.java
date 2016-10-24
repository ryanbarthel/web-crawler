package com.foo.bar.crawler;

import com.foo.bar.crawler.model.Internet;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.testng.Assert.assertTrue;


/**
 * Created by rbarthel on 10/24/16.
 */

public class CrawlerTest {
    private static final String TEST_ONE = "src//test//resources//sample1.json";
    private static final String TEST_TWO = "src//test//resources//sample2.json";
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    private Crawler.CrawlerResults getSingleCrawlerResults(String filePath) throws Exception {
        Internet internet = WebCrawler.MAPPER.readValue(new File(filePath), Internet.class);
        Crawler crawler = new Crawler(internet ,1);
        Crawler.CrawlerResults results = crawler.call();
        return results;
    }

    private List<Crawler> createCrawlerList(int numOfCrawlers) throws IOException {
        Internet one = WebCrawler.MAPPER.readValue(new File(TEST_ONE), Internet.class);
        Internet two = WebCrawler.MAPPER.readValue(new File(TEST_TWO), Internet.class);
        List<Crawler> crawlers = new ArrayList<>();
        for (int i = 0; i <= numOfCrawlers; i ++) {
            if (i % 2 == 0) crawlers.add(new Crawler(two, 2));
            else crawlers.add(new Crawler(one, 1));
        }
        return crawlers;
    }



    @Test
    public void testSample1() throws Exception {
        Crawler.CrawlerResults results = getSingleCrawlerResults(TEST_ONE);
        sampleOneResult(results);
    }

    @Test
    public void testSample2() throws Exception {
        Crawler.CrawlerResults results = getSingleCrawlerResults(TEST_TWO);
        sampletwoResult(results);
    }

    @Test
    public void testManyCrawls() throws IOException, InterruptedException {
        List<Crawler> list = createCrawlerList(20);
        List<Future<Crawler.CrawlerResults>> results = executorService.invokeAll(list);
        results.forEach(f -> {
            try {
                Crawler.CrawlerResults result = f.get();
                if (result.getCrawlerId() %2 == 0) {
                    sampletwoResult(result);
                } else {
                    sampleOneResult(result);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();


    }

    private void sampleOneResult(Crawler.CrawlerResults results) {
        assertTrue(results.getVisitedSet().containsAll(Arrays.asList(
                "http://foo.bar.com/p4", "http://foo.bar.com/p6", "http://foo.bar.com/p2", "http://foo.bar.com/p1", "http://foo.bar.com/p5" )));
        assertTrue(results.getDuplicateSet().containsAll(Arrays.asList(
                "http://foo.bar.com/p4", "http://foo.bar.com/p2", "http://foo.bar.com/p1", "http://foo.bar.com/p5" )));
        assertTrue(results.getInvalidSet().containsAll(Arrays.asList(
                "http://foo.bar.com/p3", "http://foo.bar.com/p7")));

    }

    private void sampletwoResult(Crawler.CrawlerResults results) {
        assertTrue(results.getVisitedSet().containsAll(Arrays.asList(
                "http://foo.bar.com/p4", "http://foo.bar.com/p3", "http://foo.bar.com/p2", "http://foo.bar.com/p1", "http://foo.bar.com/p5" )));
        assertTrue(results.getDuplicateSet().containsAll(Arrays.asList(
                "http://foo.bar.com/p1")));
        assertTrue(results.getInvalidSet().isEmpty());
    }


}
