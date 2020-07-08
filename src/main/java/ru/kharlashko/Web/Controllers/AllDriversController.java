package ru.kharlashko.Web.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.kharlashko.Classes.FileJSON;
import ru.kharlashko.Classes.SearchParams;
import ru.kharlashko.Connection.GoogleDrive.GoogleServiceAPI;
import ru.kharlashko.Service.ElasticSearch.ElasticSearchService;

@RestController
public class AllDriversController {

    @RequestMapping("ShowAll")
    public List<FileJSON> showAll() {
        List<FileJSON> allFiles = new ArrayList<FileJSON>();

        try {
            ElasticSearchService elasticSearchService = new ElasticSearchService();
            allFiles = elasticSearchService.showAll();
            elasticSearchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allFiles;
    }

    @RequestMapping("Search")
    public List<FileJSON> searchInElastic(@RequestParam String[] names, @RequestParam String[] values) {
        List<FileJSON> results = new ArrayList<FileJSON>();

        try {
            ElasticSearchService elasticSearchService = new ElasticSearchService();
            results = elasticSearchService.searchJSON(names, values);
            elasticSearchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    @RequestMapping("SearchInDriveAll")
    public List<FileJSON> searchInDriver(@RequestParam SearchParams params) {
        List<FileJSON> finalResults = new ArrayList<FileJSON>();

        if (params.isGoogleInclude) {
            GoogleServiceAPI googleServiceAPI = new GoogleServiceAPI();
            try {
                List<FileJSON> gResults = googleServiceAPI.Search(params);
                finalResults.addAll(gResults);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return finalResults;
    }

    // TODO удадалять данных которых нет
    @RequestMapping("UpdateDataToElasticsearch")
    public void updateDataInElasticsearch() {
        GoogleServiceAPI googleServiceAPI = new GoogleServiceAPI();

        List<FileJSON> allFilesFromGoogle = new ArrayList<FileJSON>();

        List<FileJSON> googleFiles = googleServiceAPI.ShowAll();
        allFilesFromGoogle.addAll(googleFiles);

        try {
            ElasticSearchService elasticSearchService = new ElasticSearchService();
            for (FileJSON files : allFilesFromGoogle) {
                if (!elasticSearchService.isThisFileExist("", "")) {
                    elasticSearchService.addJSONToElastic(files, "index", "indexId");
                }
            }
            elasticSearchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}