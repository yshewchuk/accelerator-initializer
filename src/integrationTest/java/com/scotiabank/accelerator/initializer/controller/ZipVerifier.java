package com.scotiabank.accelerator.initializer.controller;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipVerifier {

    public static ResultMatcher isValidZip(TemporaryFolder folder) {
        return mvcResult -> {
            verifyZip(mvcResult.getResponse().getContentAsByteArray(), folder);
        };
    }

    public static void verifyZip(byte[] content, TemporaryFolder folder) throws IOException {
        File temp = folder.newFile("projectOutput.zip");
        FileUtils.writeByteArrayToFile(temp, content);
        Path dir = folder.getRoot().toPath();

        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(temp))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = dir.resolve(fileName).toFile();
                newFile.getParentFile().mkdirs(); // make sure directory exists
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        }
    }
}
