package cz.muni.fi.pv168.project.storage.sql.db.actions;

public class ResetDatabase {
    public static void main(String[] args) {
        DestroyDatabase.destroyDatabase();
        CreateDatabase.createDatabase();
    }
}
