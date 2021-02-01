package com.softserveinc.ita.homeproject.api.tests;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.NewsApi;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import cz.jirutka.rsql.parser.RSQLParserException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RSQLNewsIT {
    private NewsApi newsApi;
    private List<ReadNews> newsList = new ArrayList<>();
    private Integer pageNumber;
    private Integer pageSize;
    private String sort;
    private String search;
    private String id;
    private String title;
    private String text;
    private String source;

    @BeforeEach
    public void setUp() throws ApiException {
        newsApi = new NewsApi(ApiClientUtil.getClient());
        initParameters();
        createManyNews();

    }


    @Test
    void getAllNewsByTitleTest() throws ApiException {
        title = newsList.get((int) (Math.random() * newsList.size())).getTitle();
        List<ReadNews> listNews = newsApi
                .getAllNews(pageNumber,
                        pageSize,
                        sort,
                        search,
                        id,
                        title,
                        text,
                        source);

        assertFalse(listNews.isEmpty());
        listNews.forEach(ReadNews -> assertEquals(ReadNews.getTitle(), title));
    }

    @Test
    void getAllNewsBySourceTest() throws ApiException {
        source = newsList.get((int) (Math.random() * newsList.size())).getSource();
        List<ReadNews> listNews = newsApi
                .getAllNews(pageNumber,
                        pageSize,
                        sort,
                        search,
                        id,
                        title,
                        text,
                        source);

        assertFalse(listNews.isEmpty());
        listNews.forEach(ReadNews -> assertEquals(ReadNews.getSource(), source));
    }

    @Test
    void getAllUsersLikeIgnoreCaseTest() throws ApiException {
        search = "source=ilike='Si'";
        List<ReadNews> listNews = newsApi
                .getAllNews(pageNumber,
                        pageSize,
                        sort,
                        search,
                        id,
                        title,
                        text,
                        source);

        assertFalse(listNews.isEmpty());
    }

    @Test
    void getAllUsersDescSortBySourceTest() throws ApiException {
        sort = "source,desc";
        List<String> sourceList = new ArrayList<>();

        List<ReadNews> listNews = newsApi
                .getAllNews(pageNumber,
                        pageSize,
                        sort,
                        search,
                        id,
                        title,
                        text,
                        source);
        listNews.forEach(readUser -> sourceList.add(readUser.getSource()));
        String[] actualSource = new String[sourceList.size()];
        sourceList.toArray(actualSource);
        Collections.sort(sourceList, Collections.reverseOrder());
        String[] expectedSource = new String[sourceList.size()];
        sourceList.toArray(expectedSource);

        assertArrayEquals(expectedSource, actualSource);
    }

    @Test
    void getAllNewsAscSortBySourceTest() throws ApiException {
        sort = "source,asc";
        List<String> sourceList = new ArrayList<>();

        List<ReadNews> listNews = newsApi
                .getAllNews(pageNumber,
                        pageSize,
                        sort,
                        search,
                        id,
                        title,
                        text,
                        source);
        listNews.forEach(readUser -> sourceList.add(readUser.getSource()));
        String[] actualSource = new String[sourceList.size()];
        sourceList.toArray(actualSource);
        Collections.sort(sourceList);
        String[] expectedSource = new String[sourceList.size()];
        sourceList.toArray(expectedSource);

        assertArrayEquals(expectedSource, actualSource);
    }

    @Test
    void getAllUsersSortAndSearchTest() throws ApiException {
        List<String> sourceList = new ArrayList<>();
        sort = "source,asc";
        search = new StringBuilder()
                .append("id=bt=('")
                .append(newsList.get(1).getId())
                .append("',")
                .append("'")
                .append(newsList.get(newsList.size() - 1).getId())
                .append("')").toString();

        List<ReadNews> listNews = newsApi
                .getAllNews(pageNumber,
                        pageSize,
                        sort,
                        search,
                        id,
                        title,
                        text,
                        source);
        listNews.forEach(readUser -> sourceList.add(readUser.getSource()));
        String[] actualSource = new String[sourceList.size()];
        sourceList.toArray(actualSource);
        Collections.sort(sourceList);
        String[] expectedSource = new String[sourceList.size()];
        sourceList.toArray(expectedSource);

        assertEquals(listNews.size(), 4);
        assertArrayEquals(expectedSource, actualSource);
    }

    @Test
    void wrongArgumentTest() {
        text = " ";
        try {
            newsApi
                    .getAllNews(pageNumber,
                            pageSize,
                            sort,
                            search,
                            id,
                            title,
                            text,
                            source);
            fail("Expected RSQLParserException");
        } catch (RSQLParserException | ApiException thrown) {
            assertNotEquals("", thrown.getMessage());
        }
    }


    @AfterEach
    void tearDown() throws ApiException {
        for (ReadNews ru : newsList) {
            newsApi.deleteNews(ru.getId());

        }
    }

    private Optional<List<CreateNews>> createNewsList() {
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

        return Optional.of(list);
    }

    private void initParameters() {
        pageNumber = 1;
        pageSize = 10;
        sort = null;
        search = null;
        id = null;
        title = null;
        text = null;
        source = null;
    }

    private void createManyNews() throws ApiException {
        List<CreateNews> expectedList = createNewsList().orElseThrow();

        for (CreateNews cn : expectedList) {
            newsList.add(newsApi.addNews(cn));
        }

        assertEquals(newsList.size(), expectedList.size());
        for (int i = 0; i < newsList.size(); i++)
            assertNews(expectedList.get(i), newsList.get(i));
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
