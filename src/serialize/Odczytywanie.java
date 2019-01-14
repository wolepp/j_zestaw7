package serialize;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Odczytywanie {
    public static void main(String[] args) {

        Circular<String> circular;
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream("bufor.ser"))) {
            circular = (Circular) input.readObject();
            System.out.println(circular.take());
            System.out.println(circular.poll());
            System.out.println(circular.remove());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
