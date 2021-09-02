package com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.source;

public abstract class Inner extends BaseModel {
    private String onParent;

    public void setOnParent(String onParent) {
        this.onParent = onParent;
    }

    public String getOnParent() {
        return onParent;
    }
}
