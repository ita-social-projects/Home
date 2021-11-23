package com.softserveinc.ita.homeproject.homeservice.mapper.config.classes.source;

public abstract class Container extends BaseModel {

    private Inner outerField;

    public void setOuterField(Inner outerField) {
        this.outerField = outerField;
    }

    public Inner getOuterField() {
        return outerField;
    }
}
