final class RayOut<T extends Object> {
    public final double distance;
    public final T obj;
    public final Vertex3<Double> endpos;

    public RayOut(double distance, T obj, Vertex3<Double> endpos) {
        this.distance = distance;
        this.obj = obj;
        this.endpos = endpos;
    }
}