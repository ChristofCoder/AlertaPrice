package at.ac.hcw.alertaprice;

public class ID_Saver {

    private static final ID_Saver instance = new ID_Saver();
    private int id;

    public ID_Saver() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public static ID_Saver getInstance() {
        return instance;
    }
}
