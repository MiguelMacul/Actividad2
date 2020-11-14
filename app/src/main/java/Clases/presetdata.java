package Clases;

public class presetdata {
    protected String Version;
    protected String fecha;
    protected int Icono;

    public presetdata() {
    }

    public presetdata( String version, String fecha, int icono) {
        Version = version;
        this.fecha = fecha;
        Icono = icono;
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
