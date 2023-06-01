package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {

    private static Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public List<Resume> getAllSorted() {
        LOG.info("GetAllSorted");
        List<Resume> resumes = getAll();
        resumes.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return resumes;
    }

    public void clear()  {
        LOG.info("Clear");
        clearResume();
    }

    public void update(Resume r) {
        LOG.info("Update " + r);
        SK searchKey = getNotExistingSearchKey(r.getUuid());
        updateResume(searchKey, r);
    }

    public Resume get(String uuid)  {
        LOG.info("Get " + uuid);
        SK searchKey = getNotExistingSearchKey(uuid);
        return getResume(searchKey);
    }

    public void save(Resume r) {
        LOG.info("Save " + r);
        SK searchKey = getExistingSearchKey(r.getUuid());
        saveResume(searchKey, r);
    }

    public void delete(String uuid) {
        LOG.info("Delete " +uuid);
        SK searchKey = getNotExistingSearchKey(uuid);
        deleteResume(searchKey);
    }

    public int size() {
        LOG.info("Size");
        return sizeResume();
    }

    private SK getExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    abstract SK getSearchKey(String uuid);

    abstract boolean isExist(SK uuid);

    abstract void saveResume(SK searchKey, Resume r);

    abstract void clearResume();

    abstract int sizeResume();

    abstract void deleteResume(SK searchKey);

    public abstract List<Resume> getAll();

    abstract void updateResume(SK searchKey, Resume resume);

    abstract Resume getResume(SK searchKey);
}
