public class Light extends Vertex3<Double> {
    public final byte r;
    public final byte g;
    public final byte b;
    public final double intensity;
    public Light(double x, double y, double z, byte r, byte g, byte b, double intensity) {
        super(x, y, z);
        this.r = r;
        this.g = g;
        this.b = b;
        this.intensity = intensity;
    }
}