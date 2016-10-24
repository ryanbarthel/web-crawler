package com.foo.bar.crawler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rbarthel on 10/24/16.
 */
public class Internet {


    @JsonProperty
    private List<Page> pages = new ArrayList<>();

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Internet{" +
                "pages=" + pages +
                '}';
    }
}
