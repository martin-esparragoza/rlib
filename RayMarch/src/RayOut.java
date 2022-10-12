final class RayOut<T extends Object> {
    public final double distance;
    public final T collider;
    public final Vertex3<Double> endpos;

    public RayOut(double distance, T collider, Vertex3<Double> endpos) {
        this.distance = distance;
        this.collider = collider;
        this.endpos = endpos;
    }
}