import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main
{    
    public static void main(String[] args){
        String name = args[0];
        try(BufferedReader br = new BufferedReader(new FileReader(name))){
            String line;
            while ((line=br.readLine()) != null){
                System.out.println(line);
            }
        }catch (IOException error){
            error.printStackTrace();
        }
    }
}
