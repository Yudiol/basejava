package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ListStorage extends AbstractStorage {
    List<Resume> resumes = new ArrayList<>();

    @Override
    public List<Resume> getAll() {
        return resumes;
    }

    @Override
    public void saveResume(Resume r, int index) {
        resumes.add(r);
    }

    @Override
    void clearResume() {
        resumes.clear();
    }

    @Override
    void deleteResume(int index) {
        resumes.remove(getResume(index));
    }

    @Override
    public int sizeResume() {
        return resumes.size();
    }

    public int getIndex(String uuid) {
        Optional<Resume> resume = resumes.stream().filter(r -> Objects.equals(r.getUuid(), uuid)).findFirst();
        return resume.map(value -> resumes.indexOf(value)).orElse(-1);
    }

    @Override
    protected void updateResume(int index, Resume resume) {
        resumes.set(index, resume);
    }

    @Override
    protected Resume getResume(int index) {
        return resumes.get(index);
    }
}
