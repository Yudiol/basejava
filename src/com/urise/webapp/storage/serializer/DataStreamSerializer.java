package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataStreamSerializer implements SerializationStorage {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            writeWithException(resume.getContacts().keySet(), dos, (contactType) -> {
                dos.writeUTF(contactType.toString());
                dos.writeUTF(resume.getContact(contactType));
            });

            Writer<SectionType> writer = (sectionType) -> {
                dos.writeUTF(sectionType.toString());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(resume.getSection(sectionType).toString());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeWithException(((ListSection) resume.getSection(sectionType)).getItems(), dos, dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeWithException(((OrganizationSection) resume.getSection(sectionType)).getOrganizations(), dos, (organizations) -> {
                            dos.writeUTF(String.valueOf(organizations.getHomePage().getName()));
                            dos.writeUTF(String.valueOf(organizations.getHomePage().getUrl()));
                            writeWithException(organizations.getPosts(), dos, (org) -> {
                                dos.writeUTF(org.getStartDate().toString());
                                dos.writeUTF(org.getEndDate().toString());
                                dos.writeUTF(org.getTitle());
                                dos.writeUTF(org.getDescription());
                            });
                        });
                        break;
                }
            };
            writeWithException(resume.getSections().keySet(), dos, writer);
        }
    }

    interface Writer<T> {
        void write(T t) throws IOException;
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            writer.write(item);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());

            readWithException(dis, () -> {
                resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
                return null;
            });

            readWithException(dis, () -> {
                        SectionType sectionType = SectionType.valueOf(dis.readUTF());
                        switch (sectionType) {
                            case PERSONAL:
                            case OBJECTIVE:
                                resume.setSection(sectionType, new TextSection(dis.readUTF()));
                                break;
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                resume.setSection(sectionType, new ListSection(readWithException(dis, dis::readUTF)));
                                break;
                            case EXPERIENCE:
                            case EDUCATION:
                                resume.setSection(sectionType, new OrganizationSection(readWithException(dis, () -> {
                                    return (new Organization(new Link(dis.readUTF(), dis.readUTF()), readWithException(dis, () ->
                                            {
                                                return new Period(LocalDate.parse(dis.readUTF()), LocalDate.parse(dis.readUTF()), dis.readUTF(), dis.readUTF());
                                            }
                                    )));
                                })));
                                break;
                        }
                        return null;
                    }
            );
            return resume;
        }
    }

    interface Reader<T> {
        T read() throws IOException;
    }

    private <T> List<T> readWithException(DataInputStream dis, Reader<T> reader) throws IOException {
        int counter = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            list.add(reader.read());
        }
        return list;
    }
}
