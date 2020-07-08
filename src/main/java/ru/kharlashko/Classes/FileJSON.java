package ru.kharlashko.Classes;

import java.util.Map;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import ru.kharlashko.Enums.FileSource;

public class FileJSON {

    protected String Name;
    protected String Extention;
    protected String MimeType;
    protected DateTime CreatedTime;
    protected DateTime ModifiedTime;
    protected FileSource Source;
    protected String FileIdInSource;
    protected JSONArray KeyWords;
    
    public FileJSON(Map<String, Object> map)
    {
        Name = (String) map.get("Name");
        Extention = (String) map.get("Extention");
        MimeType = (String) map.get("MimeType");
        FileIdInSource = (String) map.get("FileIdInSource");
        Source = (FileSource) map.get("Source");

        KeyWords = new JSONArray();
    }

    public FileJSON(String name, String extention, String mimeType, String fileIdInSource, FileSource source) {
        Name = name;
        Extention = extention;
        MimeType = mimeType;
        Source = source;
        FileIdInSource = fileIdInSource;

        KeyWords = new JSONArray();
    }


    public String getName() {
        return Name;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setFileIdInSource(String value) {
        FileIdInSource = value;
    }

    public void AddKeyWords(Object value) {
        KeyWords.put(value);
    }

    public JSONObject toJSON() {

        JSONObject obj = new JSONObject();

        obj.put("Name", Name);
        obj.put("Extention", Extention);
        obj.put("MimeType", MimeType);
        obj.put("FileIdInSource", FileIdInSource);
        obj.put("Source", Source);

        return obj;
    }
}