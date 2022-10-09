public abstract class Object extends Vertex3<Double> {
    public Object(double x, double y, double z) {
        super(x, y, z);
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
}