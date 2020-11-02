package ru.kharlashko.Enums;

public enum MimeType {
    GOOGLEFOLDER("'application/vnd.google-apps.folder'"),
    GOOGLEDOC("'application/vnd.google-apps.document'"),
    GOOGLESHEET("'application/vnd.google-apps.spreadsheet'"),
    GOOGLEPHOTO("'application/vnd.google-apps.photo'"),
    SEVEN_Z("'application/x-7z-compressed'"),
    AVI("'video/x-msvideo'"),
    DOC("'application/msword'"),
    DOCX("'application/vnd.openxmlformats-officedocument.wordprocessingml.document'"),
    EXE("'application/octet-stream'"),
    GIF("'image/gif'"),
    JPG("'image/jpeg'"),
    MThreeUEight("'audio/x-mpegurl'"),
    MFourA("'audio/m4a'"),
    MPThree("'audio/mpeg'"),
    MPFour("'video/mp4'"),
    PNG("'image/png'"),
    WAV("'audio/wav'"),
    XLSX("'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'"),
    XLS("'application/vnd.ms-excel'"),
    ZIP("'application/zip'"),
    TXT("'text/plain'"),
    RAR("'application/vnd.rar'"),
    PDF("'application/pdf'"),
    EPUB("'application/epub+zip'");

private final String type;

    MimeType (String type)
    {
        this.type = type;
    }

public static MimeType getEnum(String type)
{
    for (MimeType t : values()) {
        if(t.type.equals(type))
        return t;
    }

    return null;
}

    @Override
    public String toString()
    {
        return this.type;
    }
}