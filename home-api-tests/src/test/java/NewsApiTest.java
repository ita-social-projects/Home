import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.softserveinc.ita.homeproject.ApiClient;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ServerConfiguration;
import com.softserveinc.ita.homeproject.api.NewsApi;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NewsApiTest {

    private final NewsApi newsApi;

    {
        ApiClient client = new ApiClient();
        client.setUsername("admin@example.com");
        client.setPassword("password");
        client.setServers(List.of(new ServerConfiguration("http://localhost:8080/api/0",
                "No description provided", new HashMap())));
        newsApi = new NewsApi(client);
    }

    CreateNews createNews = new CreateNews()
            .title("title")
            .description("description")
            .source("source")
            .text("text")
            .photoUrl("photoUrl");

    @Test
    public void createNewsTest() throws ApiException {
        ReadNews readNews = newsApi.addNews(createNews);
        assertNews(createNews, readNews);
    }

    @Test
    public void getNewsByIdTest() throws ApiException {
        ReadNews savedNews = newsApi.addNews(createNews);

        ReadNews news = newsApi.getNews(savedNews.getId());
        assertNews(savedNews, news);
    }

    @Test
    public void updateNewsTest() throws ApiException {
        ReadNews savedNews = newsApi.addNews(createNews);

        UpdateNews updateNews = new UpdateNews()
                .title("updatedTitle")
                .description("updatedDescription")
                .source("updatedSource")
                .text("updatedText")
                .photoUrl("updatedPhotoUrl");

        ReadNews readNews = newsApi.updateNews(savedNews.getId(), updateNews);
        assertNews(updateNews, readNews);
    }

    @Test
    public void getAllNewsTest() throws ApiException {
        newsApi.addNews(createNews);
        List<ReadNews> allNews = newsApi.getAllNews(1, 10);

        assertNotNull(allNews);
        assertFalse(allNews.isEmpty());
    }

    @Test
    public void deleteNewsTest() throws ApiException {
        ReadNews savedNews = newsApi.addNews(createNews);

        newsApi.deleteNews(savedNews.getId());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> newsApi.getNews(savedNews.getId()))
                .withMessage("{\"responseCode\":404,\"errorMessage\":\"Can't find news with given ID:" + savedNews.getId() + "\"}");
    }


    private void assertNews(UpdateNews expected, ReadNews actual) {
        assertNotNull(actual);
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getSource(), actual.getSource());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getPhotoUrl(), actual.getPhotoUrl());
    }

    private void assertNews(CreateNews expected, ReadNews actual) {
        assertNotNull(actual);
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getSource(), actual.getSource());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getPhotoUrl(), actual.getPhotoUrl());
    }

    private void assertNews(ReadNews expected, ReadNews actual) {
        assertNotNull(actual);
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getSource(), actual.getSource());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getPhotoUrl(), actual.getPhotoUrl());
    }

}
