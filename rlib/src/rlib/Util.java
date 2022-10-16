package rlib;

public class Util {
    public static double distance(Vertex3<Double> p1, Vertex3<Double> p2) {
        return Math.sqrt(
            Math.pow(p1.x - p2.x, 2) +
            Math.pow(p1.y - p2.y, 2) +
            Math.pow(p1.z - p2.z, 2)
        );
    }

    public static Vector3d reflect(Vector3d v, Vector3d normal) {
        double dp = normal.dot(v);
        return new Vector3d(
                v.x - 2.0 * dp * normal.x,
                v.y - 2.0 * dp * normal.y,
                v.z - 2.0 * dp * normal.z
        );
    }
}