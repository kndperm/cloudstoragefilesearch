package ru.kharlashko.Classes;

import java.util.HashMap;
import java.util.Map;

import ru.kharlashko.Enums.MimeType;
import ru.kharlashko.Enums.FileSource;

public class SearchParams {

    private String name;
    private MimeType mimeType;
    public boolean isGoogleInclude;
    public boolean isOneDriveInclude;
    public boolean isYandexInclude;
    private FileSource source;
    private String extention;

    public String getName() {
        return this.name;
    }

    public MimeType getMimeType() {
        return this.mimeType;
    }

    public String getExtention() {
        return extention;
    }

    public FileSource getSource() {
        return this.source;
    }

    public SearchParams(String name, MimeType mimeType, String extention, FileSource source) {
        this.name = name;
        this.mimeType = mimeType;
        this.source = source;
        this.extention = extention;
    }

    public Map<String, String> getParamsAsHashMap() {
        Map<String, String> params = new HashMap<String, String>();

        if (this.name != null || !this.name.isEmpty())
            params.put("name", this.name);

        if (this.mimeType != null)
            params.put("mimeType", this.mimeType.toString());

        if (this.extention != null || !this.extention.isEmpty())
            params.put("extention", this.extention);

        return params;
    }
}