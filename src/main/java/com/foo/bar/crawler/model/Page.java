package com.foo.bar.crawler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rbarthel on 10/24/16.
 */

public class Page {

    @JsonProperty
    private String address;

    @JsonProperty
    private List<String> links;



    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public List<String> getLinks() {
        return links;
    }


    public void setLinks(List<String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "Page{" +
                "address='" + address + '\'' +
                ", links=" + links +
                '}';
    }
}
