package com.foo.bar.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foo.bar.crawler.model.Internet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * main() method with multi-threaded invocation of Crawler.  Also see com.foo.bar.crawler.CrawlerTest for more
 * executions.
 * Created by rbarthel on 10/24/16.
 */
public class WebCrawler  {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    public static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String SAMPLE_ONE = "src//main//resources//sample1.json";
    private static final String SAMPLE_TWO = "src//main//resources//sample2.json";


    public static void main(String[] args) {
        Internet internet1 = null;
        Internet internet2 = null;
        try {
            internet1 = MAPPER.readValue(new File(SAMPLE_ONE), Internet.class);
            internet2 = MAPPER.readValue(new File(SAMPLE_TWO), Internet.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Crawler> crawlerList = new ArrayList<>();
        crawlerList.add(new Crawler(internet1, 1));
        crawlerList.add(new Crawler(internet2, 2));
        try {
            List<Future<Crawler.CrawlerResults>> results = executorService.invokeAll(crawlerList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }



}
