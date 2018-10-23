package test.java;

import java.net.*;
import java.io.*;
import org.junit.jupiter.api.Test;
import org.json.*;
import org.apache.commons.io.FileUtils;
import main.java.VoiceIt2;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestVoiceIt2 {

	private String apiKey = System.getenv("VIAPIKEY");
	private String apiTok = System.getenv("VIAPITOKEN");

	private String phrase = "Never forget tomorrow is a new day";

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
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov", "./videoEnrollmentArmaan1.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan2.mov", "./videoEnrollmentArmaan2.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan3.mov", "./videoEnrollmentArmaan3.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoVerificationArmaan1.mov", "./videoVerificationArmaan1.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen1.mov", "./videoEnrollmentStephen1.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen2.mov", "./videoEnrollmentStephen2.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen3.mov", "./videoEnrollmentStephen3.mov");

    ret = myVoiceIt.createVideoEnrollment(userId1, "en-US", phrase, "./videoEnrollmentArmaan1.mov");
    int enrollmentId1 = getEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId1, "en-US", phrase, "./videoEnrollmentArmaan2.mov");
    int enrollmentId2 = getEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId1, "en-US", phrase, "./videoEnrollmentArmaan3.mov");
    int enrollmentId3 = getEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

	  ret = myVoiceIt.getAllVideoEnrollments(userId1);
	  assertEquals(200, getStatus(ret));
	  assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId2, "en-US", phrase, new File("./videoEnrollmentStephen1.mov"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId2, "en-US", phrase, new File("./videoEnrollmentStephen2.mov"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId2, "en-US", phrase, new File("./videoEnrollmentStephen3.mov"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Video Verification
    ret = myVoiceIt.videoVerification(userId1, "en-US", phrase, "./videoVerificationArmaan1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.videoVerification(userId1, "en-US", phrase, new File("./videoVerificationArmaan1.mov"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Video Identification
    ret = myVoiceIt.videoIdentification(groupId, "en-US", phrase, "./videoVerificationArmaan1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    ret = myVoiceIt.videoIdentification(groupId, "en-US", phrase, new File("./videoVerificationArmaan1.mov"));
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

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId1, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId1, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan2.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId1, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan3.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId2, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen1.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId2, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen2.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId2, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen3.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));


    // Video Verification By URL
    ret = myVoiceIt.videoVerificationByUrl(userId1, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoVerificationArmaan1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Video Identification By URL
    ret = myVoiceIt.videoIdentificationByUrl(groupId, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoVerificationArmaan1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    myVoiceIt.deleteAllVideoEnrollments(userId1);
    myVoiceIt.deleteAllVideoEnrollments(userId2);
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);

    deleteFile("./videoEnrollmentArmaan1.mov");
    deleteFile("./videoEnrollmentArmaan2.mov");
    deleteFile("./videoEnrollmentArmaan3.mov");
    deleteFile("./videoVerificationArmaan1.mov");
    deleteFile("./videoEnrollmentStephen1.mov");
    deleteFile("./videoEnrollmentStephen2.mov");
    deleteFile("./videoEnrollmentStephen3.mov");

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
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan1.wav", "./enrollmentArmaan1.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan2.wav", "./enrollmentArmaan2.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan3.wav", "./enrollmentArmaan3.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/verificationArmaan1.wav", "./verificationArmaan1.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen1.wav", "./enrollmentStephen1.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen2.wav", "./enrollmentStephen2.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen3.wav", "./enrollmentStephen3.wav");

    ret = myVoiceIt.createVoiceEnrollment(userId1, "en-US", phrase, "./enrollmentArmaan1.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId1, "en-US", phrase, "./enrollmentArmaan2.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId1, "en-US", phrase, "./enrollmentArmaan3.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.getAllVoiceEnrollments(userId1);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId2, "en-US", phrase, new File("./enrollmentStephen1.wav"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId2, "en-US", phrase, new File("./enrollmentStephen2.wav"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId2, "en-US", phrase, new File("./enrollmentStephen3.wav"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Verification
    ret = myVoiceIt.voiceVerification(userId1, "en-US", phrase, "./verificationArmaan1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.voiceVerification(userId1, "en-US", phrase, new File("./verificationArmaan1.wav"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Identification
    ret = myVoiceIt.voiceIdentification(groupId, "en-US", phrase, "./verificationArmaan1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    ret = myVoiceIt.voiceIdentification(groupId, "en-US", phrase, new File("./verificationArmaan1.wav"));
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
    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId1, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan1.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId1, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan2.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId1, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan3.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId2, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen1.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId2, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen2.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId2, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen3.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Verification
    ret = myVoiceIt.voiceVerificationByUrl(userId1, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/verificationArmaan1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Identification
    ret = myVoiceIt.voiceIdentificationByUrl(groupId, "en-US", phrase, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/verificationArmaan1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    myVoiceIt.deleteAllVoiceEnrollments(userId1);
    myVoiceIt.deleteAllVoiceEnrollments(userId2);
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);


    deleteFile("./enrollmentArmaan1.wav");
    deleteFile("./enrollmentArmaan2.wav");
    deleteFile("./enrollmentArmaan3.wav");
    deleteFile("./verificationArmaan1.wav");
    deleteFile("./enrollmentStephen1.wav");
    deleteFile("./enrollmentStephen2.wav");
    deleteFile("./enrollmentStephen3.wav");
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
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan1.mp4", "./faceEnrollmentArmaan1.mp4");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan2.mp4", "./faceEnrollmentArmaan2.mp4");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan3.mp4", "./faceEnrollmentArmaan3.mp4");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceVerificationArmaan1.mp4", "./faceVerificationArmaan1.mp4");

    ret = myVoiceIt.createFaceEnrollment(userId1, "./faceEnrollmentArmaan1.mp4");
    int faceEnrollmentId1 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId1, "./faceEnrollmentArmaan2.mp4");
    int faceEnrollmentId2 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId1, "./faceEnrollmentArmaan3.mp4");
    int faceEnrollmentId3 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId2, "./videoEnrollmentStephen1.mov");
    System.out.println(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId2, "./videoEnrollmentStephen2.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId2, "./videoEnrollmentStephen3.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.getAllFaceEnrollments(userId1);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Face Verification
    ret = myVoiceIt.faceVerification(userId1, "./faceVerificationArmaan1.mp4");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.faceVerification(userId1, new File("./faceVerificationArmaan1.mp4"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Face Identification
    ret = myVoiceIt.faceIdentification(groupId, "./faceVerificationArmaan1.mp4");
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

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId1, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan1.mp4");
    faceEnrollmentId1 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId1, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan2.mp4");
    faceEnrollmentId2 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId1, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan3.mp4");
    faceEnrollmentId3 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId2, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen1.mov");
    faceEnrollmentId1 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId2, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen2.mov");
    faceEnrollmentId2 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollmentByUrl(userId2, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen3.mov");
    faceEnrollmentId3 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Face Verification
    ret = myVoiceIt.faceVerificationByUrl(userId1, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceVerificationArmaan1.mp4");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Face Identification
    ret = myVoiceIt.faceIdentificationByUrl(groupId, "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceVerificationArmaan1.mp4");
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

    deleteFile("./faceEnrollmentArmaan1.mp4");
    deleteFile("./faceEnrollmentArmaan2.mp4");
    deleteFile("./faceEnrollmentArmaan3.mp4");
    deleteFile("./faceVerificationArmaan1.mp4");

  }
}
