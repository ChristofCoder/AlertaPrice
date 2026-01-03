package at.ac.hcw.alertaprice;

import java.util.Objects;

public class User {


    private static User instance = new User();
    private String name;
    private String email;


    public User() {};
    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public static User getInstance() {
        return instance;
    }
    public static void setInstance(User user) {
        instance = user;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email;}

//   @Override
//   public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            User user = (User) o;
//            return name.equals(user.name) && email.equals(user.email);
//        }
//        @Override
//        public int hashCode() {
//            return Objects.hash(name, email);
//        }

}
