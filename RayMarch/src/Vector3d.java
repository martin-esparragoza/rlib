/**
 * A 3d vertex using the double type (the d at the end refers to this)
 */
public final class Vector3d extends Vertex3<Double> {
    public Vector3d(Double x, Double y, Double z) {
        super(x, y, z);
    }

    public void normalize() {
        final double v = Math.abs(Math.sqrt(
                Math.pow(this.x, 2) +
                Math.pow(this.y, 2) +
                Math.pow(this.z, 2)
        ));

        this.x /= v;
        this.y /= v;
        this.z /= v;
    }

    /**
     * Moves a vertex along a vector
     * @param v Vertex to move
     * @param d Distance
     * @return Original vertex
     */
    public Vertex3<Double> move(Vertex3<Double> v, double d) {
        v.x += this.x * d;
        v.y += this.y * d;
        v.z += this.z * d;

        return v;
    }

    public void mul(double a) {
        this.x *= a;
        this.y *= a;
        this.z *= a;
    }
}