package com.project.vert_backend.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides services for reading, writing and checking for files.
 * @author Selwyn Lehmann
 */
public class FileService {

    /**
     * Write the InputStream to a File on the file system.
     * @param content       The file contents to be written
     * @param fileLocation  The location to write the file
     */
    public void writeToFile(InputStream content, String fileLocation) {

        try {
            File newFile = new File(fileLocation);
            if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
            }
            int read = 0;
            byte[] bytes = new byte[1024];

            OutputStream out = new FileOutputStream(newFile);
            while ((read = content.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            System.out.println("FileService: write success");
        } catch (IOException e) {
            System.out.println("FileService: write Exception - " + e);
        }
    }
}
