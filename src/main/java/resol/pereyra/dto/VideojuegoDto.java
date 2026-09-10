package resol.pereyra.dto;

public class VideojuegoDto {

    private int id;
    private String nombre;
    private double precio;
    private boolean necesitaReposicion;

    public VideojuegoDto() {
    }

    public VideojuegoDto(
            int id,
            String nombre,
            double precio,
            boolean necesitaReposicion
    ) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.necesitaReposicion = necesitaReposicion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isNecesitaReposicion() {
        return necesitaReposicion;
    }

    public void setNecesitaReposicion(
            boolean necesitaReposicion
    ) {
        this.necesitaReposicion = necesitaReposicion;
    }
}