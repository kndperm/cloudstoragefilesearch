package ru.kharlashko.Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import ru.kharlashko.Enums.FileSource;

public class FileJSON {

    protected String Name;
    protected String Extention;
    protected String MimeType;
    protected Date CreatedTime;
    protected Date ModifiedTime;
    protected FileSource Source;
    protected String FileIdInSource;
    protected JSONArray KeyWords;

    // Class initializing with hashmap
    public FileJSON(Map<String, Object> map) {
        Name = (String) map.get("Name");
        Extention = (String) map.get("Extention");
        MimeType = (String) map.get("MimeType");
        CreatedTime = (Date) map.get("CreatedTime");
        ModifiedTime = (Date) map.get("ModifiedTime");
        Source = (FileSource) map.get("Source");
        FileIdInSource = (String) map.get("FileIdInSource");

        KeyWords = new JSONArray();
    }

    // Class initializing with seperal parametrs
    public FileJSON(String name, String extention, String mimeType, String createdTime, String modifiedTime,
            FileSource source, String fileIdInSource) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        Name = name;
        Extention = extention;
        MimeType = mimeType;
        Source = source;
        try {
            CreatedTime = formatter.parse(createdTime);
            ModifiedTime = formatter.parse(modifiedTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        FileIdInSource = fileIdInSource;

        KeyWords = new JSONArray();
    }

    // #region getters
    public String getName() {
        return Name;
    }

    public String getMimeType() {
        return MimeType;
    }
    // #endregion

    // #region setters
    public void setFileIdInSource(String value) {
        FileIdInSource = value;
    }
    // #endregion

    public void AddKeyWords(Object value) {
        KeyWords.put(value);
    }

    // Transform class data to json object
    public JSONObject toJSON() {

        JSONObject obj = new JSONObject();

        obj.put("Name", Name);
        obj.put("Extention", Extention);
        obj.put("MimeType", MimeType);
        obj.put("CreatedTime", CreatedTime);
        obj.put("ModifiedTime", ModifiedTime);
        obj.put("FileIdInSource", FileIdInSource);
        obj.put("Source", Source);

        return obj;
    }
}