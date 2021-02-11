package com.softserveinc.ita.homeproject.api.tests.NewsApiTests;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.NewsApi;
import com.softserveinc.ita.homeproject.api.tests.query.NewsQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.QueryFilterUtil;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class QueryNewsIT {
    private NewsApi newsApi;
    private Integer pageNumber = 1;
    private Integer pageSize = 10;

    private final CreateNews expectedNews = new CreateNews()
            .description("description")
            .source("source")
            .text("text")
            .photoUrl("photoUrl");

    @BeforeEach
    public void setUp() {
        newsApi = new NewsApi(ApiClientUtil.getClient());
        expectedNews.setTitle(RandomStringUtils.randomAlphabetic(7));
    }

    @Test
    void getAllNewsByTitleTest() throws ApiException {
        newsApi.addNews(expectedNews);

        List<ReadNews> actualListNews = new NewsQuery
                .Builder(pageNumber, pageSize, newsApi)
                .title(expectedNews.getTitle())
                .build()
                .perfom();

        actualListNews.forEach(readNews -> assertThat(readNews.getTitle()).isEqualTo(expectedNews.getTitle()));
    }

    @Test
    void getAllNewsBySourceTest() throws ApiException {
        newsApi.addNews(expectedNews);

        List<ReadNews> actualListNews = new NewsQuery
                .Builder(pageNumber, pageSize, newsApi)
                .source(expectedNews.getSource())
                .build()
                .perfom();

        actualListNews.forEach(readNews -> assertThat(readNews.getSource()).isEqualTo(expectedNews.getSource()));
    }

    @Test
    void getAllUsersLikeIgnoreCaseTest() throws ApiException {
        saveListNews();
        String filter = "source=ilike='Si'";

        List<ReadNews> actualListNews = new NewsQuery
                .Builder(pageNumber, pageSize, newsApi)
                .filter(filter)
                .build()
                .perfom();

        actualListNews.forEach(readNews -> assertThat(readNews.getSource().toLowerCase()).contains("Si".toLowerCase()));
    }

    @Test
    void getAllUsersDescSortBySourceTest() throws ApiException {
        String sort = "source,desc";

        List<ReadNews> actualListNews = new NewsQuery
                .Builder(pageNumber, pageSize, newsApi)
                .sort(sort)
                .build()
                .perfom();

        assertThat(actualListNews).isNotEmpty().isSortedAccordingTo((r1, r2) -> {
            if (r2.getSource() == null) {
                return -1;
            }
            if (r1.getSource() == null) {
                return 1;
            }
            return r2.getSource().compareTo(r1.getSource());
        });
    }

    @Test
    void getAllNewsAscSortBySourceTest() throws ApiException {
        saveListNews();
        String sort = "source,asc";

        List<ReadNews> actualListNews = new NewsQuery
                .Builder(pageNumber, pageSize, newsApi)
                .sort(sort)
                .build()
                .perfom();

        assertThat(actualListNews).isNotEmpty().isSortedAccordingTo((r1, r2) -> {
            if (r1.getSource() == null) {
                return -1;
            }
            if (r2.getSource() == null) {
                return 1;
            }
            return r1.getSource().compareTo(r2.getSource());
        });
    }

    @Test
    void getAllUsersSortAndSearchTest() throws ApiException {
        List<ReadNews> expectedList = saveListNews();
        String sort = "source,asc";
        String selector = "id";
        String arg1 = String.valueOf(expectedList.get(1).getId());
        String arg2 = String.valueOf(expectedList.get(expectedList.size() - 1).getId());
        String filter = QueryFilterUtil.between(selector, arg1, arg2);

        List<ReadNews> actualListNews = new NewsQuery
                .Builder(pageNumber, pageSize, newsApi)
                .sort(sort)
                .filter(filter)
                .build()
                .perfom();

        assertThat(actualListNews).isNotEmpty().isSortedAccordingTo((r1, r2) -> {
            if (r1.getSource() == null) {
                return -1;
            }
            if (r2.getSource() == null) {
                return 1;
            }
            return r1.getSource().compareTo(r2.getSource());
        }).matches(id -> actualListNews.get(0).getId()
                .equals(expectedList.get(1).getId())
                && actualListNews.get(actualListNews.size() - 1).getId()
                .equals(expectedList.get(expectedList.size() - 1).getId()));


    }

    @Test
    void wrongArgumentTest() throws ApiException {
        newsApi.addNews(expectedNews);
        String text = " ";
        ApiException exception =
                new ApiException(400, "Illegal argument in select query method in api implementation");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> new NewsQuery
                        .Builder(pageNumber, pageSize, newsApi)
                        .text(text)
                        .build()
                        .perfom())
                .withMessage(new StringBuilder()
                        .append("{\"responseCode\":")
                        .append(exception.getCode())
                        .append(",\"errorMessage\":\"")
                        .append(exception.getMessage())
                        .append("\"}").toString());
    }

    private List<CreateNews> createNewsList() {
        List<CreateNews> list = new ArrayList<>();
        list.add(new CreateNews()
                .title("title1")
                .description("description")
                .source("alex")
                .text(RandomStringUtils.randomAlphabetic(5))
                .photoUrl("photoUrl1"));

        list.add(new CreateNews()
                .title("title2")
                .description("description")
                .source("bob")
                .text(RandomStringUtils.randomAlphabetic(6))
                .photoUrl("photoUrl2"));

        list.add(new CreateNews()
                .title("title3")
                .description("description")
                .source("jack")
                .text(RandomStringUtils.randomAlphabetic(7))
                .photoUrl("photoUrl3"));

        list.add(new CreateNews()
                .title("title4")
                .description("description")
                .source("sindy")
                .text(RandomStringUtils.randomAlphabetic(9))
                .photoUrl("photoUrl4"));

        list.add(new CreateNews()
                .title("title5")
                .description("description")
                .source("victor")
                .text(RandomStringUtils.randomAlphabetic(5))
                .photoUrl("photoUrl5"));

        return list;
    }

    private List<ReadNews> saveListNews() throws ApiException {
        List<CreateNews> expectedList = createNewsList();
        List<ReadNews> actualList = new ArrayList<>();
        for (CreateNews cn : expectedList) {
            actualList.add(newsApi.addNews(cn));
        }
        return actualList;
    }
}
