// TODO ITS SQUISHED? WHY?
// FIXME moiré pattern :(
public final class Camera extends Vertex3<Double> {
    private final double fov;
    private final double aspectw;
    private final double aspecth;
    private final double view_distance;
    // Ignore variable. Just used as an internal access.
    private Vertex3<Double> pos = new Vertex3<>((double)0, (double)0, (double)0);

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
    private <T extends Object> double raymarch(T[] objects, Vector3d v, double max_distance) throws NullPointerException {
        double total_distance = 0;
        while (true) {
            // Get smallest distance to move
            double distance = Double.POSITIVE_INFINITY;
            T closest_object = null;
            for (T object : objects) {
                double cur_distance = object.sdf(this.pos);
                if (cur_distance < distance) {
                    distance = cur_distance;
                    closest_object = object;
                }
            }

            // System.out.println(total_distance);
            if (total_distance >= max_distance + Cfg.EPSILON) {
                return -1;
            }
            // We only need to check if it's intersecting with the closest object
            this.pos = v.move(this.pos, distance);
            if (closest_object.point_in(this.pos) || distance <= Cfg.EPSILON) {
                return total_distance;
            }
            total_distance += distance;
        }
    }

    /**
     * Does not update the position variable. Useful for when you want to do multiple rays from a point.
     * @param objects Array of objects
     * @param v Directional vector (normalzied hopefully)
     * @param max_distance Max can travel
     * @param pos Origin
     * @return Distance (-1 if no collision)
     * @param <T> An object
     * @throws NullPointerException
     */
    private <T extends Object> double raymarch(T[] objects, Vector3d v, double max_distance, Vertex3<Double> pos) throws NullPointerException {
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

            // System.out.println(total_distance);
            if (total_distance >= max_distance + Cfg.EPSILON) {
                return -1;
            }
            // We only need to check if it's intersecting with the closest object
            pos = v.move(pos, distance);
            if (closest_object.point_in(pos) || distance <= Cfg.EPSILON) {
                return total_distance;
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
    public <T extends Object, U extends Object> byte[][][] render(byte[][][] buf, T[] objects, U[] lights) throws NullPointerException {
        final double pm = Math.atan((this.aspecth / 2f) / fov);
        final double tm = Math.atan((this.aspectw / 2f) / fov);

        // TODO write a document on how this works
        for (int y = 0; y < buf.length; y++) {
            double p = ((Math.PI / 2) - pm) + (((pm * 2) / buf.length) * y);
            for (int x = 0; x < buf[0].length; x++) {
                double t = tm + (((-tm * 2) / buf[0].length) * x);
                Vector3d v = new Vector3d(Math.sin(p) * Math.cos(t), Math.sin(p) * Math.sin(t), Math.cos(p));
                // Reset position
                this.pos.x = this.x;
                this.pos.y = this.y;
                this.pos.z = this.z;
                double distance = raymarch(objects, v, this.view_distance);

                // Check if it is a shadow or not
                if (distance >= Cfg.EPSILON) {
                    // Send a ray from the ending position to the light
                    for (U light : lights) {
                        v = new Vector3d(light.x - this.x, light.y - this.y, light.z - this.z);
                        v.normalize();
                        // Move it a LITTLE so it doesn't instantly see itself as an obstruction
                        // (Allows it to move instead of being locked in place)
                        // Afaik this value is small enough to not add that much noise to the final
                        // image but... FIXME
                        this.pos = v.move(this.pos, Cfg.EPSILON);
                        distance = raymarch(objects, v, Util.distance(light, this.pos), this.pos);
                        if (distance < 0) {
                            buf[y][x][0] = (byte)255;
                        }
                    }
                }
            }
        }

        return buf;
    }
}