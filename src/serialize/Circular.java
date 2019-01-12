package serialize;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Circular<E extends Serializable>
        implements Serializable, BlockingQueue<E> {

    private int bufSize;
    private E[] buf;
    private int readPos = 0;
    private int writePos = 0;

    public Circular(int bufSize) {
        this.bufSize = bufSize;
        buf = (E[]) new Serializable[bufSize];
    }

    @Override
    public int remainingCapacity() {
        if (writePos == readPos && buf[readPos] == null)
            return bufSize;
        return (writePos > readPos) ? writePos - readPos : bufSize + writePos - readPos;
    }

    @Override
    public boolean add(E e) throws NullPointerException{
        if (e == null)
            throw new NullPointerException();
        // TODO:
        return false;
    }

    // pogrubiona
    @Override
    public boolean offer(E e) throws NullPointerException{
        if (e == null)
            throw new NullPointerException();
        // TODO:
        return false;
    }

    @Override
    public synchronized void put(E e) throws InterruptedException, NullPointerException {
        if (e == null)
            throw new NullPointerException();

        if (buf[writePos] != null)
            this.wait();
        buf[writePos] = e;
        writePos = (writePos + 1) % bufSize;
        this.notify();
    }

    // ---------

    @Override
    public synchronized E take() throws InterruptedException {

        if (buf[readPos] == null)
            this.wait();
        E e = buf[readPos];
        buf[readPos] = null;
        readPos = (readPos + 1) % bufSize;
        this.notify();

        return e;
    }

    //pogrubiona
    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        //TODO:
        return take();
    }

    @Override
    public boolean remove(Object o) {
        try {
            take();
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    // ---------

    @Override
    public <T> T[] toArray(T[] a) {

        T[] ts = (a.length >= bufSize)
                ? a : (T[]) Array.newInstance(a.getClass().getComponentType(), bufSize);

        for (int i = 0; i < bufSize; i++)
            ts[i] = (T) buf[i];

        return ts;
    }

    // ---------

    private String nameOfClass(int i) {
        return buf[i].getClass().getSimpleName();
    }

    @Override
    public String toString() {
        //dla każdego elementu buf wypisać:
        // indeks w tablicy, odległość od głowy bufora, typ obiektu,
        // napis skojarzony z elementem lub "Empty" gdy nie ma referencji do żadnego obiektu.

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < bufSize; i++) {
            sb.append("ind: " + i);
            sb.append("; odległość od głowy: ");
            sb.append((bufSize - i + writePos) % bufSize);

            if (buf[i] == null)
                sb.append("Empty");
            else {
                sb.append(buf[i]);
                sb.append("; typ obiektu: " + nameOfClass(i));
            }
            if (i < bufSize - 1)
                sb.append(System.lineSeparator());
        }
        sb.append(']');
        return sb.toString();
    }

}
