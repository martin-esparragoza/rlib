package rlib;

public class Vertex3<T extends Number> {
    public T x;
    public T y;
    public T z;

    public Vertex3(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex3(Vertex3<T> v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
}