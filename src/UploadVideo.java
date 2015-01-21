

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;

/**
 * Uploading a video to a user's account using the YouTube Data API (V3) with OAuth2 for
 * authorization.
 *
 */

public class UploadVideo {

  /** Global instance of the HTTP transport. */
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

  /** Global instance of Youtube object to make all API requests. */
  private static YouTube youtube;
  
 
  
  /* Global instance of the format used for the video being uploaded (MIME type). */
  private static String VIDEO_FILE_FORMAT = "video/*";

  /**
   * Authorizes the installed application to access user's protected data.
   *
   * @param scopes list of scopes needed to run youtube upload.
   */
  private static Credential authorize(List<String> scopes) throws Exception {

    // Load client secrets.
	  String secretPath= "client_secret_948017279680-6emo3qagbke5sbm8p06j85a5k8tua7g3.apps.googleusercontent.com.json";
	  FileInputStream fis = new FileInputStream(secretPath);
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load( JSON_FACTORY, fis);
    // Set up file credential store.
    FileCredentialStore credentialStore = new FileCredentialStore(
        new File(System.getProperty("user.home"), ".credentials/youtube-api-uploadvideo.json"),
        JSON_FACTORY);

    // Set up authorization code flow.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialStore(credentialStore)
        .build();

    // Build the local server and bind it to port 9000
    LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();

    // Authorize.
    return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
  }

  /**
   * Uploads user selected video in the project folder to the user's YouTube account using OAuth2
   * for authentication.
   *
   * @param args command line args (not used).
   */
  public static void shareVideoOnYoutube(String path, String title,String content) {
	  
	 File videoFile= new File(path);
    // Scope required to upload to YouTube.
    List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");

    try {
      // Authorization.
      Credential credential = authorize(scopes);

      // YouTube object used to make all API requests.
      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
          "Drive & Capture").build();


      // Add extra information to the video before uploading.
      Video videoObjectDefiningMetadata = new Video();

      /*
       * Set the video to public, so it is available to everyone (what most people want). This is
       * actually the default, but I wanted you to see what it looked like in case you need to set
       * it to "unlisted" or "private" via API.
       */
      VideoStatus status = new VideoStatus();
      status.setPrivacyStatus("public");
      videoObjectDefiningMetadata.setStatus(status);

      // We set a majority of the metadata with the VideoSnippet object.
      VideoSnippet snippet = new VideoSnippet();

      /*
       * The Calendar instance is used to create a unique name and description for test purposes, so
       * you can see multiple files being uploaded. You will want to remove this from your project
       * and use your own standard names.
       */
      if(title==null){
    	  title= "Drive&Capture video: " + path;
    	  content= WriteData.loadVideoData(path);
      }
    	  snippet.setTitle(title);
    	  snippet.setDescription(content);
      

      // Set your keywords.
      List<String> tags = new ArrayList<String>();
      tags.add("Drive&Capture");
      tags.add("java");
      snippet.setTags(tags);

      // Set completed snippet to the video object.
      videoObjectDefiningMetadata.setSnippet(snippet);

      InputStreamContent mediaContent = new InputStreamContent(
          VIDEO_FILE_FORMAT, new BufferedInputStream(new FileInputStream(videoFile)));
      mediaContent.setLength(videoFile.length());

      /*
       * The upload command includes: 1. Information we want returned after file is successfully
       * uploaded. 2. Metadata we want associated with the uploaded video. 3. Video file itself.
       */
      YouTube.Videos.Insert videoInsert = youtube.videos()
          .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

      // Set the upload type and add event listener.
      MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

      /*
       * Sets whether direct media upload is enabled or disabled. True = whole media content is
       * uploaded in a single request. False (default) = resumable media upload protocol to upload
       * in data chunks.
       */
      uploader.setDirectUploadEnabled(false);

      MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
        public void progressChanged(MediaHttpUploader uploader) throws IOException {
          switch (uploader.getUploadState()) {
            case INITIATION_STARTED:
              System.out.println("Initiation Started");
              break;
            case INITIATION_COMPLETE:
              System.out.println("Initiation Completed");
              break;
            case MEDIA_IN_PROGRESS:
              System.out.println("Upload in progress");
              System.out.println("Upload percentage: " + uploader.getProgress());
              break;
            case MEDIA_COMPLETE:
              System.out.println("Upload Completed!");
              break;
            case NOT_STARTED:
              System.out.println("Upload Not Started!");
              break;
          }
        }
      };
      uploader.setProgressListener(progressListener);

      // Execute upload.
     videoInsert.execute();

    } catch (GoogleJsonResponseException e) {
      System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
          + e.getDetails().getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
      e.printStackTrace();
    } catch (Throwable t) {
      System.err.println("Throwable: " + t.getMessage());
      t.printStackTrace();
    }
  }

 

}