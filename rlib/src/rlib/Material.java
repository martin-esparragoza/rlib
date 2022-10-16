package rlib;

public final class Material {
    public final double r;
    public final double g;
    public final double b;
    public final double albedo;
    public final double roughness;

    public Material(double r, double g, double b, double albedo, double roughness) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.albedo = albedo;
        this.roughness = roughness;
    }
}