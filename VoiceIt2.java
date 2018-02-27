import java.io.File;

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
import org.apache.http.util.EntityUtils;

public class VoiceIt2 {
	
	private final String BASE_URL = "https://api.voiceit.io";
	private HttpClient httpClient;
	
	public VoiceIt2(String apiKey, String apiToken){
	        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	        credentialsProvider.setCredentials(AuthScope.ANY, 
	            new UsernamePasswordCredentials(apiKey, apiToken));
	         httpClient =  HttpClientBuilder.create()
	        		 .setDefaultCredentialsProvider(credentialsProvider).build();
	}
	
	public String getAllUsers() {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/users")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createUser() {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpPost(BASE_URL + "/users")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getUser(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/users/" + userId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteUser(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/users/" + userId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getGroupsForUser(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/users/" + userId + "/groups")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getAllEnrollmentsForUser(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/enrollments/" + userId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteAllEnrollmentsForUser(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/" + userId + "/all")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getFaceFaceEnrollmentsForUser(String userId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/enrollments/face/" + userId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createVoiceEnrollment(String userId, String contentLanguage, File recording) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments");
		httpPost.setEntity(entity);

		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createVoiceEnrollmentByUrl(String userId, String contentLanguage, String fileUrl) {
			
			HttpEntity entity = MultipartEntityBuilder
			    .create()
			    .addTextBody("userId", userId)
			    .addTextBody("contentLanguage", contentLanguage)
			    .addTextBody("fileUrl", fileUrl)
			    .build();
			HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/byUrl");
			httpPost.setEntity(entity);
			
			try {		
				return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
			} catch (Exception e) {
				return e.getMessage();
			}	
	}

	public String createFaceEnrollment(String userId, File video) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/face");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createFaceEnrollment(String userId, File video, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/face");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}

	public String createVideoEnrollment(String userId, String contentLanguage, File video) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/video");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createVideoEnrollment(String userId, String contentLanguage, File video, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/video");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createVideoEnrollmentByUrl(String userId, String contentLanguage, String fileUrl) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/video/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createVideoEnrollmentByUrl(String userId, String contentLanguage, String fileUrl, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/enrollments/video/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteFaceEnrollment(String userId, String faceEnrollmentId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/face/" + userId + "/" + faceEnrollmentId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String deleteEnrollmentForUser(String userId, String enrollmentId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpDelete(BASE_URL + "/enrollments/" + userId + "/" + enrollmentId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getAllGroups() {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/groups")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String getGroup(String groupId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/groups/" + groupId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String groupExists(String groupId) {
		try {		
			return EntityUtils.toString(httpClient.execute(
					new HttpGet(BASE_URL + "/groups/" + groupId + "/exists")).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String createGroup(String description) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("description", description)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/groups");
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
		HttpPut httpPut = new HttpPut(BASE_URL + "/groups/addUser");
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
		HttpPut httpPut = new HttpPut(BASE_URL + "/groups/removeUser");
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
					new HttpDelete(BASE_URL + "/groups/" + groupId)).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String voiceVerification(String userId, String contentLanguage, File recording) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String voiceVerificationByUrl(String userId, String contentLanguage, String fileUrl) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String faceVerification(String userId, File video) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/face");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String faceVerification(String userId, File video, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/face");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoVerification(String userId, String contentLanguage, File video) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/video");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoVerification(String userId, String contentLanguage, File video, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/video");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoVerificationByUrl(String userId, String contentLanguage, String fileUrl) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/video/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoVerificationByUrl(String userId, String contentLanguage, String fileUrl, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("userId", userId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/verification/video/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String voiceIdentification(String groupId, String contentLanguage, File recording) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String voiceIdentificationByUrl(String groupId, String contentLanguage, String fileUrl) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoIdentification(String groupId, String contentLanguage, File video) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/video");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoIdentification(String groupId, String contentLanguage, File video , boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addBinaryBody("video", video, ContentType.create("application/octet-stream"), "video")
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/video");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoIdentificationByUrl(String groupId, String contentLanguage, String fileUrl) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/video/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
	
	public String videoIdentificationByUrl(String groupId, String contentLanguage, String fileUrl, boolean doBlinkDetection) {
		
		HttpEntity entity = MultipartEntityBuilder
		    .create()
		    .addTextBody("groupId", groupId)
		    .addTextBody("contentLanguage", contentLanguage)
		    .addTextBody("doBlinkDetection", String.valueOf(doBlinkDetection))
		    .addTextBody("fileUrl", fileUrl)
		    .build();
		HttpPost httpPost = new HttpPost(BASE_URL + "/identification/video/byUrl");
		httpPost.setEntity(entity);
	
		try {		
			return EntityUtils.toString(httpClient.execute(httpPost).getEntity());
		} catch (Exception e) {
			return e.getMessage();
		}		
	}
}
