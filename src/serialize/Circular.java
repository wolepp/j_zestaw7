package serialize;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
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
        return (writePos > readPos)
                ? bufSize + readPos - writePos : readPos - writePos;
    }

    @Override
    public boolean add(E e) throws NullPointerException, IllegalStateException {
        if (offer(e))
            return true;

        throw new IllegalStateException("Buffer is full");
    }

    @Override
    public synchronized boolean offer(E e) throws NullPointerException {
        if (e == null)
            throw new NullPointerException();

        if (buf[writePos] != null)
            return false;

        buf[writePos] = e;
        writePos = (writePos + 1) % bufSize;
        return true;
    }

    @Override
    public void put(E e) throws InterruptedException, NullPointerException {
        if (buf[writePos] != null)
            this.wait();

        offer(e);
        this.notify();
    }

    @Override
    public E take() throws InterruptedException {

        if (buf[readPos] == null)
            this.wait();
        E e = poll();
        this.notify();

        return e;
    }

    @Override
    public synchronized E poll() {
        E e = buf[readPos];
        buf[readPos] = null;
        if (e != null)
            readPos = (readPos + 1) % bufSize;
        return e;
    }

    @Override
    public E remove() throws NullPointerException{
        if (buf[readPos] == null)
            throw new NullPointerException();

        E e = poll();
        return e;
    }

    @Override
    public synchronized boolean remove(Object o) {
        //TODO:
        //szuka tylko od read do write
        // jeśli nie ma to false
        //else
        // kasuje i przesuwa odpowiednio reszte, albo w prawo albo w lewo (chyba już co wygodniej)
        return false;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] ts = (a.length >= bufSize)
                ? a : (T[]) Array.newInstance(a.getClass().getComponentType(), bufSize);

        for (int i = 0; i < bufSize; i++)
            ts[i] = (T) buf[i];

        return ts;
    }

    private String nameOfClass(int i) {
        return buf[i].getClass().getSimpleName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < bufSize; i++) {
            sb.append("ind: " + i);
            sb.append("; od głowy: ");
            sb.append((bufSize - i + writePos) % bufSize);

            if (buf[i] == null) {
                sb.append("; Empty");
            } else {
                sb.append("; typ obiektu: " + nameOfClass(i));
                sb.append("; " + buf[i]);
            }
            if (i < bufSize - 1)
                sb.append(System.lineSeparator());
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}


