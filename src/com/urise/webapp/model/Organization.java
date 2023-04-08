package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class Organization {
    private final Link homePage;

    private final List<Post> posts;

    public Organization(String name, String url, List<Post> posts) {
        this.homePage = new Link(name, url);
        this.posts = posts;
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Post> getPosts() {
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
