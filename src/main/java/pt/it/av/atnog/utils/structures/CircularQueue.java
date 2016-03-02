package pt.it.av.atnog.utils.structures;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Queue implemented with a circular buffer.
 *
 * <p>This queue has a fixed size.
 * After filling the buffer, any subsequent add operation will overweight the oldest element.
 * Currently this structure is used in two differente scenarios:
 * gather the gather neighborhoods of a elements and
 * as a buffer to mediate the communication between producers and consumers </p>
 * @param <E> the type of elements held in this collection
 */
public class CircularQueue<E> implements Queue<E> {
    private int head = 0, size = 0;
    private E data[];

    public CircularQueue() {
        this(10);
    }

    public CircularQueue(int size) {
        this.data = (E[]) new Object[size];
    }

    @Override
    public boolean add(E e) {
        if(e == null)
            throw new NullPointerException();

        int tail = (head + size) % data.length;
        data[tail] = e;
        if (size == data.length)
            head = (head + 1) % data.length;
        else
            size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        boolean rv = false;
        if (contains(o)) {
            E tdata[] = (E[]) new Object[this.data.length];
            int tsize = 0;
            for (E e : this)
                if (!e.equals(o))
                    tdata[tsize++] = e;
            this.data = null;
            this.data = tdata;
            this.size = tsize;
            this.head = 0;
            rv = true;
        }
        return rv;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean rv = true;
        Iterator it = c.iterator();
        while (rv && it.hasNext())
            rv = contains(it.next());
        return rv;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c)
            this.add(e);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        E tdata[] = (E[]) new Object[this.data.length];
        int tsize = 0;
        for (E e : this)
            if (!c.contains(e))
                tdata[tsize++] = e;

        boolean rv = tsize != size;
        this.data = null;
        this.data = tdata;
        this.size = tsize;
        this.head = 0;
        return rv;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        E tdata[] = (E[]) new Object[this.data.length];
        int tsize = 0;
        for (E e : this)
            if (c.contains(e))
                tdata[tsize++] = e;
        boolean rv = tsize != size;
        this.data = null;
        this.data = tdata;
        this.size = tsize;
        this.head = 0;
        return rv;
    }

    @Override
    public void clear() {
        this.head = 0;
        this.size = 0;
    }

    @Override
    public boolean offer(E e) {
        add(e);
        return true;
    }

    @Override
    public E remove() {
        if (isEmpty())
            throw new NoSuchElementException();
        E rv = data[head];
        head = (head + 1) % data.length;
        size--;
        return rv;
    }

    @Override
    public E poll() {
        E rv = null;
        if (!isEmpty()) {
            rv = data[head];
            head = (head + 1) % data.length;
            size--;
        }
        return rv;
    }

    @Override
    public E element() {
        if (isEmpty())
            throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E peek() {
        E rv = null;
        if (!isEmpty())
            rv = data[head];
        return rv;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns true if this collection is full.
     *
     * @return true if this collection is full
     */
    public boolean isFull() {
        return size == data.length;
    }

    /**
     * Returns the middle element in the queue.
     * It is usefull to gather neighborhoods of elements.
     *
     * @return the middle element in the queue
     */
    public E middle() {
        return data[(head + (size / 2)) % data.length];
    }

    /**
     * Returns the maximum number of elements in the queue.
     * @return the maximum number of elements in the queue
     */
    public int length() {
        return data.length;
    }

    @Override
    public boolean contains(Object o) {
        boolean rv = false;
        Iterator it = iterator();
        while (rv && it.hasNext())
            rv = it.next().equals(o);
        return rv;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularQueueIterator();
    }


    @Override
    public <T> T[] toArray(T[] a) {
        int size = 0;
        for (E e : this)
            a[size++] = (T) e;
        return a;
    }

    @Override
    public Object[] toArray() {
        E rv[] = (E[])new Object[data.length];
        int i = 0;
        for(E e: this)
            rv[i++] = e;
        return rv;
    }

    /**
     * CircularQueue iterator
     */
    private class CircularQueueIterator implements Iterator {
        private int i = head, count = size;

        @Override
        public boolean hasNext() {
            return count > 0;
        }

        @Override
        public Object next() {
            E rv = data[i];
            i = (i + 1) % data.length;
            count--;
            return rv;
        }
    }
}