package com.softserveinc.ita.homeproject.application.config.classes.destination;

public abstract class ContainerDto extends BaseModelDto {
    private InnerDto outerField;

    public InnerDto getOuterField() {
        return outerField;
    }

    public void setOuterField(InnerDto outerField) {
        this.outerField = outerField;
    }
}
