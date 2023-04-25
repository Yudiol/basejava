package com.urise.webapp.storage.serializer;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements SerializationStorage {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {

            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            Map<ContactType, String> contacts = resume.getContacts();
            int counter = contacts.size();
            dos.writeInt(counter);

            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            dos.writeInt(resume.getSections().size());
            for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
                SectionType sectionType = entry.getKey();
                Section section = entry.getValue();
                String className = section.getClass().getSimpleName();
                dos.writeUTF(className);
                dos.writeUTF(sectionType.toString());

                if (section instanceof TextSection) {
                    writeTextSection(resume, dos, sectionType);
                } else if (section instanceof ListSection) {
                    writeListSection(resume, dos, sectionType);
                } else if (section instanceof OrganizationSection) {
                    writeOrganisationSection(resume, dos, sectionType);
                } else {
                    throw new StorageException("The exception was thrown while writing file. The class "
                            + className + " didn't found. Check the list of section.", "");
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        List<String> sectionList = Arrays.asList(
                "TextSection",
                "ListSection",
                "OrganizationSection");

        try (DataInputStream dis = new DataInputStream(is)) {

            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int counter = dis.readInt();
            for (int i = 0; i < counter; i++) {
                resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int counterSection = dis.readInt();
            for (int i = 0; i < counterSection; i++) {
                String className = dis.readUTF();
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                if (sectionList.get(0).equalsIgnoreCase(className)) {
                    readTextSection(resume, dis, sectionType);
                } else if (sectionList.get(1).equalsIgnoreCase(className)) {
                    readListSection(resume, dis, sectionType);
                } else if (sectionList.get(2).equalsIgnoreCase(className)) {
                    readOrganizationSection(resume, dis, sectionType);
                } else {
                    throw new StorageException("The exception was thrown while reading the file resume. The class "
                            + className + " didn't found. Check an ArrayList named the sectionList into the 'doRead' method for matching Sections and the list.", "");
                }
            }
            return resume;
        }
    }

    private void readTextSection(Resume resume, DataInputStream dis, SectionType sectionType) throws IOException {
        resume.setSection(sectionType, new TextSection(dis.readUTF()));
    }

    private void readListSection(Resume resume, DataInputStream dis, SectionType sectionType) throws IOException {
        int counter = dis.readInt();
        List<String> listSection = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            listSection.add(dis.readUTF());
        }
        resume.setSection(sectionType, new ListSection(listSection));
    }

    private void readOrganizationSection(Resume resume, DataInputStream dis, SectionType sectionType) throws
            IOException {
        List<Organization> organizations = new ArrayList<>();
        int counterOrganizations = dis.readInt();
        for (int i = 0; i < counterOrganizations; i++) {
            Link link = new Link(dis.readUTF(), dis.readUTF());
            List<Period> periods = new ArrayList<>();
            int counterPeriods = dis.readInt();
            for (int j = 0; j < counterPeriods; j++) {
                periods.add(new Period(LocalDate.parse(dis.readUTF()), LocalDate.parse(dis.readUTF()), dis.readUTF(), dis.readUTF()));
            }
            organizations.add(new Organization(link, periods));
        }
        resume.setSection(sectionType, new OrganizationSection(organizations));
    }


    private void writeOrganisationSection(Resume resume, DataOutputStream dos, SectionType sectionType) throws
            IOException {
        List<Organization> organizations = ((OrganizationSection) resume.getSection(sectionType)).getOrganizations();
        int counterOrganizations = organizations.size();
        dos.writeInt(counterOrganizations);
        for (int i = 0; i < counterOrganizations; i++) {
            dos.writeUTF(String.valueOf(organizations.get(i).getHomePage().getName()));
            dos.writeUTF(String.valueOf(organizations.get(i).getHomePage().getUrl()));
            List<Period> periods = organizations.get(i).getPosts();
            int counterPeriods = periods.size();
            dos.writeInt(counterPeriods);
            for (int j = 0; j < counterPeriods; j++) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dos.writeUTF(periods.get(j).getStartDate().format(formatter));
                dos.writeUTF(periods.get(j).getEndDate().format(formatter));
                dos.writeUTF(periods.get(j).getTitle());
                dos.writeUTF(periods.get(j).getDescription());
            }
        }
    }

    private void writeListSection(Resume resume, DataOutputStream dos, SectionType sectionType) throws IOException {
        List<String> listSection = ((ListSection) resume.getSection(sectionType)).getItems();
        int counter = listSection.size();
        dos.writeInt(counter);
        for (int i = 0; i < counter; i++) {
            dos.writeUTF(listSection.get(i));
        }
    }

    private void writeTextSection(Resume resume, DataOutputStream dos, SectionType sectionType) throws IOException {
        dos.writeUTF(resume.getSection(sectionType).toString());
    }
}
