package pt.it.av.atnog.utils.linearAlgebra;

/**
 * Created by mantunes on 11/26/14.
 */
public class Vector {
    protected double data[];

    public Vector(double array[]) {
        this.data = array;
    }

    public Vector(int size) {
        data = new double[size];
    }

    public static Vector ones(int size) {
        Vector c = new Vector(size);
        c.set(1.0);
        return c;
    }

    public int size() {
        return data.length;
    }

    public void set(int i, double scalar) {
        data[i] = scalar;
    }

    public void set(double scalar) {
        for (int i = 0; i < data.length; i++)
            data[i] = scalar;
    }

    public double get(int i) {
        return data[i];
    }

    public Vector add(double scalar) {
        Vector c = new Vector(data.length);
        for (int i = 0; i < data.length; i++)
            c.data[i] = data[i] + scalar;
        return c;
    }

    public Vector add(Vector b) {
        Vector c = new Vector(data.length);
        for (int i = 0; i < data.length; i++)
            c.data[i] = data[i] + b.data[i];
        return c;
    }

    public Vector sub(double scalar) {
        Vector c = new Vector(data.length);
        for (int i = 0; i < data.length; i++)
            c.data[i] = data[i] - scalar;
        return c;
    }

    public Vector sub(Vector b) {
        Vector c = new Vector(data.length);
        for (int i = 0; i < data.length; i++)
            c.data[i] = data[i] - b.data[i];
        return c;
    }

    public Vector mul(double scalar) {
        Vector c = new Vector(data.length);
        for (int i = 0; i < data.length; i++)
            c.data[i] = data[i] * scalar;
        return c;
    }

    public double minkowskiDistance(Vector po, int p) {
        double sum = 0.0;
        if (data.length == po.data.length)
            for (int i = 0; i < data.length; i++) {
                double absDiff = Math.abs(data[i] - po.data[i]);
                sum += Math.pow(absDiff, p);
            }
        else
            throw new IllegalArgumentException("The points do not have the same number of dimensions");

        return Math.pow(sum, 1.0 / p);
    }

    public double euclideanDistance(Vector p) {
        return minkowskiDistance(p, 2);
    }

    public double manhattanDistance(Vector p) {
        return minkowskiDistance(p, 1);
    }

    public boolean equals(Object o) {
        boolean rv = false;
        if (o != null) {
            if (o == this)
                rv = true;
            else if (o instanceof Vector) {
                Vector B = (Vector) o;
                if (data.length == B.data.length) {
                    rv = true;
                    for (int i = 0; i < data.length && rv == true; i++)
                        if (Double.compare(data[i], B.data[i]) != 0)
                            rv = false;
                }
            }
        }
        return rv;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < data.length; i++)
            sb.append(data[i] + ";");
        sb.append("}");
        return sb.toString();
    }
}
