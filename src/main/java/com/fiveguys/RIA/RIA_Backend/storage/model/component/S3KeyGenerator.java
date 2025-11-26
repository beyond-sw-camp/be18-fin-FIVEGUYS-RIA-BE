package com.fiveguys.RIA.RIA_Backend.storage.model.component;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class S3KeyGenerator {

    public String generateKey(String folder, String originalName) {
        String ext = extractExtension(originalName);
        String uuid = UUID.randomUUID().toString();

        if (folder == null || folder.isBlank()) {
            return uuid + ext;
        }
        return folder + "/" + uuid + ext;
    }

    private String extractExtension(String originalName) {
        if (originalName == null) return "";
        int dot = originalName.lastIndexOf('.');
        if (dot == -1) return "";
        return originalName.substring(dot);
    }
}
