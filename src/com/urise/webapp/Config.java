package com.urise.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private final static Config INSTANCE = new Config();
    protected final String PROPS = "./config/resumes.properties";

    private final Properties properties = new Properties();
    private final String STORAGE_DIR;
    private final String url;
    private final String user;
    private final String password;

    public static Config get() {
        return INSTANCE;
    }

    public Config() {
        try (InputStream is = Files.newInputStream(Paths.get(PROPS))) {
            properties.load(is);
            STORAGE_DIR = (String) properties.get("storage.dir");
            url = (String) properties.get("db.url");
            user = (String) properties.get("db.user");
            password = (String) properties.get("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS);
        }
    }

    public String getSTORAGE_DIR() {
        return STORAGE_DIR;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
