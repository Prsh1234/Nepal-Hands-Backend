package com.example.nepalhandsbackend.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileTextUtils {

    public List<String> splitLines(String text) {
        if (text == null || text.isBlank()) return List.of();

        return Arrays.stream(text.split("\\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public byte[] toBytes(MultipartFile file) {
        try {
            return (file != null && !file.isEmpty()) ? file.getBytes() : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    public List<byte[]> toBytesList(List<MultipartFile> files) {
        if (files == null) return List.of();

        return files.stream()
                .map(this::toBytes)
                .filter(Objects::nonNull)
                .toList();
    }
}
