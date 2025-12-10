package at.ac.hcw.alertaprice;

import java.util.Objects;

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

    public String getFilename(){ return filename; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email;}

   @Override
   public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return name.equals(user.name) && email.equals(user.email);
        }
        @Override
        public int hashCode() {
            return Objects.hash(name, email);
        }

}
