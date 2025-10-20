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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;


public abstract class Reserva implements Validable {
    protected String id;
    protected Aula aula;
    protected LocalDate fecha;
    protected LocalTime inicio;
    protected LocalTime fin;
    protected String responsable;
    protected EstadoReserva estado;

    public Reserva(String id, Aula aula, LocalDate fecha, LocalTime inicio, LocalTime fin, String responsable) {
        this.id = id;
        this.aula = aula;
        this.fecha = fecha;
        this.inicio = inicio;
        this.fin = fin;
        this.responsable = responsable;
        this.estado = EstadoReserva.ACTIVA;
    }

    protected void validarBase() throws ValidarException {
        if (id == null || id.trim().isEmpty())
            throw new ValidarException("ID de reserva vacío.");
        if (aula == null)
            throw new ValidarException("Aula no asignada.");
        if (fecha == null)
            throw new ValidarException("Fecha inválida.");
        if (inicio == null || fin == null)
            throw new ValidarException("Horas inválidas.");
        if (!inicio.isBefore(fin))
            throw new ValidarException("Hora inicio debe ser menor que hora fin.");
        if (responsable == null || responsable.trim().isEmpty())
            throw new ValidarException("Responsable vacío.");
    }

    public boolean solapa(Reserva otra) {
        if (!aula.equals(otra.aula)) return false;
        if (!fecha.equals(otra.fecha)) return false;
        return inicio.isBefore(otra.fin) && otra.inicio.isBefore(fin);
    }

    public long horasDuracion() {
        Duration d = Duration.between(inicio, fin);
        long minutos = d.toMinutes();
        return (minutos + 59) / 60; // redondeo arriba
    }

    public String getId() { return id; }
    public Aula getAula() { return aula; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getInicio() { return inicio; }
    public LocalTime getFin() { return fin; }
    public String getResponsable() { return responsable; }
    public EstadoReserva getEstado() { return estado; }

    public void setEstado(EstadoReserva e) { this.estado = e; }
    public void setResponsable(String r) { this.responsable = r; }

    public abstract String tipoReserva();

    @Override
    public String toString() {
        return id + " | Aula: " + aula.getId() + " | Fecha: " + fecha +
                " | " + inicio + "-" + fin + " | " + tipoReserva() + " | Resp: " + responsable + " [" + estado + "]";
    }

    // -------- Subclases ---------

    public static class ReservaClase extends Reserva {
        private String materia;
        private String docente;

        public ReservaClase(String id, Aula aula, LocalDate fecha, LocalTime inicio, LocalTime fin,
                            String responsable, String materia, String docente) {
            super(id, aula, fecha, inicio, fin, responsable);
            this.materia = materia;
            this.docente = docente;
        }

        public String getMateria() { return materia; }
        public String getDocente() { return docente; }

        @Override
        public void validar() throws ValidarException {
            validarBase();
            if (materia == null || materia.trim().isEmpty())
                throw new ValidarException("Materia vacía.");
            if (docente == null || docente.trim().isEmpty())
                throw new ValidarException("Docente vacío.");
        }

        @Override
        public String tipoReserva() { return "CLASE"; }
    }

    public static class ReservaPractica extends Reserva {
        private String equipo;
        private int alumnos;

        public ReservaPractica(String id, Aula aula, LocalDate fecha, LocalTime inicio, LocalTime fin,
                               String responsable, String equipo, int alumnos) {
            super(id, aula, fecha, inicio, fin, responsable);
            this.equipo = equipo;
            this.alumnos = alumnos;
        }

        public String getEquipo() { return equipo; }
        public int getAlumnos() { return alumnos; }

        @Override
        public void validar() throws ValidarException {
            validarBase();
            if (alumnos <= 0)
                throw new ValidarException("Número de alumnos inválido.");
            if (aula.getCapacidad() < alumnos)
                throw new ValidarException("Excede la capacidad del aula.");
        }

        @Override
        public String tipoReserva() { return "PRACTICA"; }
    }

    public static class ReservaEvento extends Reserva {
        private TipoEvento tipoEvento;
        private int asistentes;

        public ReservaEvento(String id, Aula aula, LocalDate fecha, LocalTime inicio, LocalTime fin,
                             String responsable, TipoEvento tipoEvento, int asistentes) {
            super(id, aula, fecha, inicio, fin, responsable);
            this.tipoEvento = tipoEvento;
            this.asistentes = asistentes;
        }

        public TipoEvento getTipoEvento() { return tipoEvento; }
        public int getAsistentes() { return asistentes; }

        @Override
        public void validar() throws ValidarException {
            validarBase();
            if (tipoEvento == null)
                throw new ValidarException("Tipo de evento inválido.");
            if (asistentes <= 0)
                throw new ValidarException("Asistentes inválidos.");
            if (aula.getCapacidad() < asistentes)
                throw new ValidarException("Excede capacidad del aula.");
        }

        @Override
        public String tipoReserva() { return "EVENTO"; }
    }
}
