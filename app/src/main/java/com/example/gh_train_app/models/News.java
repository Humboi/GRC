package com.example.gh_train_app.models;

public class News {
    String newsId;
    String news_category;
    String news_title;
    String news_description;
    String news_date;
    String imageUrl;

    public News(){}

    public News(String newsId, String news_category, String news_title, String news_description, String news_date, String imageUrl) {
        this.newsId = newsId;
        this.news_category = news_category;
        this.news_title = news_title;
        this.news_description = news_description;
        this.news_date = news_date;
        this.imageUrl = imageUrl;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNews_category() {
        return news_category;
    }

    public void setNews_category(String news_category) {
        this.news_category = news_category;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
