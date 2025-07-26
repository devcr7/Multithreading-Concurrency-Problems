package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultithreadedWebCrawler {
    private final Queue<String> urlQueue;
    private final Set<String> visitedUrls;
    private final Set<String> queuedUrls; // Track URLs that have been queued
    private final ExecutorService executor;
    private final int maxPages;

    public MultithreadedWebCrawler(String seedUrl, int maxPages) {
        this.urlQueue = new ConcurrentLinkedQueue<>();
        this.visitedUrls = ConcurrentHashMap.newKeySet();
        this.queuedUrls = ConcurrentHashMap.newKeySet(); // Thread-safe set for queued URLs
        this.executor = Executors.newFixedThreadPool(5); // Adjust the number of threads as needed
        this.maxPages = maxPages;

        urlQueue.add(seedUrl);
        queuedUrls.add(seedUrl); // Mark seed URL as queued
    }

    public void start() {
        try {
            while (visitedUrls.size() < maxPages) {
                String url = urlQueue.poll();
                if (url != null && visitedUrls.add(url)) {
                    executor.submit(() -> crawlPage(url));
                }
            }
        } finally {
            shutdown();
        }

        System.out.println("Crawling completed. Total pages visited: " + visitedUrls.size());
    }

    private void crawlPage(String url) {
        try {
            String html = downloadPage(url);
            List<String> links = extractLinks(html);

            for (String link : links) {
                if (visitedUrls.size() >= maxPages) {
                    break;
                }

                // Atomically check and add to queuedUrls to prevent duplicates
                if (queuedUrls.add(link)) {
                    urlQueue.add(link);
                }
            }
            System.out.println("Crawled: " + url + " | Found links: " + links.size());
        } catch (Exception e) {
            System.err.println("Error crawling " + url + ": " + e.getMessage());
        }
    }

    private String downloadPage(String url) throws IOException {
        StringBuilder content = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } finally {
            conn.disconnect();
        }
        return content.toString();
    }

    private List<String> extractLinks(String html) {
        Pattern pattern = Pattern.compile("href=[\"'](http[s]?://[^\"'#\\s]+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);

        Set<String> links = new HashSet<>();
        while (matcher.find()) {
            links.add(matcher.group(1));
        }
        return List.copyOf(links);
    }

    private void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}