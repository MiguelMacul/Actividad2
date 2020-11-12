package Clases;

public class presetdata {
    protected String Nombre;
    protected String Version;
    protected String fecha;
    protected int Icono;

    public presetdata() {
    }

    public presetdata(String nombre, String version, String fecha, int icono) {
        Nombre = nombre;
        Version = version;
        this.fecha = fecha;
        Icono = icono;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIcono() {
        return Icono;
    }

    public void setIcono(int icono) {
        Icono = icono;
    }
}
