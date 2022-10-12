public final class Sphere extends Object {
    private final double radius;

    public Sphere(double x, double y, double z, Material mat, double radius) {
        super(x, y, z, mat);
        this.radius = radius;
    }

    @Override
    public boolean point_in(Vertex3<Double> p) {
        // This saves a few instructions
        return Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2) + Math.pow(this.z - p.z, 2) < Math.pow(this.radius, 2);
    }

    @Override
    public double sdf(Vertex3<Double> p) {
        return Util.distance(this, p) - this.radius;
    }
}