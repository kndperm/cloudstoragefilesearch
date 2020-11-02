package ru.kharlashko.Service.Connections.GoogleDrive;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import ru.kharlashko.Classes.FileJSON;
import ru.kharlashko.Classes.SearchParams;
import ru.kharlashko.Classes.UploadParams;
import ru.kharlashko.Enums.FileSource;
import ru.kharlashko.Interfaces.IService;

public class GoogleServiceAPI implements IService {

    private static final String APPLICATION_NAME = "MonitoringDemo";
    private static final String CREDENTIAL_FILE_PATH = "/google/client_secret.json";
    private static final String TOKEN_DIRECTORY_PATH = "src/main/resources/google/tokens";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCORES = Collections.singletonList(DriveScopes.DRIVE);
    private Drive service;

    public GoogleServiceAPI() {
    }

    // Get user Credential
    private static Credential getCredential(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        InputStream input = GoogleServiceAPI.class.getResourceAsStream(CREDENTIAL_FILE_PATH);
        if (input == null)
            throw new FileNotFoundException(
                    "Resource not found: " + CREDENTIAL_FILE_PATH + ". Please get credential.json");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(input));

        GoogleAuthorizationCodeFlow codeFlow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCORES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKEN_DIRECTORY_PATH)))
                        .setAccessType("offline").build();

        // LocalServerReceiver receiver = new
        // LocalServerReceiver.Builder().setPort(8888).build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().build();
        return new AuthorizationCodeInstalledApp(codeFlow, receiver).authorize("user");
    }

    // Connect to Google Drive
    public void Connect() {

        Credential credential;
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            credential = getCredential(HTTP_TRANSPORT);
            service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (GeneralSecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    // Upload File
    public String Upload(UploadParams params) throws IOException {
        File fileMeta = new File();
        fileMeta.setName(params.getName());
        fileMeta.setMimeType(params.getMimeType().toString());

        java.io.File filePath = new java.io.File(params.getOriginalFileName());
        FileContent mediaContent = new FileContent(params.getMimeType().toString(), filePath);

        File file = service.files().create(fileMeta, mediaContent).setFields("id").execute();
        return file.getId();
    }

    // Download file
    public void Download(String id) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        service.files().get(id).executeAndDownloadTo(outputStream);
    }

    // Download file
    public void Delete(String fileId) throws IOException {
        service.files().delete(fileId).execute();
    }

    // Search Files
    public List<FileJSON> Search(SearchParams params) throws IOException {
        String pageToken = null;
        List<FileJSON> searchResult = new ArrayList<FileJSON>();
        List<String> keys = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        String q = "";

        params.getParamsAsHashMap().forEach((k, v) -> {
            keys.add(k);
            values.add(v);
        });

        for (int i = 0; i < params.getParamsAsHashMap().size(); i++) {
            if (i != 0)
                q += " and ";

            q += keys.toArray()[0] + "=" + values;
        }

        do {
            // FileList result = service.files().list()
            // .setQ("mimeType=" + params.getMimeType().toString() + " and name=" +
            // params.getName())
            // .setSpaces("drive").setFields("nextPageToken, files(id, name, createdTime,
            // fileExtension, size)")
            // .setPageToken(pageToken).execute();

            FileList result = service.files().list().setQ(q).setSpaces("drive")
                    .setFields("nextPageToken, files(id, name, createdTime, fileExtension, size, modifiedByMeTime)")
                    .setPageToken(pageToken).execute();

            for (File file : result.getFiles()) {
                file.getId();
                file.getName();
                file.getCreatedTime();
                file.getFileExtension();
                file.getSize();
                file.getModifiedByMeTime();
                // file.getContentHints();
                // file.getDescription();
                // file.getFactory();
            }

            pageToken = result.getNextPageToken();
        } while (pageToken != null);

        return searchResult;
    }

    // Show all files in Google Drive
    public List<FileJSON> ShowAll() {

        List<File> files = new ArrayList<File>();

        try {
            FileList result = service.files().list().setPageSize(1000)
                    .setFields("nextPageToken, files(id, name, mimeType)").execute();
            files = result.getFiles();

        } catch (IOException e) {
        }

        if (files == null || files.isEmpty()) {
        }

        List<FileJSON> fileJSONList = new ArrayList<FileJSON>();

        for (File f : files) {

            FileJSON fileJSON = new FileJSON(f.getName(), f.getFileExtension(), f.getMimeType(),
                    f.getCreatedTime().toString(), f.getModifiedTime().toString(), FileSource.GoogleDrive, f.getId());
            fileJSONList.add(fileJSON);
        }

        return fileJSONList;
    }
}