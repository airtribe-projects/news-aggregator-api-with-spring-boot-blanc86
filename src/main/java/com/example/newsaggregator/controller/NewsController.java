package com.example.newsaggregator.controller;

import com.example.newsaggregator.dto.ApiResponse;
import com.example.newsaggregator.dto.NewsArticle;
import com.example.newsaggregator.dto.PreferencesRequest;
import com.example.newsaggregator.service.NewsService;
import com.example.newsaggregator.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class NewsController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @GetMapping("/preferences")
    public ResponseEntity<?> getUserPreferences() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Set<String> preferences = userService.getUserPreferences(username);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Error retrieving preferences: " + e.getMessage(), false));
        }
    }

    @PutMapping("/preferences")
    public ResponseEntity<?> updateUserPreferences(@Valid @RequestBody PreferencesRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            userService.updatePreferences(username, request.getCategories());
            return ResponseEntity.ok(new ApiResponse("Preferences updated successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Error updating preferences: " + e.getMessage(), false));
        }
    }

    @GetMapping("/news")
    public ResponseEntity<?> getNews() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Set<String> preferences = userService.getUserPreferences(username);

            if (preferences.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse("No preferences set. Please set your news preferences first.", false));
            }

            List<NewsArticle> articles = newsService.getNewsByCategories(preferences);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Error fetching news: " + e.getMessage(), false));
        }
    }
}