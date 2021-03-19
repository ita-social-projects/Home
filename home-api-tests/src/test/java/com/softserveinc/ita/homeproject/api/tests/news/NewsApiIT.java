package com.softserveinc.ita.homeproject.api.tests.news;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.NewsApi;
import com.softserveinc.ita.homeproject.api.tests.query.NewsQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import org.junit.jupiter.api.Test;

class NewsApiIT {

    private final NewsApi newsApi = new NewsApi(ApiClientUtil.getClient());

    @Test
    void createNewsTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ApiResponse<ReadNews> response = newsApi.createNewsWithHttpInfo(expectedNews);

        assertEquals(Response.Status.CREATED.getStatusCode(),
            response.getStatusCode());
        assertNews(expectedNews, response.getData());
    }

    @Test
    void getNewsByIdTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ApiResponse<ReadNews> response = newsApi.getNewsWithHttpInfo(newsApi.createNews(expectedNews).getId());


        assertEquals(Response.Status.OK.getStatusCode(),
            response.getStatusCode());
        assertNews(expectedNews, response.getData());
    }

    @Test
    void updateNewsTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title("UpdatedTitle")
            .description("UpdatedDescription")
            .text("UpdatedText")
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        ApiResponse<ReadNews> response = newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews);

        assertEquals(Response.Status.OK.getStatusCode(),
            response.getStatusCode());
        assertNews(savedNews, updateNews, response.getData());
    }

    @Test
    void deleteNewsTest() throws ApiException {
        ReadNews expectedNews = newsApi.createNews(createNews().title("FirstTitle"));
        newsApi.createNews(createNews().title("SecondTitle"));
        newsApi.createNews(createNews().title("ThirdTitle"));

        ApiResponse<Void> removeResponse = newsApi.deleteNewsWithHttpInfo(expectedNews.getId());

        List<ReadNews> actualNewsList = new NewsQuery
            .Builder(newsApi)
            .pageNumber(1)
            .pageSize(10)
            .build().perfom();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), removeResponse.getStatusCode());
        assertFalse(actualNewsList.contains(expectedNews));
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.getNews(expectedNews.getId()));
    }

    private CreateNews createNews(){
        return new CreateNews()
            .title("Title")
            .description("Description")
            .text("Text")
            .photoUrl("http://someurl.example.com")
            .source("Source");
    }

    @Test
    void createNewsInvalidTitleTest() {
        CreateNews createNewsInvalidTitle = new CreateNews()
                .title("title over 70 symbols - title over 70 symbols - title over 70 symbols - title over 70 symbols"
                    + " - title over 70 symbols")
                .description("description")
                .source("source")
                .text("Some text")
                .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> newsApi.createNews(createNewsInvalidTitle))
                .withMessage("{\"responseCode\":400,\"errorMessage\":\"Parameter `title` is invalid"
                    + " - size must be between 1 and 70 signs." + "\"}");
    }

    @Test
    void createNewsInvalidDescriptionTest() {
        CreateNews createNewsInvalidDescription = new CreateNews()
            .title("title")
            .description("description description description description description description description"
                + " description description description description description description description"
                + " description description description description description description description")
            .source("source")
            .text("Some text")
            .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.createNews(createNewsInvalidDescription))
            .withMessage("{\"responseCode\":400,\"errorMessage\":\"Parameter `description` is invalid"
                + " - size must be between 1 and 150 signs." + "\"}");
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
