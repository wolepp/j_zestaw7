package serialize;

import java.io.*;

public class Zapisywanie {
    public static void main(String[] args) {

        Circular<String> circular = new Circular<>(4);
        circular.add("Pierwszy string");
        circular.offer("String numer dwa");
        try {
            circular.put("String z metodą put");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("bufor.ser"))) {
            output.writeObject(circular);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
