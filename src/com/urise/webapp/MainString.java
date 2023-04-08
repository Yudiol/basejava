package com.urise.webapp;

import com.urise.webapp.model.ContactType;

import java.util.EnumMap;
import java.util.Map;

public class MainString {

    static Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    public static void main(String[] args) {


//        for (ContactType type : ContactType.values())
//            System.out.println(type.getTitle());

        System.out.println(contacts.keySet());
        System.out.println(getContact(ContactType.GITHUB));

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