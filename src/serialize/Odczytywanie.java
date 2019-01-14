package serialize;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Odczytywanie {
    public static void main(String[] args) {

        Circular<String> circular;
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream("bufor.ser"))) {
            circular = (Circular) input.readObject();
            for (String string : circular)
                System.out.println(string);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
