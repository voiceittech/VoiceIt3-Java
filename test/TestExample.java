import voiceit.java.VoiceIt3;
import org.json.JSONObject;

public class TestExample {
    public static void main(String[] args) {
        String ak = System.getenv("VOICEIT_API_KEY");
        String at = System.getenv("VOICEIT_API_TOKEN");
        if (ak == null || at == null) { System.out.println("Set env vars"); System.exit(1); }

        VoiceIt3 vi = new VoiceIt3(ak, at);
        String phrase = "never forget tomorrow is a new day";
        String td = "test-data";
        int errors = 0;

        JSONObject r = new JSONObject(vi.createUser());
        String userId = r.getString("userId");
        System.out.println("CreateUser: " + r.getString("responseCode"));

        for (int i = 1; i <= 3; i++) {
            r = new JSONObject(vi.createVideoEnrollment(userId, "en-US", phrase, td + "/videoEnrollmentA" + i + ".mov"));
            System.out.println("VideoEnrollment" + i + ": " + r.getString("responseCode"));
            if (!r.getString("responseCode").equals("SUCC")) errors++;
        }

        r = new JSONObject(vi.videoVerification(userId, "en-US", phrase, td + "/videoVerificationA1.mov"));
        System.out.println("VideoVerification: " + r.getString("responseCode"));
        System.out.println("  Voice: " + r.optDouble("voiceConfidence", 0) + ", Face: " + r.optDouble("faceConfidence", 0));
        if (!r.getString("responseCode").equals("SUCC")) errors++;

        vi.deleteAllEnrollments(userId);
        vi.deleteUser(userId);

        if (errors > 0) { System.out.println("\n" + errors + " FAILURES"); System.exit(1); }
        System.out.println("\nAll tests passed!");
    }
}
