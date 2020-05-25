package main.java;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class VoiceIt2 {

	private static String BASE_URL = "https://api.voiceit.io";
	private String notificationUrl = "";
	private HttpClient httpClient;
	public static final String VERSION = "1.6.0";

	public VoiceIt2(String apiKey, String apiToken){
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
			setup(clientBuilder, apiKey, apiToken);
			httpClient = clientBuilder.build();
	}

	public VoiceIt2(String apiKey, String apiToken, String customBaseURL) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException{
		// Setting custom host URL and SSL Context
		BASE_URL = customBaseURL;
    	SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new X509TrustManager[] { new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0];}}}, new SecureRandom());
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) { return true; }
		};
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setSSLContext(sslContext).setSSLHostnameVerifier(hostnameVerifier);
		setup(clientBuilder, apiKey, apiToken);
		httpClient = clientBuilder.build();
	}

	private void setup(HttpClientBuilder clientBuilder, String apiKey, String apiToken) {
	      CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	      credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(apiKey, apiToken));
	      clientBuilder
	      .setDefaultCredentialsProvider(credentialsProvider)
	      .setDefaultHeaders(Arrays.asList(new BasicHeader("platformId", "29"), new BasicHeader("platformVersion", VERSION)));
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
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/phrases/" + contentLanguage + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getAllUsers() {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/users" + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String createUser() {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpPost(BASE_URL + "/users" + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String checkUserExists(String userId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/users/" + userId + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String deleteUser(String userId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/users/" + userId + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getGroupsForUser(String userId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/users/" + userId + "/groups" + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getAllGroups() {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/groups" + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getGroup(String groupId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/groups/" + groupId + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String groupExists(String groupId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/groups/" + groupId + "/exists" + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String createGroup(String description) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("description", description)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/groups" + notificationUrl);
		httpPost.setEntity(entity);

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String addUserToGroup(String groupId, String userId) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("userId", userId)
		    .build();
		HttpPut httpPut = new HttpPut(BASE_URL + "/groups/addUser" + notificationUrl);
		httpPut.setEntity(entity);

		try {
			return EntityUtils.toString(httpClient.execute(httpPut).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String removeUserFromGroup(String groupId, String userId) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("userId", userId)
		    .build();
		HttpPut httpPut = new HttpPut(BASE_URL + "/groups/removeUser" + notificationUrl);
		httpPut.setEntity(entity);

		try {
			return EntityUtils.toString(httpClient.execute(httpPut).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String deleteGroup(String groupId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/groups/" + groupId + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getAllVoiceEnrollments(String userId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/enrollments/voice/" + userId + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getAllFaceEnrollments(String userId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/enrollments/face/" + userId + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getAllVideoEnrollments(String userId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/enrollments/video/" + userId + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

			try {
				return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
			} catch (Exception e) {
				return e.getMessage();
			}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String createFaceEnrollmentByUrl(String userId, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/face/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String deleteAllEnrollments(String userId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/" + userId + "/all" + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String faceVerificationByUrl(String userId, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/face/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String faceIdentificationByUrl(String groupId, String fileUrl) {

		HttpEntity entity = MultipartEntityBuilder
				.create()
				.addTextBody("groupId", groupId)
				.addTextBody("fileUrl", fileUrl)
				.build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/face/byUrl" + notificationUrl);
		httpPost.setEntity(entity);

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String createUserToken(String userId, int secondsToTimeout) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpPost(BASE_URL + "/users/" + userId + "/token?timeOut" + Integer.toString(secondsToTimeout))).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String expireUserTokens(String userId) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpPost(BASE_URL + "/users/" + userId + "/expireTokens" + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
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

		try {
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String switchSubAccountType(String subAccountAPIKey) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpPost(BASE_URL + "/subaccount/" + subAccountAPIKey + "/switchType" + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String regenerateSubAccountAPIToken(String subAccountAPIKey) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpPost(BASE_URL + "/subaccount/" + subAccountAPIKey + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String deleteSubAccount(String subAccountAPIKey) {
		try {
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/subaccount/" + subAccountAPIKey + notificationUrl)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
