package com.softserveinc.ita.homeproject.api.tests.news;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;

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

    @Test
    void createNewsWithNullTitleTest() {
        CreateNews createNewsInvalidTitle = new CreateNews()
            .title(null)
            .description("description")
            .source("source")
            .text("Some text")
            .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.createNews(createNewsInvalidTitle))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `title` is invalid - must not be null.");
    }

    @Test
    void createNewsWithEmptyTitleTest() {
        CreateNews createNewsInvalidTitle = new CreateNews()
            .title("")
            .description("description")
            .source("source")
            .text("Some text")
            .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.createNews(createNewsInvalidTitle))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `title` is invalid - size must be between 1 and 70 signs.");
    }

    @Test
    void createNewsWithInvalidTitleTest() {
        CreateNews createNewsInvalidTitle = new CreateNews()
                .title("title over 70 symbols - title over 70 symbols - title over 70 symbols - title over 70 symbols"
                    + " - title over 70 symbols")
                .description("description")
                .source("source")
                .text("Some text")
                .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> newsApi.createNews(createNewsInvalidTitle))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `title` is invalid - size must be between 1 and 70 signs.");
    }

    @Test
    void createNewsWithNullDescriptionTest() {
        CreateNews createNewsInvalidDescription = new CreateNews()
            .title("title")
            .description(null)
            .source("source")
            .text("Some text")
            .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.createNews(createNewsInvalidDescription))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `description` is invalid - must not be null.");
    }

    @Test
    void createNewsWithEmptyDescriptionTest() {
        CreateNews createNewsInvalidDescription = new CreateNews()
            .title("title")
            .description("")
            .source("source")
            .text("Some text")
            .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.createNews(createNewsInvalidDescription))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `description` is invalid - size must be between 1 and 150 signs.");
    }

    @Test
    void createNewsWithInvalidDescriptionTest() {
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
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `description` is invalid - size must be between 1 and 150 signs.");
    }

    @Test
    void createNewsWithNullTextTest() {
        CreateNews createNewsInvalidDescription = new CreateNews()
            .title("title")
            .description("Some description")
            .source("source")
            .text(null)
            .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.createNews(createNewsInvalidDescription))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `text` is invalid - must not be null.");
    }

    @Test
    void createNewsWithEmptyTextTest() {
        CreateNews createNewsInvalidDescription = new CreateNews()
            .title("title")
            .description("Some description")
            .source("source")
            .text("")
            .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.createNews(createNewsInvalidDescription))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `text` is invalid - size must be between 1 and 150 signs.");
    }

    @Test
    void createNewsWithInvalidTextTest() {
        CreateNews createNewsInvalidDescription = new CreateNews()
            .title("title")
            .description("Description")
            .source("source")
            .text("I will not illegally download any movie" +
                " I will not illegally download any movie" +
                " I will not illegally download any movie" +
                " I will not illegally download any movie" +
                " I will not illegally download any movie")
            .photoUrl("222222");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.createNews(createNewsInvalidDescription))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `text` is invalid - size must be between 1 and 150 signs.");
    }

    @Test
    void getNonExistentNewsTest() {
        Long wrongId = 100L;

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.getNewsWithHttpInfo(wrongId))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("News with 'id: " + wrongId + "' is not found");
    }

    @Test
    void passNullWhenReceivingNewsTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.getNewsWithHttpInfo(null))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Missing the required parameter 'id' when calling getNews");
    }

    @Test
    void updateNewsWithNullTitleTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title(null)
            .description("UpdatedDescription")
            .text("UpdatedText")
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `title` is invalid - must not be null.");
    }

    @Test
    void updateNewsWithEmptyTitleTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title("")
            .description("UpdatedDescription")
            .text("UpdatedText")
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `title` is invalid - size must be between 1 and 70 signs.");
    }

    @Test
    void updateNewsWithInvalidTitleTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title("title over 70 symbols - title over 70 symbols - title over 70 symbols - title over 70 symbols" +
                " - title over 70 symbols")
            .description("UpdatedDescription")
            .text("UpdatedText")
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `title` is invalid - size must be between 1 and 70 signs.");
    }

    @Test
    void updateNewsWithNullDescriptionTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title("Title Example")
            .description(null)
            .text("UpdatedText")
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `description` is invalid - must not be null.");
    }

    @Test
    void updateNewsWithEmptyDescriptionTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title("Title Example")
            .description("")
            .text("UpdatedText")
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `description` is invalid - size must be between 1 and 150 signs.");
    }

    @Test
    void updateNewsWithInvalidDescriptionTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title("Title Example")
            .description("description description description description description description description" +
                " description description description description description description description" +
                " description description description description description description description")
            .text("UpdatedText")
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `description` is invalid - size must be between 1 and 150 signs.");
    }

    @Test
    void updateNewsWithNullTextTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title("Title Example")
            .description("Description")
            .text(null)
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `text` is invalid - must not be null.");
    }

    @Test
    void updateNewsWithEmptyTextTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title("Title Example")
            .description("Description")
            .text("")
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `text` is invalid - size must be between 1 and 150 signs.");
    }

    @Test
    void updateNewsWithInvalidTextTest() throws ApiException {
        CreateNews expectedNews = createNews();

        ReadNews savedNews = newsApi.createNews(expectedNews);

        UpdateNews updateNews = new UpdateNews()
            .title("Title Example")
            .description("Description")
            .text("I will not illegally download any movie" +
                " I will not illegally download any movie" +
                " I will not illegally download any movie" +
                " I will not illegally download any movie" +
                " I will not illegally download any movie")
            .photoUrl("http://updatedurl.example.com")
            .source("UpdatedSource");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.updateNewsWithHttpInfo(savedNews.getId(), updateNews))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `text` is invalid - size must be between 1 and 150 signs.");
    }

    @Test
    void deleteNonExistentNewsTest() {
        Long wrongId = 100L;

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.deleteNewsWithHttpInfo(wrongId))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Can't find news with given ID: " + wrongId);
    }

    @Test
    void passNullWhenDeleteNewsTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> newsApi.deleteNewsWithHttpInfo(null))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Missing the required parameter 'id' when calling deleteNews");
    }

    private CreateNews createNews(){
        return new CreateNews()
            .title("Title")
            .description("Description")
            .text("Text")
            .photoUrl("http://someurl.example.com")
            .source("Source");
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
