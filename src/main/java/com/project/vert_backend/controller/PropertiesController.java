package com.project.vert_backend.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties Singleton. Loads the properties once.
 * @author Selwyn Lehmann
 */
public class PropertiesController {

    private static PropertiesController instance;

    private final Properties properties;

    private PropertiesController() {
        this.properties = new Properties();
    }

    /**
     * Retrieves Singleton instance - thread safe.
     * @return
     */
    public static PropertiesController getInstance() {
        ///Uses double-checked locking to provide thread safe instance that only blocks when
        // the instance has not yet been created (to prevent creating more than one instance)
        if (instance == null) {
            synchronized (PropertiesController.class) {
                if (instance == null) {
                    instance = new PropertiesController();
                }
            }
        }
        return instance;
    }

    public void setPropertiesFile(InputStream aPropertiesStream) {
        try {
            properties.load(aPropertiesStream);
        } catch (IOException ex) {
            System.out.println("PropertiesController: Properties file Exception - " + ex);
        }
    }

    public String getProperty(String property) {
        return properties.getProperty(property);
    }
}
