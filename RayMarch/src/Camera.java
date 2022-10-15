/* IMPORTANT
 - The meat of the engine.
 - The reason why colors are to 0-1
   doubles instead of 0-255 8-bit
   integers is because a lot of
   color math is WAY easier with
   0-1 doubles. This is able to run
   on my baby chromebook so your computer
   probably has enough memory to spare.
 */

import org.jetbrains.annotations.Nullable;

// TODO Its squished because of how far the camera will see a ray (camera type)
public final class Camera extends Vertex3<Double> {
    public final double yaw;
    public final double pitch;
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
    public Camera(
            double x,
            double y,
            double z,
            double yaw,
            double pitch,
            double fov,
            int aspectw,
            int aspecth,
            double view_distance) {
        super(x, y, z);
        this.fov = fov;
        this.aspectw = aspectw;
        this.aspecth = aspecth;
        this.view_distance = view_distance;
        this.yaw = yaw;
        this.pitch = pitch;
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
    public <T extends Object> double[][][] render(double[][][] buf, T[] objects, Light[] lights, Environment env) throws NullPointerException {
        final double pm = Math.atan((this.aspecth / 2f) / fov);
        final double tm = Math.atan((this.aspectw / 2f) / fov);

        // TODO write a document on how this works
        for (int y = 0; y < buf.length; y++) {
            double p = ((Math.PI / 2) - pm) + (((pm * 2) / buf.length) * y);
            for (int x = 0; x < buf[0].length; x++) {
                double t = tm + (((-tm * 2) / buf[0].length) * x);
                Vector3d v = new Vector3d(
                    Math.sin(p + this.pitch) * Math.cos(t + this.yaw),
                    Math.sin(p + this.pitch) * Math.sin(t + this.yaw),
                    Math.cos(p + this.pitch)
                );
                // Reset position
                Vertex3<Double> pos = new Vertex3<>(this);
                RayOut<T> out = raymarch(objects, v, this.view_distance, pos);
                if (out != null) {
                    Vector3d normal = out.obj.normal(out.endpos);
                    for (Light light : lights) {
                        Vector3d lv = new Vector3d(light.x - pos.x, light.y - pos.y, light.z - pos.z);
                        lv.normalize();
                        Vertex3<Double> l_pos = new Vertex3<>(pos);
                        lv.move(l_pos, Cfg.DEPTH_BIAS);

                        // Diffuse
                        double ldistance = Util.distance(light, l_pos);
                        RayOut<T> out_l = raymarch(objects, lv, ldistance, l_pos);
                        double dif = lv.dot(normal);
                        if (dif < Cfg.EPSILON) {
                            dif = 0.0;
                        }

                        if (out_l == null) {
                            buf[y][x][0] += (dif * light.r * (light.intensity / Math.pow(ldistance, 2)) * out.obj.mat.albedo) * out.obj.mat.r;
                            buf[y][x][1] += (dif * light.g * (light.intensity / Math.pow(ldistance, 2)) * out.obj.mat.albedo) * out.obj.mat.g;
                            buf[y][x][2] += (dif * light.b * (light.intensity / Math.pow(ldistance, 2)) * out.obj.mat.albedo) * out.obj.mat.b;
                        }

                        // Indirect lighting
                        int depth = 0;
                        while (depth < Cfg.MAX_BOUNCES) {
                            for (int i = 0; i < (int) (Cfg.TOTAL_BOUNCES * out.obj.mat.roughness); i++) {
                                // Make the ray TODO
                            }
                            depth++;
                        }
                    }
                } else {
                    buf[y][x][0] = env.r * p;
                    buf[y][x][1] = env.g * p;
                    buf[y][x][2] = env.b * p;
                }
            }
        }

        return buf;
    }
}