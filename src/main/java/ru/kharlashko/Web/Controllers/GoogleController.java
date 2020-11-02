package ru.kharlashko.Web.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.kharlashko.Classes.FileJSON;
import ru.kharlashko.Classes.SearchParams;
import ru.kharlashko.Classes.UploadParams;
import ru.kharlashko.Enums.MimeType;
import ru.kharlashko.Service.Connections.GoogleDrive.GoogleServiceAPI;
import ru.kharlashko.Enums.FileSource;

@RestController
public class GoogleController {

    GoogleServiceAPI gService;

    @RequestMapping("/google/connect")
    public String Connect() {
        gService = new GoogleServiceAPI();
        gService.Connect();
        return "Google Drive is connected";
    }

    @RequestMapping("/google/search")
    public List<FileJSON> Search(@RequestParam SearchParams params) {

        List<FileJSON> result = new ArrayList<FileJSON>();

        try {
            result = gService.Search(params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @RequestMapping("/google/download")
    public String Download(@RequestParam String id) {
        try {
            gService.Download(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File was downloaded";
    }

    @RequestMapping("/google/show")
    public List<FileJSON> ShowAll() {
        List<FileJSON> result = gService.ShowAll();
        return result;
    }

    @RequestMapping("/google/upload")
    public String Upload(@RequestParam String name, @RequestParam MimeType mimeType,
            @RequestParam String originalFilePath) {
        UploadParams params = new UploadParams(name, originalFilePath, mimeType);
        try {
            gService.Upload(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File was upload";
    }
}