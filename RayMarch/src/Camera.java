import org.jetbrains.annotations.Nullable;

// TODO Its squished because of how far the camera will see a ray (camera type)
public final class Camera extends Vertex3<Double> {
    private final double fov;
    private final double aspectw;
    private final double aspecth;
    private final double view_distance;

    /**
     * Create a new camera in the world.
     * @param x
     * @param y
     * @param z
     * @param fov Distance from the origin to the view plane
     * @param aspectw Aspect ratio width
     * @param aspecth Aspect ratio height
     * @param view_distance How far the camera sees before assuming that nothing is there
     */
    public Camera(double x, double y, double z, double fov, int aspectw, int aspecth, double view_distance) {
        super(x, y, z);
        this.fov = fov;
        this.aspectw = aspectw;
        this.aspecth = aspecth;
        this.view_distance = view_distance;
    }

    /**
     * Executes a ray with v as the direction. Attribute pos is given & set (WHY ARE THERE NO REFERENCES). Uses this.pos.
     * @param objects List of objects (can be lights, objects, anything)
     * @param v Vector for direciton of the ray
     * @return Returns -1 if not colliding (distance if colliding)
     * @param <T> An object
     */
    @Nullable
    private <T extends Object> RayOut<T> raymarch(T[] objects, Vector3d v, double max_distance, Vertex3<Double> pos) throws NullPointerException {
        double total_distance = 0;
        while (true) {
            // Get smallest distance to move
            double distance = Double.POSITIVE_INFINITY;
            T closest_object = null;
            for (T object : objects) {
                double cur_distance = object.sdf(pos);
                if (cur_distance < distance) {
                    distance = cur_distance;
                    closest_object = object;
                }
            }

            if (total_distance >= max_distance + Cfg.EPSILON) {
                return null;
            }
            // We only need to check if it's intersecting with the closest object
            v.move(pos, distance);
            if (closest_object.point_in(pos) || distance <= Cfg.EPSILON) {
                return new RayOut<>(total_distance, closest_object, pos);
            }
            total_distance += distance;
        }
    }

    /**
     * Render a scene into a buffer
     * @param buf Buffer with depth of 3
     * @param objects List of objects to collide with
     * @param lights Lights to use
     * @return Outputted buffer
     * @param <T> Child class
     * @throws NullPointerException
     */
    public <T extends Object> byte[][][] render(byte[][][] buf, T[] objects, Light[] lights) throws NullPointerException {
        final double pm = Math.atan((this.aspecth / 2f) / fov);
        final double tm = Math.atan((this.aspectw / 2f) / fov);

        // TODO write a document on how this works
        for (int y = 0; y < buf.length; y++) {
            double p = ((Math.PI / 2) - pm) + (((pm * 2) / buf.length) * y);
            for (int x = 0; x < buf[0].length; x++) {
                double t = tm + (((-tm * 2) / buf[0].length) * x);
                Vector3d v = new Vector3d(Math.sin(p) * Math.cos(t), Math.sin(p) * Math.sin(t), Math.cos(p));
                // Reset position
                Vertex3<Double> pos = new Vertex3<>(this);
                RayOut<T> out = raymarch(objects, v, this.view_distance, pos);
                if (out != null) {
                    Vector3d normal = new Vector3d(
                        out.endpos.x - out.collider.x,
                        out.endpos.y - out.collider.y,
                        out.endpos.z - out.collider.z
                    );
                    normal.normalize();
                    // Send a ray from the ending position to the light
                    for (Light light : lights) {
                        v = new Vector3d(light.x - pos.x, light.y - pos.y, light.z - pos.z);
                        v.normalize();
                        Vertex3<Double> l_pos = new Vertex3<>(pos);
                        v.move(l_pos, Cfg.EPSILON);
                        out = raymarch(objects, v, Util.distance(light, l_pos), l_pos);
                        double dif = v.dot(normal);
                        /*
                         * TODO
                         *  Multiple lights
                         *  Shadow bias
                         *  Materials
                         *  Light color
                         *  Other stuff
                         */
                        if (dif < Cfg.EPSILON) {
                            dif = 0.0;
                        }
                        buf[y][x][0] = (byte)(255 * dif);
                        if (out == null) { // Unobstructed
                            buf[y][x][0] /= 2;
                        }
                    }
                } else {
                    // TODO actual background color
                    buf[y][x][0] = (byte)181;
                    buf[y][x][1] = (byte)176;
                    buf[y][x][2] = (byte)255;
                }
            }
        }

        return buf;
    }
}