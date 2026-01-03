package at.ac.hcw.alertaprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//ist angelget um sicherzustellen wer sich einloggt UND als vorlage zu v2 wo es mehrere user per haushalt sein kann.
public final class UserRepository {

    private static final Path USER_FILE = Paths.get("user.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private UserRepository() {}
    //liest user.json und gibt User zurück (oder null, wenn keine Datei/leer)
    public static User load()
    {
        try {
            if (!Files.exists(USER_FILE) || Files.size(USER_FILE) == 0) return null;
            try (Reader r = Files.newBufferedReader(USER_FILE)) {
                return GSON.fromJson(r, User.class);
            }
        } catch (Exception e) {
            System.err.println("Failed to load user.json: " + e.getMessage());
            return null;
        }
    }
    //überschreibt user.json mit dem aktuellen User (Profil speichern)
    public static void save(User user) throws Exception {
        if (user == null) throw new IllegalArgumentException("user is null");
        try (Writer w = Files.newBufferedWriter(USER_FILE)) {
            GSON.toJson(user, w);
        }
    }
    // löscht user.json NUR wenn der Nutzer explizit "Profil löschen" klickt (NICHT beim Programm schließen)
    public static void delete() throws Exception {
        Files.deleteIfExists(USER_FILE);
    }
}