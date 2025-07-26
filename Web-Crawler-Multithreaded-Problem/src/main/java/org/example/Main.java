package org.example;

public class Main {
    public static void main(String[] args) {
        String seedUrl = "https://example.com";
        int maxPages = 5;

        MultithreadedWebCrawler crawler = new MultithreadedWebCrawler(seedUrl, maxPages);
        crawler.start();
    }
}