import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
public class write {
    public static void main (String []args) throws IOException{
          File history = new File("history/history.txt");
        FileWriter fw = new FileWriter(history);
        FileReader fr = new FileReader(history);;
    }
    
}
