import voiceit.java.VoiceIt3;
import org.json.JSONObject;

public class TestExample {
    public static void main(String[] args) {
        String ak = System.getenv("VOICEIT_API_KEY");
        String at = System.getenv("VOICEIT_API_TOKEN");
        if (ak == null || at == null) { System.out.println("Set env vars"); System.exit(1); }

        VoiceIt3 vi = new VoiceIt3(ak, at);
        String phrase = "Never forget tomorrow is a new day";
        String td = "../test-data";
        int errors = 0;

        String raw = vi.createUser();
        System.out.println("Raw createUser: " + raw.substring(0, Math.min(100, raw.length())));
        JSONObject r = new JSONObject(raw);
        String userId = r.getString("userId");
        System.out.println("CreateUser: " + r.getString("responseCode"));

        for (int i = 1; i <= 3; i++) {
            raw = vi.createVideoEnrollment(userId, "en-US", phrase, td + "/videoEnrollmentA" + i + ".mov");
            try {
                r = new JSONObject(raw);
                System.out.println("VideoEnrollment" + i + ": " + r.getString("responseCode"));
                if (!r.getString("responseCode").equals("SUCC")) errors++;
            } catch (Exception e) {
                System.out.println("VideoEnrollment" + i + ": FAIL (non-JSON: " + raw.substring(0, Math.min(80, raw.length())) + ")");
                errors++;
            }
        }

        raw = vi.videoVerification(userId, "en-US", phrase, td + "/videoVerificationA1.mov");
        try {
            r = new JSONObject(raw);
            System.out.println("VideoVerification: " + r.getString("responseCode"));
            System.out.println("  Voice: " + r.optDouble("voiceConfidence", 0) + ", Face: " + r.optDouble("faceConfidence", 0));
            if (!r.getString("responseCode").equals("SUCC")) errors++;
        } catch (Exception e) {
            System.out.println("VideoVerification: FAIL (non-JSON: " + raw.substring(0, Math.min(80, raw.length())) + ")");
            errors++;
        }

        vi.deleteAllEnrollments(userId);
        vi.deleteUser(userId);

        if (errors > 0) { System.out.println("\n" + errors + " FAILURES"); System.exit(1); }
        System.out.println("\nAll tests passed!");
    }
}
