public class Light extends Vertex3<Double> {
    public final double r;
    public final double g;
    public final double b;
    public final double intensity;
    public Light(double x, double y, double z, double r, double g, double b, double intensity) {
        super(x, y, z);
        this.r = r;
        this.g = g;
        this.b = b;
        this.intensity = intensity;
    }
}