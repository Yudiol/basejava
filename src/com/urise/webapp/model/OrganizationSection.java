package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class OrganizationSection extends Section {
    private List<Organization> organizations;

    public OrganizationSection() {
    }

    public OrganizationSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "organization must not be null");
        this.organizations = organizations;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }
}
