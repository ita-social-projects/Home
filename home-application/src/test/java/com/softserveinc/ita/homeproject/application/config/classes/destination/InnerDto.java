package com.softserveinc.ita.homeproject.application.config.classes.destination;

public abstract class InnerDto extends BaseModelDto {
    private String onParent;

    public void setOnParent(String onParent) {
        this.onParent = onParent;
    }

    public String getOnParent() {
        return onParent;
    }
}
