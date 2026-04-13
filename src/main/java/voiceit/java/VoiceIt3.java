package voiceit.java;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;

public class VoiceIt3 {

	private static String BASE_URL = "https://api.voiceit.io";
	private String notificationUrl = "";
	private HttpClient httpClient;
	public static final String VERSION = "3.1.0";

	public VoiceIt3(String apiKey, String apiToken){
			HttpClientBuilder clientBuilder = HttpClients.custom();
			setup(clientBuilder, apiKey, apiToken);
			httpClient = clientBuilder.build();
	}

	/**
	 * Construct a VoiceIt3 client that talks to a custom base URL (for example,
	 * an on-premise deployment). TLS certificate and hostname verification use
	 * the JVM's default trust store and PKIX validation — unchanged from the
	 * standard constructor. To trust a private CA, add it to the JVM trust
	 * store (cacerts) or use the (SSLContext) overload below.
	 */
	public VoiceIt3(String apiKey, String apiToken, String customBaseURL) {
		BASE_URL = customBaseURL;
		HttpClientBuilder clientBuilder = HttpClients.custom();
		setup(clientBuilder, apiKey, apiToken);
		httpClient = clientBuilder.build();
	}

	/**
	 * Construct a VoiceIt3 client with a caller-supplied SSLContext. Use this
	 * overload when you need to trust a private CA or enforce certificate
	 * pinning. Never construct an SSLContext that accepts all certificates.
	 */
	public VoiceIt3(String apiKey, String apiToken, String customBaseURL, SSLContext sslContext) {
		BASE_URL = customBaseURL;
		HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
			.setTlsSocketStrategy(new DefaultClientTlsStrategy(sslContext))
			.build();
		HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(cm);
		setup(clientBuilder, apiKey, apiToken);
		httpClient = clientBuilder.build();
	}

	// URL-encode a path segment so caller-supplied IDs containing '/'
	// or '?' cannot change the endpoint or inject query parameters.
	private static String enc(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 not supported", e);
		}
	}

	private void setup(HttpClientBuilder clientBuilder, String apiKey, String apiToken) {
	      BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	      credentialsProvider.setCredentials(
	          new AuthScope(null, -1),
	          new UsernamePasswordCredentials(apiKey, apiToken.toCharArray()));
	      clientBuilder
	      .setDefaultCredentialsProvider(credentialsProvider)
	      .setDefaultHeaders(Arrays.asList(new BasicHeader("platformId", "29"), new BasicHeader("platformVersion", VERSION)));
	}

	/**
	 * Execute a request and return the response body as a String. Wraps the
	 * try-with-resources lifecycle that HttpClient 5 requires so call sites
	 * stay one-liners.
	 */
	private String exec(ClassicHttpRequest request) {
		try (ClassicHttpResponse response = httpClient.executeOpen(null, request, null)) {
			HttpEntity entity = response.getEntity();
			return entity == null ? "" : EntityUtils.toString(entity);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

  public String getNotificationUrl(){
    return notificationUrl;
  }

	public void addNotificationUrl(String url) throws UnsupportedEncodingException{
    notificationUrl = "?notificationURL=" + URLEncoder.encode(url, "UTF-8");
  }

	public void removeNotificationUrl() {
    notificationUrl = "";
  }

	public String getPhrases(String contentLanguage) {
		return exec(new HttpGet(BASE_URL + "/phrases/" + enc(contentLanguage) + notificationUrl));
	}

	public String getAllUsers() {
		return exec(new HttpGet(BASE_URL + "/users" + notificationUrl));
	}

	public String createUser() {
		return exec(new HttpPost(BASE_URL + "/users" + notificationUrl));
	}

	public String checkUserExists(String userId) {
		return exec(new HttpGet(BASE_URL + "/users/" + enc(userId) + notificationUrl));
	}

	public String deleteUser(String userId) {
		return exec(new HttpDelete(BASE_URL + "/users/" + enc(userId) + notificationUrl));
	}

	public String getGroupsForUser(String userId) {
		return exec(new HttpGet(BASE_URL + "/users/" + enc(userId) + "/groups" + notificationUrl));
	}

	public String getAllGroups() {
		return exec(new HttpGet(BASE_URL + "/groups" + notificationUrl));
	}

	public String getGroup(String groupId) {
		return exec(new HttpGet(BASE_URL + "/groups/" + enc(groupId) + notificationUrl));
	}

	public String groupExists(String groupId) {
		return exec(new HttpGet(BASE_URL + "/groups/" + enc(groupId) + "/exists" + notificationUrl));
	}

	public String createGroup(String description) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("description", description)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/groups" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String addUserToGroup(String groupId, String userId) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("userId", userId)
		    .build();
		HttpPut httpPut = new HttpPut(BASE_URL + "/groups/addUser" + notificationUrl);
		httpPut.setEntity(entity);

		return exec(httpPut);
	}

	public String removeUserFromGroup(String groupId, String userId) {
		String url = BASE_URL + "/groups/removeUser?groupId=" + enc(groupId) + "&userId=" + enc(userId);
		if (!notificationUrl.isEmpty()) {
			url += "&" + notificationUrl.substring(1);
		}
		return exec(new HttpDelete(url));
	}

	public String deleteGroup(String groupId) {
		return exec(new HttpDelete(BASE_URL + "/groups/" + enc(groupId) + notificationUrl));
	}

	public String getAllVoiceEnrollments(String userId) {
		return exec(new HttpGet(BASE_URL + "/enrollments/voice/" + enc(userId) + notificationUrl));
	}

	public String getAllFaceEnrollments(String userId) {
		return exec(new HttpGet(BASE_URL + "/enrollments/face/" + enc(userId) + notificationUrl));
	}

	public String getAllVideoEnrollments(String userId) {
		return exec(new HttpGet(BASE_URL + "/enrollments/video/" + enc(userId) + notificationUrl));
	}

	public String createVoiceEnrollment(String userId, String contentLanguage, String phrase, String recordingPath) {
    return createVoiceEnrollment(userId, contentLanguage, phrase, new File(recordingPath));
  }

	public String createVoiceEnrollment(String userId, String contentLanguage, String phrase, File recording) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/voice" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String createVoiceEnrollmentByUrl(String userId, String contentLanguage, String phrase, String fileUrl) {

			HttpEntity entity = MultipartEntityBuilder
			    .create()
			    .addTextBody("userId", userId)
			    .addTextBody("contentLanguage", contentLanguage)
			    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
			    .addTextBody("fileUrl", fileUrl)
			    .build();
			HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/voice/byUrl" + notificationUrl);
			httpPost.setEntity(entity);

			return exec(httpPost);
	}

	public String createFaceEnrollment(String userId, String videoPath) {
    return createFaceEnrollment(userId, new File(videoPath));
  }

	public String createFaceEnrollment(String userId, File video) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/face" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String createFaceEnrollmentByUrl(String userId, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/face/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String createVideoEnrollment(String userId, String contentLanguage, String phrase, String videoPath) {
    return createVideoEnrollment(userId, contentLanguage, phrase, new File(videoPath));
	}

	public String createVideoEnrollment(String userId, String contentLanguage, String phrase, File video) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/video" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String createVideoEnrollmentByUrl(String userId, String contentLanguage, String phrase, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/video/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String deleteAllEnrollments(String userId) {
		return exec(new HttpDelete(BASE_URL + "/enrollments/" + enc(userId) + "/all" + notificationUrl));
	}

	public String voiceVerification(String userId, String contentLanguage, String phrase, String recordingPath) {
    return voiceVerification(userId, contentLanguage, phrase, new File(recordingPath));
  }

	public String voiceVerification(String userId, String contentLanguage, String phrase, File recording) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/voice" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String voiceVerificationByUrl(String userId, String contentLanguage, String phrase, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/voice/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String faceVerification(String userId, String videoPath) {
		return faceVerification(userId, new File(videoPath));
	}

	public String faceVerification(String userId, File video) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/face" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String faceVerificationByUrl(String userId, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/face/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

  public String videoVerification(String userId, String contentLanguage, String phrase, String videoPath) {
    return videoVerification(userId, contentLanguage, phrase, new File(videoPath));
  }

	public String videoVerification(String userId, String contentLanguage, String phrase, File video) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/video" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String videoVerificationByUrl(String userId, String contentLanguage, String phrase, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/video/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String voiceIdentification(String groupId, String contentLanguage, String phrase, String recordingPath) {
    return voiceIdentification(groupId, contentLanguage, phrase, new File(recordingPath));
  }

	public String voiceIdentification(String groupId, String contentLanguage, String phrase, File recording) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/voice" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String voiceIdentificationByUrl(String groupId, String contentLanguage, String phrase, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/voice/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String faceIdentification(String groupId, String videoPath) {
		return faceIdentification(groupId, new File(videoPath));
	}

	public String faceIdentification(String groupId, File video) {

		HttpEntity entity = MultipartEntityBuilder
				.create()
				.addTextBody("groupId", groupId)
				.addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
				.build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/face" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String faceIdentificationByUrl(String groupId, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
				.create()
				.addTextBody("groupId", groupId)
				.addTextBody("fileUrl", fileUrl)
				.build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/face/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String videoIdentification(String groupId, String contentLanguage, String phrase, String videoPath) {
    return videoIdentification(groupId, contentLanguage, phrase, new File(videoPath));
	}

	public String videoIdentification(String groupId, String contentLanguage, String phrase, File video) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/video" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String videoIdentificationByUrl(String groupId, String contentLanguage, String phrase, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("phrase", phrase, ContentType.create("text/plain", Charset.forName("UTF-8")))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/video/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String createUserToken(String userId, int secondsToTimeout) {
		return exec(new HttpPost(BASE_URL + "/users/" + enc(userId) + "/token?timeOut=" + Integer.toString(secondsToTimeout)));
	}

	public String expireUserTokens(String userId) {
		return exec(new HttpPost(BASE_URL + "/users/" + enc(userId) + "/expireTokens" + notificationUrl));
	}

	public String createManagedSubAccount(String firstName, String lastName, String email, String password, String contentLanguage) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
			.addTextBody("firstName", firstName)
		    .addTextBody("lastName", lastName)
		    .addTextBody("email", email)
		    .addTextBody("password", password)
		    .addTextBody("contentLanguage", contentLanguage)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/subaccount/managed" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}

	public String createUnmanagedSubAccount(String firstName, String lastName, String email, String password, String contentLanguage) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
			.addTextBody("firstName", firstName)
		    .addTextBody("lastName", lastName)
		    .addTextBody("email", email)
		    .addTextBody("password", password)
		    .addTextBody("contentLanguage", contentLanguage)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/subaccount/unmanaged" + notificationUrl);
		httpPost.setEntity(entity);

		return exec(httpPost);
	}


	public String regenerateSubAccountAPIToken(String subAccountAPIKey) {
		return exec(new HttpPost(BASE_URL + "/subaccount/" + enc(subAccountAPIKey) + notificationUrl));
	}

	public String deleteSubAccount(String subAccountAPIKey) {
		return exec(new HttpDelete(BASE_URL + "/subaccount/" + enc(subAccountAPIKey) + notificationUrl));
	}

}
