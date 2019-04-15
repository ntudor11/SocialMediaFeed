

public class DBTest {
    
    public static void main(String[] args) {
        
        DBInterface dbInterface = DBInterface.getDBInterface();

        dbInterface.connect();
        System.out.println("should be connected now?");
        dbInterface.disConnect();

    }
}