package com.urise.webapp.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Organization implements Serializable {
    private Link homePage;

    private List<Period> posts;

    public Organization() {
    }

    public Organization(String name, String url, List<Period> posts) {
        this.homePage = new Link(name, url);
        this.posts = posts;
    }

    public Organization(Link homePage, List<Period> posts) {
        this.homePage = homePage;
        this.posts = posts;
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Period> getPosts() {
        return posts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return homePage.equals(that.homePage) && posts.equals(that.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, posts);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homePage=" + homePage +
                ", posts=" + posts +
                '}';
    }
}
