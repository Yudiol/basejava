package com.urise.webapp;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import java.io.IOException;
import java.sql.SQLException;

public class MainString {
    public static void main(String[] args) throws IOException, SQLException {
        Storage storage = Config.get().getStorage();
        Resume resume = new ResumeData().createResume("UUID_1", "Kirill");
        storage.clear();
        storage.save(resume);
//        Resume r = storage.get("UUID_1");
//        System.out.println(r.getFullName());
//        System.out.println(r.getUuid());
//        r.getContacts().entrySet().forEach(System.out::println);
//        r.getSections().entrySet().forEach(System.out::println);
//        ((OrganizationSection) r.getSection(SectionType.EXPERIENCE)).getOrganizations()
//                .forEach(or -> System.out.println(or.getHomePage() + " " + or.getPosts()));
    }
}