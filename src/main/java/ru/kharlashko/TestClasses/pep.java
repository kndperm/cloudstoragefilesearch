package ru.kharlashko.TestClasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.kharlashko.Classes.FileJSON;
import ru.kharlashko.Connection.GoogleDrive.GoogleMimeType;
import ru.kharlashko.Connection.GoogleDrive.GoogleServiceAPI;
import ru.kharlashko.Service.ElasticSearch.ElasticSearchService;

//Nest file to check functions
public class pep {

    public static void main(String[] args) {
        //UploadToGoogle();
        //DownloadFromGoogle();
        //DeleteFromGoogle();
        ShowAllFromElastic();
    }

    // Connect to Google
    public static void GConnect() {
        GoogleServiceAPI googleServiceAPI = new GoogleServiceAPI();
        googleServiceAPI.Connect();

        System.out.println("Google was Connected");
    }

    // Show all files in Google
    public static void ShowFilesInGoogle() {
        GoogleServiceAPI googleServiceAPI = new GoogleServiceAPI();
        googleServiceAPI.Connect();

        System.out.println("Google was Connected");

        List<FileJSON> allFiles = new ArrayList<FileJSON>();

        List<FileJSON> googleFiles = googleServiceAPI.ShowAll();
        allFiles.addAll(googleFiles);

        for (FileJSON fileJSON : googleFiles) {
            System.out.println(fileJSON.toString());
        }
    }

    public static void UploadToGoogle() {
        GoogleServiceAPI googleServiceAPI = new GoogleServiceAPI();
        googleServiceAPI.Connect();

        String name = "lilacstare.png";
        GoogleMimeType mimeType = GoogleMimeType.PNG;
        String originalFilePath = "F:/картинки/Emodz/lilacstare.png";
        String key = "-";

        try {
            key = googleServiceAPI.Upload(name, mimeType, originalFilePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(key);
    }

    public static void DeleteFromGoogle() {
        GoogleServiceAPI googleServiceAPI = new GoogleServiceAPI();
        googleServiceAPI.Connect();

        String fileId = "1j0unUtB5HSmqgs7orz4s9PFlKbczMR45";

        try {
            googleServiceAPI.Delete(fileId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("0,0");
    }

    public static void DownloadFromGoogle() {
        GoogleServiceAPI googleServiceAPI = new GoogleServiceAPI();
        googleServiceAPI.Connect();

        String fileId = "1j0unUtB5HSmqgs7orz4s9PFlKbczMR45";

        try {
            googleServiceAPI.Download(fileId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("0,0");
    }

    // Add files to elastic
    public static void AddFilesInElastic() {
        GoogleServiceAPI googleServiceAPI = new GoogleServiceAPI();
        googleServiceAPI.Connect();

        System.out.println("Google was Connected");

        List<FileJSON> allFiles = new ArrayList<FileJSON>();

        List<FileJSON> googleFiles = googleServiceAPI.ShowAll();
        allFiles.addAll(googleFiles);

        try {
            System.out.println("her");
            ElasticSearchService service = new ElasticSearchService();
            for (FileJSON files : allFiles) {
                // if (!service.isThisFileExist("knd_google", files.getName())) {
                service.addJSONToElastic(files, "knd_google", files.getName());
                // System.out.println("add " + files.getName());
                // }
            }
            service.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static void ShowAllFromElastic() {
        List<FileJSON> list = new ArrayList<FileJSON>();

        try {
            ElasticSearchService service = new ElasticSearchService();
            list = service.showAll();
            service.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (FileJSON fileJSON : list) {
            System.out.println(fileJSON.toString());
        }

    }
}