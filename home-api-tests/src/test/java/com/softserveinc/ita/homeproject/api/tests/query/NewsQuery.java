package com.softserveinc.ita.homeproject.api.tests.query;

import java.util.List;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.NewsApi;
import com.softserveinc.ita.homeproject.model.ReadNews;

public class NewsQuery extends BaseQuery {
    private String title;

    private String text;

    private String source;

    private NewsApi newsApi;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setNewsApi(NewsApi newsApi) {
        this.newsApi = newsApi;
    }

    public List<ReadNews> perfom() throws ApiException {
        return newsApi
            .getAllNews(this.getPageNumber(),
                this.getPageSize(),
                this.getSort(),
                this.getFilter(),
                this.getId(),
                title,
                text,
                source);
    }

    public static class Builder extends BaseBuilder<NewsQuery, Builder> {

        public Builder(NewsApi newsApi) {
            queryClass.setNewsApi(newsApi);
        }

        public Builder title(String title) {
            queryClass.setTitle(title);
            return this;
        }

        public Builder text(String text) {
            queryClass.setText(text);
            return this;
        }

        public Builder source(String source) {
            queryClass.setSource(source);
            return this;
        }

        @Override
        protected NewsQuery getActual() {
            return new NewsQuery();
        }

        @Override
        protected NewsQuery.Builder getActualBuilder() {
            return this;
        }
    }
}
