package com.example.newsaggregator.service;

import com.example.newsaggregator.dto.NewsArticle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class NewsService {

    @Value("${news.api.key}")
    private String apiKey;

    @Value("${news.api.url}")
    private String apiUrl;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public NewsService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public List<NewsArticle> getNewsByCategories(Set<String> categories) {
        List<NewsArticle> allArticles = new ArrayList<>();

        for (String category : categories) {
            try {
                List<NewsArticle> articles = fetchNewsForCategory(category);
                allArticles.addAll(articles);
            } catch (Exception e) {
                System.err.println("Error fetching news for category " + category + ": " + e.getMessage());
            }
        }

        return allArticles;
    }

    private List<NewsArticle> fetchNewsForCategory(String category) {
        String url = apiUrl + "/top-headlines?category=" + category + "&apiKey=" + apiKey + "&pageSize=5";

        try {
            Mono<String> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class);

            String jsonResponse = response.block();
            return parseNewsResponse(jsonResponse);
        } catch (Exception e) {
            System.err.println("Error calling news API: " + e.getMessage());
            return getDefaultNews(category);
        }
    }

    private List<NewsArticle> parseNewsResponse(String jsonResponse) {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode articlesNode = root.path("articles");

            for (JsonNode articleNode : articlesNode) {
                NewsArticle article = new NewsArticle();
                article.setTitle(articleNode.path("title").asText());
                article.setDescription(articleNode.path("description").asText());
                article.setUrl(articleNode.path("url").asText());
                article.setUrlToImage(articleNode.path("urlToImage").asText());
                article.setPublishedAt(articleNode.path("publishedAt").asText());
                article.setSource(articleNode.path("source").path("name").asText());

                articles.add(article);
            }
        } catch (Exception e) {
            System.err.println("Error parsing news response: " + e.getMessage());
        }

        return articles;
    }

    private List<NewsArticle> getDefaultNews(String category) {
        List<NewsArticle> articles = new ArrayList<>();

        articles.add(new NewsArticle(
                "Sample " + category + " News",
                "This is a sample news article for " + category + " category",
                "https://example.com",
                "https://example.com/image.jpg",
                "2025-01-01T00:00:00Z",
                "Sample News"
        ));

        return articles;
    }
}
