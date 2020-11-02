package ru.kharlashko.Interfaces;

import java.io.IOException;
import java.util.List;

import ru.kharlashko.Classes.FileJSON;
import ru.kharlashko.Classes.SearchParams;
import ru.kharlashko.Classes.UploadParams;

public interface IService {

    public void Connect();
    public String Upload(UploadParams params) throws IOException;
    public void Download(String id) throws IOException;
    public List<FileJSON> Search(SearchParams params) throws IOException;
    public List<FileJSON> ShowAll();
}