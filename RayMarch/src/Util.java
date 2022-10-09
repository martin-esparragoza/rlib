public class Util {
    public static double distance(Vertex3<Double> p1, Vertex3<Double> p2) {
        return Math.sqrt(
                Math.pow(p1.x - p2.x, 2) +
                Math.pow(p1.y - p2.y, 2) +
                Math.pow(p1.z - p2.z, 2)
        );
    }
}