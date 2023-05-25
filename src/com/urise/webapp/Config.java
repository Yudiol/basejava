package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private final static Config INSTANCE = new Config();
    protected final String PROPS = "C:/Users/yudir/IdeaProjects/basejava/config/resumes.properties";

    private final Properties properties = new Properties();
    private final String STORAGE_DIR;

    public static Config get() {
        return INSTANCE;
    }

    private final Storage storage;

    private Config() {
        try (InputStream is = Files.newInputStream(Paths.get(PROPS))) {
            properties.load(is);
            STORAGE_DIR = (String) properties.get("storage.dir");
            storage = new SqlStorage((String) properties.get("db.url"), (String) properties.get("db.user"), (String) properties.get("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS);
        }
    }

    public String getSTORAGE_DIR() {
        return STORAGE_DIR;
    }

    public Storage getStorage() {
        return storage;
    }
}
