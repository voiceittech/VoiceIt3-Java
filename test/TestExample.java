import voiceit.java.VoiceIt3;

public class TestExample {
    public static void main(String[] args) {
        String apiKey = System.getenv("VOICEIT_API_KEY");
        String apiToken = System.getenv("VOICEIT_API_TOKEN");
        if (apiKey == null || apiToken == null) {
            System.out.println("Set VOICEIT_API_KEY and VOICEIT_API_TOKEN");
            System.exit(1);
        }
        VoiceIt3 vi = new VoiceIt3(apiKey, apiToken);
        System.out.println("CreateUser: " + vi.createUser());
        System.out.println("GetAllUsers: " + vi.getAllUsers());
        System.out.println("CreateGroup: " + vi.createGroup("Test Group"));
        System.out.println("GetAllGroups: " + vi.getAllGroups());
        System.out.println("GetPhrases: " + vi.getPhrases("en-US"));
        System.out.println("\nAll API calls completed successfully!");
    }
}
