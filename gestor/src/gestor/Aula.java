/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

/**
 *
 * @author  Rodrigo Enrique Herrera Solorzano 256423
 */



public class Aula implements Validable {
    private String id;
    private String nombre;
    private TipoAula tipo;
    private int capacidad;

    public Aula(String id, String nombre, TipoAula tipo, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
    }

    @Override
    public void validar() throws ValidarException {
        if (id == null || id.trim().isEmpty())
            throw new ValidarException("El ID del aula no puede estar vacío.");
        if (nombre == null || nombre.trim().isEmpty())
            throw new ValidarException("El nombre del aula no puede estar vacío.");
        if (capacidad <= 0)
            throw new ValidarException("La capacidad debe ser mayor a 0.");
        if (tipo == null)
            throw new ValidarException("Tipo de aula inválido.");
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoAula getTipo() { return tipo; }
    public int getCapacidad() { return capacidad; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipo(TipoAula tipo) { this.tipo = tipo; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + tipo + "), Capacidad: " + capacidad;
    }
}
