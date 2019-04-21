package test.java;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.json.*;
import org.apache.commons.io.FileUtils;
import main.java.VoiceIt2;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestVoiceIt2 {

	private String apiKey = System.getenv("VIAPIKEY");
	private String apiTok = System.getenv("VIAPITOKEN");

	private String phrase = "never forget tomorrow is a new day";

  void downloadFile(String source, String destination) {
    try {
      FileUtils.copyURLToFile( new URL(source), new File(destination) );
    } catch (MalformedURLException e) {
      System.err.println(e);
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  void deleteFile(String path) {
    File file = new File(path);

    if(file.delete())
    {
        System.out.println("File " + path + " deleted successfully");
    }
    else
    {
        System.err.println("Failed to delete the file");
    }
  }

  int getStatus(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getInt("status");
  }

  String getResponseCode(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getString("responseCode");
  }

  String getUserId(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getString("userId");
  }

  String getGroupId(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getString("groupId");
  }

  int getEnrollmentId(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getInt("id");
  }

  int getFaceEnrollmentId(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getInt("faceEnrollmentId");
  }


  @Test
  void TestWebhook() {
    VoiceIt2 myVoiceIt = new VoiceIt2(apiKey, apiTok);

    if (System.getenv("BOXFUSE_ENV").equals("voiceittest")) {
      try {
        Files.write(Paths.get(System.getenv("HOME") + "/platformVersion"), VoiceIt2.VERSION.getBytes());
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    try {
      myVoiceIt.addNotificationUrl("https://voiceit.io");
    } catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assertEquals("?notificationURL=https%3A%2F%2Fvoiceit.io", myVoiceIt.getNotificationUrl());
    myVoiceIt.removeNotificationUrl();
		assertEquals("", myVoiceIt.getNotificationUrl());
  }

  @Test
  void TestBasics() {
    VoiceIt2 myVoiceIt = new VoiceIt2(apiKey, apiTok);

    String ret = "";

		// Get All Phrases
		ret = myVoiceIt.getPhrases("en-US");
		assertEquals(200, getStatus(ret));
		assertEquals("SUCC", getResponseCode(ret));

    // Create User
    ret = myVoiceIt.createUser();
    String userId = getUserId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Get All Users
    ret = myVoiceIt.getAllUsers();
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Check if a Specific User Exists
    ret = myVoiceIt.checkUserExists(userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Create Group
    ret = myVoiceIt.createGroup("Sample Group Description");
    String groupId = getGroupId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Get All Groups
    ret = myVoiceIt.getGroupsForUser(userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Add User to Group
    ret = myVoiceIt.addUserToGroup(groupId, userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Get Groups for User
    ret = myVoiceIt.getGroupsForUser(userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Get a Specific Group
    ret = myVoiceIt.getGroup(groupId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Check if Group Exists
    ret = myVoiceIt.groupExists(groupId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Remove User from Group
    ret = myVoiceIt.removeUserFromGroup(groupId, userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Create User Token
    ret = myVoiceIt.createUserToken(userId, 5);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Expire User Token
    ret = myVoiceIt.expireUserTokens(userId);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Delete Group
    ret = myVoiceIt.deleteGroup(groupId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

		// Delete All Enrollments
    ret = myVoiceIt.deleteAllEnrollments(userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Delete User
    ret = myVoiceIt.deleteUser(userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

  }


  @Test
  void TestVideo() {
    VoiceIt2 myVoiceIt = new VoiceIt2(apiKey, apiTok);

    String ret = "";
    ret = myVoiceIt.createUser();
    String userId1 = getUserId(ret);
    ret = myVoiceIt.createUser();
    String userId2 = getUserId(ret);
    ret = myVoiceIt.createGroup("Sample Group Description");
    String groupId = getGroupId(ret);
    myVoiceIt.addUserToGroup(groupId, userId1);
    myVoiceIt.addUserToGroup(groupId, userId2);

    // Create Video Enrollments
    downloadFile("https://drive.voiceit.io/files/videoEnrollmentB1.mov", "./videoEnrollmentB1.mov");
    downloadFile("https://drive.voiceit.io/files/videoEnrollmentB2.mov", "./videoEnrollmentB2.mov");
    downloadFile("https://drive.voiceit.io/files/videoEnrollmentB3.mov", "./videoEnrollmentB3.mov");
    downloadFile("https://drive.voiceit.io/files/videoVerificationB1.mov", "./videoVerificationB1.mov");
    downloadFile("https://drive.voiceit.io/files/videoEnrollmentC1.mov", "./videoEnrollmentC1video.mov");
    downloadFile("https://drive.voiceit.io/files/videoEnrollmentC2.mov", "./videoEnrollmentC2video.mov");
    downloadFile("https://drive.voiceit.io/files/videoEnrollmentC3.mov", "./videoEnrollmentC3video.mov");

    ret = myVoiceIt.createVideoEnrollment(userId1, "en-US", phrase, "./videoEnrollmentB1.mov");
    int enrollmentId1 = getEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId1, "en-US", phrase, "./videoEnrollmentB2.mov");
    int enrollmentId2 = getEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId1, "en-US", phrase, "./videoEnrollmentB3.mov");
    int enrollmentId3 = getEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

	  ret = myVoiceIt.getAllVideoEnrollments(userId1);
	  assertEquals(200, getStatus(ret));
	  assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId2, "en-US", phrase, new File("./videoEnrollmentC1video.mov"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId2, "en-US", phrase, new File("./videoEnrollmentC2video.mov"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId2, "en-US", phrase, new File("./videoEnrollmentC3video.mov"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Video Verification
    ret = myVoiceIt.videoVerification(userId1, "en-US", phrase, "./videoVerificationB1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Video Identification
    ret = myVoiceIt.videoIdentification(groupId, "en-US", phrase, "./videoVerificationB1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    ret = myVoiceIt.videoIdentification(groupId, "en-US", phrase, new File("./videoVerificationB1.mov"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    // Delete Individual Video Enrollments
    ret = myVoiceIt.deleteVideoEnrollment(userId1, enrollmentId1);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteVideoEnrollment(userId1, enrollmentId2);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteVideoEnrollment(userId1, enrollmentId3);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Delete All Enrollments
    ret = myVoiceIt.deleteAllVideoEnrollments(userId2);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Reset for ...ByUrl operations
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);
    ret = myVoiceIt.createUser();
    userId1 = getUserId(ret);
    ret = myVoiceIt.createUser();
    userId2 = getUserId(ret);
    ret = myVoiceIt.createGroup("Sample Group Description");
    groupId = getGroupId(ret);
    myVoiceIt.addUserToGroup(groupId, userId1);
    myVoiceIt.addUserToGroup(groupId, userId2);

    // Video Enrollments By URL

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId1, "en-US", phrase, "https://drive.voiceit.io/files/videoEnrollmentB1.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId1, "en-US", phrase, "https://drive.voiceit.io/files/videoEnrollmentB2.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId1, "en-US", phrase, "https://drive.voiceit.io/files/videoEnrollmentB3.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId2, "en-US", phrase, "https://drive.voiceit.io/files/videoEnrollmentC1.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId2, "en-US", phrase, "https://drive.voiceit.io/files/videoEnrollmentC2.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId2, "en-US", phrase, "https://drive.voiceit.io/files/videoEnrollmentC3.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));


    // Video Verification By URL
    ret = myVoiceIt.videoVerificationByUrl(userId1, "en-US", phrase, "https://drive.voiceit.io/files/videoVerificationB1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Video Identification By URL
    ret = myVoiceIt.videoIdentificationByUrl(groupId, "en-US", phrase, "https://drive.voiceit.io/files/videoVerificationB1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    myVoiceIt.deleteAllVideoEnrollments(userId1);
    myVoiceIt.deleteAllVideoEnrollments(userId2);
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);

    deleteFile("./videoEnrollmentB1.mov");
    deleteFile("./videoEnrollmentB2.mov");
    deleteFile("./videoEnrollmentB3.mov");
    deleteFile("./videoEnrollmentC1video.mov");
    deleteFile("./videoEnrollmentC2video.mov");
    deleteFile("./videoEnrollmentC3video.mov");
    deleteFile("./videoVerificationB1.mov");

  }


  @Test
  void TestVoice() {
    VoiceIt2 myVoiceIt = new VoiceIt2(apiKey, apiTok);

    String ret = "";
    ret = myVoiceIt.createUser();
    String userId1 = getUserId(ret);
    ret = myVoiceIt.createUser();
    String userId2 = getUserId(ret);
    ret = myVoiceIt.createGroup("Sample Group Description");
    String groupId = getGroupId(ret);
    myVoiceIt.addUserToGroup(groupId, userId1);
    myVoiceIt.addUserToGroup(groupId, userId2);

    // Create Voice Enrollments
    downloadFile("https://drive.voiceit.io/files/enrollmentA1.wav", "./enrollmentA1.wav");
    downloadFile("https://drive.voiceit.io/files/enrollmentA2.wav", "./enrollmentA2.wav");
    downloadFile("https://drive.voiceit.io/files/enrollmentA3.wav", "./enrollmentA3.wav");
    downloadFile("https://drive.voiceit.io/files/verificationA1.wav", "./verificationA1.wav");
    downloadFile("https://drive.voiceit.io/files/enrollmentC1.wav", "./enrollmentC1.wav");
    downloadFile("https://drive.voiceit.io/files/enrollmentC2.wav", "./enrollmentC2.wav");
    downloadFile("https://drive.voiceit.io/files/enrollmentC3.wav", "./enrollmentC3.wav");

    ret = myVoiceIt.createVoiceEnrollment(userId1, "en-US", phrase, "./enrollmentA1.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId1, "en-US", phrase, "./enrollmentA2.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId1, "en-US", phrase, "./enrollmentA3.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.getAllVoiceEnrollments(userId1);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId2, "en-US", phrase, new File("./enrollmentC1.wav"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId2, "en-US", phrase, new File("./enrollmentC2.wav"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId2, "en-US", phrase, new File("./enrollmentC3.wav"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Verification
    ret = myVoiceIt.voiceVerification(userId1, "en-US", phrase, "./verificationA1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Identification
    ret = myVoiceIt.voiceIdentification(groupId, "en-US", phrase, "./verificationA1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    // Reset for ...ByUrl operations
    myVoiceIt.deleteAllVoiceEnrollments(userId1);
    myVoiceIt.deleteAllVoiceEnrollments(userId2);
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);
    ret = myVoiceIt.createUser();
    userId1 = getUserId(ret);
    ret = myVoiceIt.createUser();
    userId2 = getUserId(ret);
    ret = myVoiceIt.createGroup("Sample Group Description");
    groupId = getGroupId(ret);
    myVoiceIt.addUserToGroup(groupId, userId1);
    myVoiceIt.addUserToGroup(groupId, userId2);

    // Create Voice Enrollments By URL
    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId1, "en-US", phrase, "https://drive.voiceit.io/files/enrollmentA1.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId1, "en-US", phrase, "https://drive.voiceit.io/files/enrollmentA2.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId1, "en-US", phrase, "https://drive.voiceit.io/files/enrollmentA3.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId2, "en-US", phrase, "https://drive.voiceit.io/files/enrollmentC1.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId2, "en-US", phrase, "https://drive.voiceit.io/files/enrollmentC2.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId2, "en-US", phrase, "https://drive.voiceit.io/files/enrollmentC3.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Verification
    ret = myVoiceIt.voiceVerificationByUrl(userId1, "en-US", phrase, "https://drive.voiceit.io/files/verificationA1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Identification
    ret = myVoiceIt.voiceIdentificationByUrl(groupId, "en-US", phrase, "https://drive.voiceit.io/files/verificationA1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    myVoiceIt.deleteAllVoiceEnrollments(userId1);
    myVoiceIt.deleteAllVoiceEnrollments(userId2);
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);


    deleteFile("./enrollmentA1.wav");
    deleteFile("./enrollmentA2.wav");
    deleteFile("./enrollmentA3.wav");
    deleteFile("./verificationA1.wav");
    deleteFile("./enrollmentC1.wav");
    deleteFile("./enrollmentC2.wav");
    deleteFile("./enrollmentC3.wav");
  }


  @Test
  void TestFace() {
    VoiceIt2 myVoiceIt = new VoiceIt2(apiKey, apiTok);

    String ret = "";
    ret = myVoiceIt.createUser();
    String userId1 = getUserId(ret);
    ret = myVoiceIt.createUser();
    String userId2 = getUserId(ret);
    ret = myVoiceIt.createGroup("Sample Group Description");
    String groupId = getGroupId(ret);
    myVoiceIt.addUserToGroup(groupId, userId1);
    myVoiceIt.addUserToGroup(groupId, userId2);

    // Create Face Enrollments
    downloadFile("https://drive.voiceit.io/files/faceEnrollmentB1.mp4", "./faceEnrollmentB1.mp4");
    downloadFile("https://drive.voiceit.io/files/faceEnrollmentB2.mp4", "./faceEnrollmentB2.mp4");
    downloadFile("https://drive.voiceit.io/files/faceEnrollmentB3.mp4", "./faceEnrollmentB3.mp4");
    downloadFile("https://drive.voiceit.io/files/videoEnrollmentC1.mov", "./videoEnrollmentC1face.mov");
    downloadFile("https://drive.voiceit.io/files/videoEnrollmentC2.mov", "./videoEnrollmentC2face.mov");
    downloadFile("https://drive.voiceit.io/files/videoEnrollmentC3.mov", "./videoEnrollmentC3face.mov");
    downloadFile("https://drive.voiceit.io/files/faceVerificationB1.mp4", "./faceVerificationB1.mp4");

    ret = myVoiceIt.createFaceEnrollment(userId1, "./faceEnrollmentB1.mp4");
    int faceEnrollmentId1 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId1, "./faceEnrollmentB2.mp4");
    int faceEnrollmentId2 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId1, "./faceEnrollmentB3.mp4");
    int faceEnrollmentId3 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId2, "./videoEnrollmentC1face.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId2, "./videoEnrollmentC2face.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId2, "./videoEnrollmentC3face.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.getAllFaceEnrollments(userId1);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Face Verification
    ret = myVoiceIt.faceVerification(userId1, "./faceVerificationB1.mp4");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.faceVerification(userId1, new File("./faceVerificationB1.mp4"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Face Identification
    ret = myVoiceIt.faceIdentification(groupId, "./faceVerificationB1.mp4");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(groupId, getGroupId(ret));

    // Delete Face Enrollments Individually
    ret = myVoiceIt.deleteFaceEnrollment(userId1, faceEnrollmentId1);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteFaceEnrollment(userId1, faceEnrollmentId2);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteFaceEnrollment(userId1, faceEnrollmentId3);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteAllFaceEnrollments(userId2);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);

    // By Url

  ret = "";
	ret = myVoiceIt.createUser();
	userId1 = getUserId(ret);
	ret = myVoiceIt.createUser();
	userId2 = getUserId(ret);
	ret = myVoiceIt.createGroup("Sample Group Description");
	groupId = getGroupId(ret);
	myVoiceIt.addUserToGroup(groupId, userId1);
	myVoiceIt.addUserToGroup(groupId, userId2);

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId1, "https://drive.voiceit.io/files/faceEnrollmentB1.mp4");
    faceEnrollmentId1 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId1, "https://drive.voiceit.io/files/faceEnrollmentB2.mp4");
    faceEnrollmentId2 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId1, "https://drive.voiceit.io/files/faceEnrollmentB3.mp4");
    faceEnrollmentId3 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId2, "https://drive.voiceit.io/files/videoEnrollmentC1.mov");
    faceEnrollmentId1 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId2, "https://drive.voiceit.io/files/videoEnrollmentC2.mov");
    faceEnrollmentId2 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId2, "https://drive.voiceit.io/files/videoEnrollmentC3.mov");
    faceEnrollmentId3 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Face Verification
    ret = myVoiceIt.faceVerificationByUrl(userId1, "https://drive.voiceit.io/files/faceVerificationB1.mp4");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Face Identification
    ret = myVoiceIt.faceIdentificationByUrl(groupId, "https://drive.voiceit.io/files/faceVerificationB1.mp4");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(groupId, getGroupId(ret));

    // Delete Face Enrollments Individually
    ret = myVoiceIt.deleteFaceEnrollment(userId1, faceEnrollmentId1);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteFaceEnrollment(userId1, faceEnrollmentId2);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteFaceEnrollment(userId1, faceEnrollmentId3);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteAllFaceEnrollments(userId2);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);

    deleteFile("./faceEnrollmentB1.mp4");
    deleteFile("./faceEnrollmentB2.mp4");
    deleteFile("./faceEnrollmentB3.mp4");
    deleteFile("./videoEnrollmentC1face.mov");
    deleteFile("./videoEnrollmentC2face.mov");
    deleteFile("./videoEnrollmentC3face.mov");
    deleteFile("./faceVerificationB1.mp4");

  }
}
