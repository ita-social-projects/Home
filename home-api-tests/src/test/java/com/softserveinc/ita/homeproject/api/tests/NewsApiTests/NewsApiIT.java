package com.softserveinc.ita.homeproject.api.tests.NewsApiTests;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.NewsApi;
import com.softserveinc.ita.homeproject.api.tests.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class NewsApiIT {

    private NewsApi newsApi;

    private final CreateNews createNews = new CreateNews()
            .title("title")
            .description("description")
            .source("source")
            .text("text")
            .photoUrl("photoUrl");

    @BeforeEach
    public void setUp() {
        newsApi = new NewsApi(ApiClientUtil.getClient());
    }

    @Test
    void createNewsTest() throws ApiException {
        ReadNews readNews = newsApi.addNews(createNews);
        assertNews(createNews, readNews);
    }

    @Test
    void getNewsByIdTest() throws ApiException {
        ReadNews savedNews = newsApi.addNews(createNews);
        ReadNews news = newsApi.getNews(savedNews.getId());

        assertNotNull(news);
        assertEquals(savedNews, news);
    }

    @Test
    void updateNewsTest() throws ApiException {
        ReadNews savedNews = newsApi.addNews(createNews);

        UpdateNews updateNews = new UpdateNews()
                .title("updatedTitle")
                .description("updatedDescription")
                .source("updatedSource")
                .text("updatedText")
                .photoUrl("updatedPhotoUrl");

        ReadNews updatedNews = newsApi.updateNews(savedNews.getId(), updateNews);
        assertNews(savedNews, updateNews, updatedNews);
    }

    @Test
    void deleteNewsTest() throws ApiException {
        ReadNews savedNews = newsApi.addNews(createNews);
        assertNotNull(newsApi.getNews(savedNews.getId()));

        newsApi.deleteNews(savedNews.getId());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> newsApi.getNews(savedNews.getId()))
                .withMessage("{\"responseCode\":404,\"errorMessage\":\"Can't find news with given ID:" + savedNews.getId() + "\"}");
    }

    private void assertNews(ReadNews saved, UpdateNews update, ReadNews updated) {
        assertNotNull(updated);
        assertNotEquals(saved, updated);
        assertEquals(update.getTitle(), updated.getTitle());
        assertEquals(update.getDescription(), updated.getDescription());
        assertEquals(update.getSource(), updated.getSource());
        assertEquals(update.getText(), updated.getText());
        assertEquals(update.getPhotoUrl(), updated.getPhotoUrl());
    }

    private void assertNews(CreateNews expected, ReadNews actual) {
        assertNotNull(actual);
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getSource(), actual.getSource());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getPhotoUrl(), actual.getPhotoUrl());
    }
}
