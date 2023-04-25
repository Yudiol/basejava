package com.urise.webapp;

import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.OrganizationSection;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.storage.FileStorage;
import com.urise.webapp.storage.serializer.DataStreamSerializer;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class MainString {

    static Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);

    public static void main(String[] args) throws IOException {


//        for (ContactType type : ContactType.values())
//            System.out.println(type.getTitle());

//        System.out.println(contacts.keySet());
//        System.out.println(getContact(ContactType.GITHUB));

        FileStorage storage = new FileStorage("C:/Project/storage", new DataStreamSerializer());


        Resume resume = new ResumeData().createResume("UUID_1", "Kirill");
        storage.clear();
        storage.save(resume);
        Resume r = storage.get("UUID_1");
        System.out.println(r.getFullName());
        System.out.println(r.getUuid());
        r.getContacts().entrySet().forEach(System.out::println);
        r.getSections().entrySet().forEach(System.out::println);
        ((OrganizationSection) r.getSection(SectionType.EXPERIENCE)).getOrganizations()
                .forEach(or -> System.out.println(or.getHomePage() + " " + or.getPosts()));

//        System.out.println(ContactType.MAIL.getTitle());


//        String[] strArray = new String[]{"1", "2", "3", "4", "5"};
////        String result = "";
//        StringBuilder sb = new StringBuilder();
//        for (String str : strArray) {
//            sb.append(str).append(", ");
//        }
//        System.out.println(sb.toString());
//
//        String str1 = "abc";
//        String str3 = "c";
//        String str2 = ("ab" + str3).intern();
//        System.out.println(str1 == str2);
    }

    public static String getContact(ContactType type) {
        return contacts.get(type);
    }
}