package ru.kharlashko.Web.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.kharlashko.Classes.FileJSON;
import ru.kharlashko.Classes.SearchParams;
import ru.kharlashko.Connection.GoogleDrive.GoogleMimeType;
import ru.kharlashko.Connection.GoogleDrive.GoogleServiceAPI;
import ru.kharlashko.Enums.FileSource;

@RestController
public class GoogleController {

    GoogleServiceAPI gService;

    @RequestMapping("google/connect")
    public String Connect() {
        gService = new GoogleServiceAPI();
        gService.Connect();
        return "Google Drive is connected";
    }

    @RequestMapping("google/search")
    public List<FileJSON> Search(@RequestParam SearchParams params) {
        
        List<FileJSON> result = new ArrayList<FileJSON>();
        
        try {
            result = gService.Search(params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return new FileJSON("name", "extention", GoogleMimeType.GOOGLEDOC.toString(), "fileIdInSource",
        //FileSource.GoogleDrive);

        return result;
    }

    @RequestMapping("google/download")
    public String Download(@RequestParam String id) {
        try {
            gService.Download(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File was downloaded";
    }

    @RequestMapping("google/show")
    public List<FileJSON> ShowAll() {
        List<FileJSON> result = gService.ShowAll();
        return result;
    }

    @RequestMapping("google/upload")
    public String Upload(@RequestParam String name, @RequestParam Object mimeType,
            @RequestParam String originalFilePath) {
        try {
            gService.Upload(name, mimeType, originalFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File was upload";
    }
}