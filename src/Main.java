import java.util.ArrayList;
import yms.com.*;

public class Main {

    public static void main(String[] args) {
       Contacts con = Contacts.getInstance();
       con.programRuning();
       con.writeDataToFile();
    }
}
