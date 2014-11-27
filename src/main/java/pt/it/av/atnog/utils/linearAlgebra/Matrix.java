package pt.it.av.atnog.utils.linearAlgebra;

import pt.it.av.atnog.utils.Utils;
import pt.it.av.atnog.utils.parallel.ThreadPool;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by mantunes on 11/3/14.
 */
public class Matrix {
    protected double data[];
    protected int rows, columns;

    public Matrix(int rows, int columns) {
        this.data = new double[rows * columns];
        this.rows = rows;
        this.columns = columns;
    }

    public static Matrix identity(int size) {
        Matrix C = new Matrix(size, size);
        for (int i = 0; i < size; i++)
            C.data[i * C.columns + i] = 1.0;
        return C;
    }

    public static Matrix rand(int rows, int columns) {
        Matrix C = new Matrix(rows, columns);
        for (int n = 0; n < rows * columns; n++)
            C.data[n] = Utils.randomBetween(0, 10);
        return C;
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public void set(int r, int c, double scalar) {
        data[r * columns + c] = scalar;
    }

    public void set(double scalar) {
        for (int i = 0; i < data.length; i++)
            data[i] = scalar;
    }

    public double get(int r, int c) {
        return data[r * columns + c];
    }

    public Matrix transpose() {
        Matrix C = new Matrix(columns, rows);
        for (int n = 0, total = data.length; n < total; n++) {
            int r = n / columns, c = n % columns;
            C.data[c * C.columns + r] = data[n];
        }
        return C;
    }

    public Matrix add(Matrix B) {
        Matrix C = new Matrix(rows, columns);
        for (int n = 0, total = data.length; n < total; n++)
            C.data[n] = data[n] + B.data[n];
        return C;
    }

    public Matrix sub(Matrix B) {
        Matrix C = new Matrix(rows, columns);
        for (int n = 0, total = data.length; n < total; n++)
            C.data[n] = data[n] - B.data[n];
        return C;
    }

    /*public Matrix mul(Matrix B) {

    }*/

    public Matrix mul(Matrix B) {
        Matrix BT = B.transpose(), C = new Matrix(rows, B.columns);
        for (int i = 0; i < C.rows; i++) {
            int ic = i * columns;
            for (int j = 0; j < C.columns; j++) {
                int jc = j * BT.columns;
                double cij = 0.0;
                for (int k = 0; k < B.rows; k++)
                    cij += data[ic + k] * BT.data[jc + k];
                C.data[i * C.columns + j] = cij;
            }
        }
        return C;
    }

    public Matrix parallel_mul(Matrix B) {
        Matrix BT = B.transpose(), C = new Matrix(rows, B.columns);
        int I = C.rows, J = C.columns, K = columns;
        ThreadPool tp = new ThreadPool((Object o, List<Object> l) -> {
            int i = (Integer) o, ic = i * columns;
            for (int j = 0; j < C.columns; j++) {
                int jc = j * BT.columns;
                double cij = 0.0;
                for (int k = 0; k < B.rows; k++)
                    cij += data[ic + k] * BT.data[jc + k];
                C.data[i * C.columns + j] = cij;
            }
        });

        BlockingQueue<Object> sink = tp.sink();
        tp.start();

        try {
            for (int i = 0; i < I; i++)
                sink.add(new Integer(i));
            tp.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return C;
    }

    public boolean equals(Object o) {
        boolean rv = false;
        if (o != null) {
            if (o == this)
                rv = true;
            else if (o instanceof Matrix) {
                Matrix B = (Matrix) o;
                if (rows == B.rows && columns == B.columns) {
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
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                sb.append(String.format("%3.0f ", data[r * columns + c]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
