package at.ac.hcw.alertaprice;

public class User {


    private static final User instance = new User();
    private String name;
    private String email;
    private String filename;

    public User() {};
    public User(String name, String email){
        this.name = name;
        this.email = email;
        this.filename = name + "_alerts.json";
    }

    public static User getInstance() {
        return instance;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email;}
}
