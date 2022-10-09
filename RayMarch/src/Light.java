public class Light extends Object{
    public Light(double x, double y, double z) {
        super(x, y, z);
    }

    @Override
    public boolean point_in(Vertex3<Double> p) {
        return false;
    }

    @Override
    public double sdf(Vertex3<Double> p) {
        return 0;
    }
}