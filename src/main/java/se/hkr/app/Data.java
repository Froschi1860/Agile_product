package se.hkr.app;


import java.time.LocalDateTime;
import java.util.ArrayList;


public class Data {

    static public ArrayList<User> users = new ArrayList<>();


    public static void insertMood(String moodRating,  User user) {
        int moodNum = Integer.parseInt(moodRating);
        String userId = user.email;
        DatabaseApiInsert.createMoodEntry(DatabaseConnection.getInstance().connect(), moodNum, LocalDateTime.now(), userId);
        DatabaseConnection.getInstance().disconnect();
    }

    public static void insertTension(String tensionRating, User user) {
        int tensionNum = Integer.parseInt(tensionRating);
        String userId = user.email;
        DatabaseApiInsert.createTensionEntry(DatabaseConnection.getInstance().connect(), tensionNum, LocalDateTime.now(), userId);
        DatabaseConnection.getInstance().disconnect();
    }

    public static void insertJournal(String journalText, User user) {
        String userId = user.email;
        DatabaseApiInsert.createJournalEntry(DatabaseConnection.getInstance().connect(), journalText, LocalDateTime.now(), userId);
        DatabaseConnection.getInstance().disconnect();
    }

} 

