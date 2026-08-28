package com.elisa.petadoption.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
public class PetImageStorageService {
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp"
    );
    private final Path uploadDirectory = Path.of("uploads", "pets").toAbsolutePath().normalize();

    public String store(MultipartFile image) throws IOException {
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image must be 5 MB or smaller.");
        }

        String extension = EXTENSIONS.get(image.getContentType());
        if (extension == null) {
            throw new IllegalArgumentException("Use a JPG, PNG, or WebP image.");
        }

        Files.createDirectories(uploadDirectory);
        String filename = UUID.randomUUID() + "." + extension;
        Files.copy(image.getInputStream(), uploadDirectory.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/pets/" + filename;
    }
}
