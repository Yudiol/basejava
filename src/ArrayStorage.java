import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    {
        Arrays.fill(storage, null);
    }

    void clear() {
        Arrays.fill(storage, null);
    }

    void save(Resume r) {
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] != null && storage[i] == r) {
                break;
            }
            if (storage[i] == null) {
                storage[i] = r;
                break;
            }
        }

    }

    Resume get(String uuid) {
        getAll();
        for (Resume resume : storage) {
            if (resume != null && Objects.equals(resume.uuid, uuid)) {
                return resume;
            }
        }
        return null;
    }

    void delete(String uuid) {
        int mark =-1;
        for (int i = 0; i < storage.length - 1; i++) {
            if (storage[i] != null && Objects.equals(storage[i].uuid, uuid)) {
                storage[i] = null;
                mark = i;
            }
            if (mark != -1) {
                storage[i] = storage[i + 1];
            }
            if (i == storage.length - 2) {
                storage[i + 1] = null;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return storage;
    }

    int size() {
        int counter = 0;
        for (Resume resume : storage) {
            if (resume != null) {
                counter++;
            }
        }
        return counter;
    }
}
