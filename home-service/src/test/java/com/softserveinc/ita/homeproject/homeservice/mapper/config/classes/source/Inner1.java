package com.softserveinc.ita.homeproject.homeservice.mapper.config.classes.source;

import java.util.List;

public class Inner1 extends Inner {

    private List<InnerItem> innerItemList;

    private String onChild1;

    public String getOnChild1() {
        return onChild1;
    }

    public void setOnChild1(String onChild1) {
        this.onChild1 = onChild1;
    }

    public List<InnerItem> getInnerItemList() {
        return innerItemList;
    }

    public void setInnerItemList(List<InnerItem> innerItemList) {
        this.innerItemList = innerItemList;
    }
}
