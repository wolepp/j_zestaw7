package serialize;

import java.io.Serializable;
import java.util.Iterator;

public class CircularIterator<E extends Serializable>
        implements Iterator<E> {

    Circular circular;
    int currentIndex;
    boolean start = true;

    public CircularIterator(Circular<E> circular) {
        this.circular = circular;
        currentIndex = circular.readPos;
    }

    @Override
    public boolean hasNext() {
        return (circular.buf[(currentIndex + 1) % circular.bufSize] != null);
    }

    @Override
    public E next() {
        if (!start)
            currentIndex = (currentIndex + 1) % circular.bufSize;
        else
            start = false;
        return (E) circular.buf[currentIndex];
    }
}
