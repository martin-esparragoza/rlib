import rlib.*;

import java.io.FileWriter;
import java.io.IOException;

public final class Main {
    public static void main(String[] args) throws IOException {
        // Clear the file
        new FileWriter("out.ppm", false).close();
        FileWriter fw = new FileWriter("out.ppm", true);
        double[][][] buf = new double[800][800][3];

        Camera c = new Camera(0, 0, 0, 0, 0, 10, 10, 10, 500);
        long start = System.nanoTime();
        c.render(
            buf,
            new Sphere[]{
                new Sphere(8, -2, 0, new Material(0.39, 0.88, 0.52, 1.0, 0.5), 1.5),
                new Sphere(9, 2, 0, new Material(0.53, 0.195, 0.91, 0.7, 0.3), 1.5)
            },
            new Light[]{
                new Light(-4.5, -4, 0.4, 1.0, 1.0, 0.87, 50.0),
                new Light(2.5, 5, 0.5, 1.0, 0.3, 0.0, 81.0)
            },
            new Environment(0.61, 1.0, 0.78, 0.01)
        );
        long end = System.nanoTime();
        System.out.printf("Took %fms\n", (end - start) / 1000000.0);

        // Write it to a .ppm file
        fw.write(String.format("P3 %d %d 255\n", buf[0].length, buf.length));
        for (int y = 0; y < buf.length; y++) {
            for (int x = 0; x < buf[0].length; x++) {
                fw.write(String.format("%d %d %d\n", (int)(buf[y][x][0] * 255), (int)(buf[y][x][1] * 255), (int)(buf[y][x][2] * 255)));
            }
        }
        fw.close();
    }
}