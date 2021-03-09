package com.softserveinc.ita.homeproject.api.tests.news;

import static com.softserveinc.ita.homeproject.api.tests.utils.QueryFilterUtils.createExceptionMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.util.ArrayList;
import java.util.List;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.NewsApi;
import com.softserveinc.ita.homeproject.api.tests.query.NewsQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.QueryFilterUtils;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class QueryNewsIT {
    private final CreateNews expectedNews = new CreateNews()
        .description("description")
        .source("source")
        .text("text")
        .photoUrl("photoUrl");

    private NewsApi newsApi = new NewsApi(ApiClientUtil.getClient());

    @Test
    void getAllNews() throws ApiException {
        List<ReadNews> expectedListNews = saveListNews();

        List<ReadNews> actualListNews = new NewsQuery
            .Builder(newsApi)
            .pageNumber(1)
            .pageSize(10)
            .build()
            .perfom();

        assertThat(actualListNews).isNotEmpty();
    }

    @Test
    void getAllNewsByTitleTest() throws ApiException {
        expectedNews.setTitle(RandomStringUtils.randomAlphabetic(7));
        newsApi.addNews(expectedNews);

        List<ReadNews> actualListNews = new NewsQuery
            .Builder(newsApi)
            .pageNumber(1)
            .pageSize(10)
            .title(expectedNews.getTitle())
            .build()
            .perfom();

        actualListNews.forEach(readNews -> assertThat(readNews.getTitle()).isEqualTo(expectedNews.getTitle()));
    }

    @Test
    void getAllNewsBySourceTest() throws ApiException {
        expectedNews.setTitle(RandomStringUtils.randomAlphabetic(7));
        newsApi.addNews(expectedNews);

        List<ReadNews> actualListNews = new NewsQuery
            .Builder(newsApi)
            .pageNumber(1)
            .pageSize(10)
            .source("source")
            .build()
            .perfom();

        actualListNews.forEach(readNews -> assertThat(readNews.getSource()).isEqualTo("source"));
    }

    @Test
    void getAllUsersLikeIgnoreCaseTest() throws ApiException {
        saveListNews();

        List<ReadNews> actualListNews = new NewsQuery
            .Builder(newsApi)
            .pageNumber(1)
            .pageSize(10)
            .filter("source=ilike='Si'")
            .build()
            .perfom();

        actualListNews.forEach(readNews -> assertThat(readNews.getSource().toLowerCase()).contains("Si".toLowerCase()));
    }

    @Test
    void getAllNewsDescSortBySourceTest() throws ApiException {
        saveListNews();

        List<ReadNews> actualListNews = new NewsQuery
            .Builder(newsApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("source,desc")
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

        List<ReadNews> actualListNews = new NewsQuery
            .Builder(newsApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("source,asc")
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
        String selector = "id";
        String arg1 = String.valueOf(expectedList.get(1).getId());
        String arg2 = String.valueOf(expectedList.get(expectedList.size() - 1).getId());
        String filter = QueryFilterUtils.getBetweenPredicate(selector, arg1, arg2);

        List<ReadNews> actualListNews = new NewsQuery
            .Builder(newsApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("source,asc")
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
    void emptyArgumentTest() {
        ApiException exception =
            new ApiException(400, "The query argument for search is empty");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> new NewsQuery
                .Builder(newsApi)
                .pageNumber(1)
                .pageSize(10)
                .text(" ")
                .build()
                .perfom())
            .withMessage(createExceptionMessage(exception));
    }

    @Test
    void wrongFilterPredicateExceptionTest() {
        ApiException exception =
            new ApiException(400, "Unknown operator: =ilik=");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> new NewsQuery
                .Builder(newsApi)
                .pageNumber(1)
                .pageSize(10)
                .filter("source=ilik='Si'")
                .build()
                .perfom())
            .withMessage(createExceptionMessage(exception));
    }

    @Test
    void wrongFilterFieldExceptionTest() {
        ApiException exception =
            new ApiException(400, "Unknown property: sourc from entity");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> new NewsQuery
                .Builder(newsApi)
                .pageNumber(1)
                .pageSize(10)
                .filter("sourc=ilike='Si'")
                .build()
                .perfom())
            .withMessage(createExceptionMessage(exception));
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
