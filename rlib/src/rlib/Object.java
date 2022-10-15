package rlib;

public abstract class Object extends Vertex3<Double> {
    public final Material mat;
    public Object(double x, double y, double z, Material mat) {
        super(x, y, z);
        this.mat = mat;
    }

    /**
     * Finds out if given vertex is in the object
     * @param p Point
     * @return true if in
     */
    public abstract boolean point_in(Vertex3<Double> p);

    /**
     * Signed distance function
     * @param p Point
     * @return distance
     */
    public abstract double sdf(Vertex3<Double> p);

    /**
     * Calculates the normal based on a point on its surface
     * @param p Point
     * @return Normal
     */
    public abstract Vector3d normal(Vertex3<Double> p);
}