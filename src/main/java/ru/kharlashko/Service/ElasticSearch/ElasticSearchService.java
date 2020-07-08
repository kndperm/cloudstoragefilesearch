package ru.kharlashko.Service.ElasticSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import ru.kharlashko.Classes.FileJSON;

public class ElasticSearchService {

    private final String _webhost = "192.168.99.100";
    private final int _webport = 9200;

    private RestHighLevelClient client;

    public ElasticSearchService() {
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(_webhost, _webport, "http")));
    }

    // Add data to Elastic
    public void addJSONToElastic(FileJSON fileJSON, String index, String indexId) throws IOException {

        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(indexId);
        indexRequest.source(fileJSON.toJSON().toMap(), XContentType.JSON);

        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    // Search data from Elastic
    public List<FileJSON> searchJSON(String[] names, String[] values) throws IOException {

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        for (int i = 0; i < names.length; i++) {
            searchSourceBuilder.query(QueryBuilders.termQuery(names[i], values[i]));
        }

        //searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchRequest.indices("posts");
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        List<FileJSON> results = new ArrayList<FileJSON>();

        SearchHits hits = response.getHits();
        SearchHit[] hitsArray = hits.getHits();
        for (SearchHit searchHit : hitsArray) {
            Map<String, Object> map = searchHit.getSourceAsMap();

            FileJSON fileJSON = new FileJSON(map);
            results.add(fileJSON);
        }

        return results;
    }

    public List<FileJSON> showAll() throws IOException {
        List<FileJSON> results = new ArrayList<FileJSON>();

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        SearchHit[] hitsArray = hits.getHits();
        for (SearchHit searchHit : hitsArray) {
            Map<String, Object> map = searchHit.getSourceAsMap();

            FileJSON fileJSON = new FileJSON(map);
            results.add(fileJSON);
        }

        return results;
    }

    // Delete data from Elastic
    public void deleteJSON(String index, String indexId) throws IOException {
        DeleteRequest request = new DeleteRequest(index, indexId);

        client.delete(request, RequestOptions.DEFAULT);
    }

    // Update data
    public void updateJSON(List<String> names, List<String> values, String index, String indexId) throws IOException {
        UpdateRequest request = new UpdateRequest(index, indexId);

        XContentBuilder json = XContentFactory.jsonBuilder().startObject();
        for (int i = 0; i < names.size(); i++) {
            json.field(names.get(i), values.get(i));
        }
        json.endObject();

        request.doc(json);

        client.update(request, RequestOptions.DEFAULT);
    }

    // Close Service
    public void close() throws IOException {
        client.close();
    }

    public Boolean isThisFileExist(String index, String indexId) throws IOException {
        GetRequest request = new GetRequest(index, indexId);

        return client.exists(request, RequestOptions.DEFAULT);
    }
}