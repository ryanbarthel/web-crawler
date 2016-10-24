# web-crawler

Uses Jackson to deserialize Json test data into objects for consumption. Jackson annotated classes are in com.foo.bar.crawler.model.

TestNG was used in conjunction with a main() method to test.  The crawler is designed as an immutable Callable object that recurses over
the 'internet' represented by the test data.  Because of Crawler's immutability and data hiding Crawlers can be created and executed in 
parallel, example CrawlerTest.testManyCrawls(); 
