package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.model.TextSection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorageTest {
    final static String STORAGE_DIR = Config.get().getSTORAGE_DIR();
    private final ResumeTestData resumeTestData = new ResumeTestData();
    final Storage storage;
    final Resume r1 = resumeTestData.createResume("UUID_1", "Григорий Кислин");
    //    final Resume r1 = new Resume("UUID_1", "Григорий Кислин");
    final Resume r2 = resumeTestData.createResume("UUID_2", "Ivan Ivanov");
    //    final Resume r2 = new Resume("UUID_2", "Ivan Ivanov");
    final Resume r3 = resumeTestData.createResume("UUID_3", "Petr Petrov");
    //    final Resume r3 = new Resume("UUID_3", "Petr Petrov");
    final Resume r4 = resumeTestData.createResume("UUID_NOT_EXIST", "4");
//    final Resume r4 = new Resume("UUID_NOT_EXIST", "4");

    @Before
    public void setUp() throws IOException, SQLException {
        storage.clear();
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Test
    public void getAllSorted() throws SQLException {
        List<Resume> resumes = new ArrayList<>();
        resumes.add(r3);
        resumes.add(r2);
        resumes.add(r1);
        resumes.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        Assert.assertEquals(resumes, storage.getAllSorted());
    }

    // method clear()
    @Test
    public void clear() throws IOException, SQLException {
        storage.clear();
        assertSize(0);
    }

    //method update()
    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws SQLException {
        storage.update(r4);
    }

    @Test
    public void update() throws SQLException {
        Resume resume = resumeTestData.createResume("UUID_1", "1");
        resume.setContact(ContactType.GITHUB,"QWERTY");
        resume.setContact(ContactType.HOME_PHONE,"HOME_PHONE");
        resume.setSection(SectionType.PERSONAL,new TextSection("PERSONAL"));
//        Resume resume = new Resume("UUID_1", "1");
        storage.update(resume);
        Assert.assertEquals(resume, storage.get("UUID_1"));
    }

    // method get()
    @Test
    public void get() {
        assertGet(r1);
        assertGet(r2);
        assertGet(r3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(r4.getUuid());
    }

    // method save()
    @Test
    public void save() throws SQLException {
        storage.save(r4);
        assertGet(r4);
        assertSize(4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws SQLException {
        storage.save(r1);
    }

    // method size()
    @Test
    public void size() {
        assertSize(3);
    }

    // method delete()
    @Test
    public void delete() {
        storage.delete(r1.getUuid());
        assertSize(2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    void assertSize(int i) {
        Assert.assertEquals(i, storage.size());
    }
}
