package com.softserveinc.ita.homeproject.homeservice.mapper.config.classes.destination;

import java.util.List;

public class Inner1Dto extends InnerDto {

    private List<InnerItemDto> innerItemList;

    private String onChild1;

    public String getOnChild1() {
        return onChild1;
    }

    public void setOnChild1(String onChild1) {
        this.onChild1 = onChild1;
    }

    public List<InnerItemDto> getInnerItemList() {
        return innerItemList;
    }

    public void setInnerItemList(List<InnerItemDto> innerItemList) {
        this.innerItemList = innerItemList;
    }
}
