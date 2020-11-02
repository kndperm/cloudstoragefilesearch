package ru.kharlashko.Classes;

import ru.kharlashko.Enums.MimeType;

public class UploadParams {
    private String name;
    private String originalFileName;
    private MimeType mimeType;

    public UploadParams(String name, String originalFileName, MimeType mimeType) {
        this.name = name;
        this.originalFileName = originalFileName;
        this.mimeType = mimeType;
    }

    public String getName() {
        return this.name;
    }

    public String getOriginalFileName() {
        return this.originalFileName;
    }

    public MimeType getMimeType() {
        return this.mimeType;
    }
}