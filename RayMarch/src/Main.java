import java.io.FileWriter;
import java.io.IOException;

public final class Main {
    public static void main(String[] args) throws IOException {
        // Clear the file
        new FileWriter("out.ppm", false).close();
        FileWriter fw = new FileWriter("out.ppm", true);
        byte[][][] buf = new byte[675][1200][3];

        Camera c = new Camera(0f, 0f, 0f, 10f, 16, 9, 500f);
        buf = c.render(
            buf,
            new Sphere[]{new Sphere(5, 0, 0, 2)},
            new Light[]{new Light(1, -4, 5)}
        );

        // Write it to a .ppm file
        fw.write(String.format("P3 %d %d 255\n", buf[0].length, buf.length));
        for (int y = 0; y < buf.length; y++) {
            for (int x = 0; x < buf[0].length; x++) {
                // 8-bit integers are signed
                fw.write(String.format("%d %d %d\n", Byte.toUnsignedInt(buf[y][x][0]), Byte.toUnsignedInt(buf[y][x][1]), Byte.toUnsignedInt(buf[y][x][2])));
            }
        }
        fw.close();
    }
}