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
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class GestorReservas {
    private static final Scanner sc = new Scanner(System.in);
    private static final ArrayList<Aula> aulas = new ArrayList<Aula>();
    private static final ArrayList<Reserva> reservas = new ArrayList<Reserva>();

    private static final String FILE_AULAS = "aulas.txt";
    private static final String FILE_RESERVAS = "reservas.txt";

    public static void main(String[] args) {
        cargarDatos();
        menuPrincipal();
        guardarDatos();
        System.out.println("Fin del programa. Datos guardados.");
    }

    // menu
    private static void menuPrincipal() {
        int op;
        do {
            System.out.println("==================================");
            System.out.println("   GESTOR DE RESERVAS - ITCA");
            System.out.println("==================================");
            System.out.println("1. Gestionar aulas");
            System.out.println("2. Registrar reserva");
            System.out.println("3. Buscar / Modificar / Cancelar reserva");
            System.out.println("4. Reportes");
            System.out.println("5. Salir");
            System.out.println("==================================");
            System.out.print("Seleccione una opción: ");
            op = leerInt();

            switch (op) {
                case 1: menuAulas(); break;
                case 2: menuReservas(); break;
                case 3: menuGestionReservas(); break;
                case 4: menuReportes(); break;
                case 5: break;
                default: System.out.println("Opción inválida.");
            }
        } while (op != 5);
    }

    // menu para las aulas
    private static void menuAulas() {
        int op;
        do {
            System.out.println("\n--- GESTIÓN DE AULAS ---");
            System.out.println("1. Registrar aula");
            System.out.println("2. Listar aulas");
            System.out.println("3. Modificar aula");
            System.out.println("4. Volver");
            System.out.print("Opción: ");
            op = leerInt();
            switch (op) {
                case 1: registrarAula(); break;
                case 2: listarAulas(); break;
                case 3: modificarAula(); break;
                case 4: break;
                default: System.out.println("Opción inválida.");
            }
        } while (op != 4);
    }

    private static void registrarAula() {
        System.out.print("ID del aula: ");
        String id = sc.nextLine().trim();
        for (Aula a : aulas) {
            if (a.getId().equalsIgnoreCase(id)) {
                System.out.println("Ya existe un aula con ese ID.");
                return;
            }
        }
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.println("Tipo (1-Teórica, 2-Laboratorio, 3-Auditorio): ");
        int t = leerInt();
        TipoAula tipo = null;
        if (t == 1) tipo = TipoAula.TEORICA;
        else if (t == 2) tipo = TipoAula.LABORATORIO;
        else if (t == 3) tipo = TipoAula.AUDITORIO;
        else { System.out.println("Tipo inválido."); return; }
        System.out.print("Capacidad: ");
        int cap = leerInt();

        Aula a = new Aula(id, nombre, tipo, cap);
        try {
            a.validar();
            aulas.add(a);
            System.out.println("Aula registrada correctamente.");
        } catch (ValidarException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listarAulas() {
        if (aulas.isEmpty()) {
            System.out.println("No hay aulas registradas.");
        } else {
            System.out.println("--- LISTA DE AULAS ---");
            for (Aula a : aulas)
                System.out.println(a);
        }
    }

    private static void modificarAula() {
        System.out.print("Ingrese ID del aula: ");
        String id = sc.nextLine().trim();
        Aula aula = null;
        for (Aula a : aulas) {
            if (a.getId().equalsIgnoreCase(id)) {
                aula = a; break;
            }
        }
        if (aula == null) {
            System.out.println("Aula no encontrada.");
            return;
        }

        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nuevo = sc.nextLine();
        if (nuevo != null && !nuevo.trim().isEmpty()) aula.setNombre(nuevo);

        System.out.print("Nueva capacidad (0 para mantener): ");
        int cap = leerInt();
        if (cap > 0) aula.setCapacidad(cap);

        System.out.println("Tipo (1-Teórica,2-Laboratorio,3-Auditorio,0=mantener): ");
        int t = leerInt();
        if (t == 1) aula.setTipo(TipoAula.TEORICA);
        else if (t == 2) aula.setTipo(TipoAula.LABORATORIO);
        else if (t == 3) aula.setTipo(TipoAula.AUDITORIO);

        System.out.println("Aula modificada con éxito.");
    }

    // menu
    private static void menuReservas() {
        int op;
        do {
            System.out.println("\n--- REGISTRAR RESERVA ---");
            System.out.println("1. Clase");
            System.out.println("2. Práctica");
            System.out.println("3. Evento");
            System.out.println("4. Volver");
            System.out.print("Opción: ");
            op = leerInt();
            switch (op) {
                case 1: registrarClase(); break;
                case 2: registrarPractica(); break;
                case 3: registrarEvento(); break;
                case 4: break;
                default: System.out.println("Opción inválida.");
            }
        } while (op != 4);
    }

    private static Aula buscarAula() {
        System.out.print("ID del aula: ");
        String id = sc.nextLine().trim();
        for (Aula a : aulas) {
            if (a.getId().equalsIgnoreCase(id)) return a;
        }
        System.out.println("Aula no encontrada.");
        return null;
    }

    private static LocalDate leerFecha() {
        try {
            System.out.print("Fecha (YYYY-MM-DD): ");
            String s = sc.nextLine().trim();
            return LocalDate.parse(s);
        } catch (Exception e) {
            System.out.println("Fecha inválida.");
            return null;
        }
    }

    private static LocalTime leerHora(String msg) {
        try {
            System.out.print(msg + " (HH:MM): ");
            String s = sc.nextLine().trim();
            return LocalTime.parse(s);
        } catch (Exception e) {
            System.out.println("Hora inválida.");
            return null;
        }
    }

    private static void registrarClase() {
        Aula aula = buscarAula();
        if (aula == null) return;
        LocalDate fecha = leerFecha();
        LocalTime inicio = leerHora("Hora inicio");
        LocalTime fin = leerHora("Hora fin");
        System.out.print("Responsable: "); String resp = sc.nextLine().trim();
        System.out.print("Materia: "); String mat = sc.nextLine().trim();
        System.out.print("Docente: "); String doc = sc.nextLine().trim();

        Reserva.ReservaClase r = new Reserva.ReservaClase("R" + (reservas.size() + 1),
                aula, fecha, inicio, fin, resp, mat, doc);
        try {
            r.validar();
            if (hayConflicto(r)) {
                System.out.println("Error: conflicto de horario en el aula.");
                return;
            }
            reservas.add(r);
            System.out.println("Reserva registrada.");
        } catch (ValidarException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void registrarPractica() {
        Aula aula = buscarAula();
        if (aula == null) return;
        LocalDate fecha = leerFecha();
        LocalTime inicio = leerHora("Hora inicio");
        LocalTime fin = leerHora("Hora fin");
        System.out.print("Responsable: "); String resp = sc.nextLine().trim();
        System.out.print("Equipo requerido: "); String eq = sc.nextLine().trim();
        System.out.print("Cantidad de alumnos: "); int al = leerInt();

        Reserva.ReservaPractica r = new Reserva.ReservaPractica("R" + (reservas.size() + 1),
                aula, fecha, inicio, fin, resp, eq, al);
        try {
            r.validar();
            if (hayConflicto(r)) {
                System.out.println("Error: conflicto de horario en el aula.");
                return;
            }
            reservas.add(r);
            System.out.println("Reserva registrada.");
        } catch (ValidarException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void registrarEvento() {
        Aula aula = buscarAula();
        if (aula == null) return;
        LocalDate fecha = leerFecha();
        LocalTime inicio = leerHora("Hora inicio");
        LocalTime fin = leerHora("Hora fin");
        System.out.print("Responsable: "); String resp = sc.nextLine().trim();
        System.out.println("Tipo (1-Conferencia, 2-Taller, 3-Reunión): ");
        int t = leerInt();
        TipoEvento tipo = (t == 1) ? TipoEvento.CONFERENCIA :
                          (t == 2) ? TipoEvento.TALLER : TipoEvento.REUNION;
        System.out.print("Asistentes: "); int as = leerInt();

        Reserva.ReservaEvento r = new Reserva.ReservaEvento("R" + (reservas.size() + 1),
                aula, fecha, inicio, fin, resp, tipo, as);
        try {
            r.validar();
            if (hayConflicto(r)) {
                System.out.println("Error: conflicto de horario en el aula.");
                return;
            }
            reservas.add(r);
            System.out.println("Reserva registrada.");
        } catch (ValidarException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // reservas
    private static void menuGestionReservas() {
        int op;
        do {
            System.out.println("\n--- GESTIONAR RESERVAS ---");
            System.out.println("1. Buscar por ID");
            System.out.println("2. Modificar responsable");
            System.out.println("3. Cancelar reserva");
            System.out.println("4. Volver");
            System.out.print("Opción: ");
            op = leerInt();
            switch (op) {
                case 1: buscarReserva(); break;
                case 2: modificarResponsable(); break;
                case 3: cancelarReserva(); break;
                case 4: break;
                default: System.out.println("Opción inválida.");
            }
        } while (op != 4);
    }

    private static void buscarReserva() {
        System.out.print("ID de reserva: ");
        String id = sc.nextLine().trim();
        for (Reserva r : reservas) {
            if (r.getId().equalsIgnoreCase(id)) {
                System.out.println(r);
                return;
            }
        }
        System.out.println("No se encontró la reserva.");
    }

    private static void modificarResponsable() {
        System.out.print("ID de reserva: ");
        String id = sc.nextLine().trim();
        for (Reserva r : reservas) {
            if (r.getId().equalsIgnoreCase(id)) {
                System.out.print("Nuevo responsable: ");
                String resp = sc.nextLine().trim();
                r.setResponsable(resp);
                System.out.println("Actualizado.");
                return;
            }
        }
        System.out.println("Reserva no encontrada.");
    }

    private static void cancelarReserva() {
        System.out.print("ID de reserva: ");
        String id = sc.nextLine().trim();
        for (Reserva r : reservas) {
            if (r.getId().equalsIgnoreCase(id)) {
                r.setEstado(EstadoReserva.CANCELADA);
                System.out.println("Reserva cancelada.");
                return;
            }
        }
        System.out.println("Reserva no encontrada.");
    }

    // para los reportes
    private static void menuReportes() {
        System.out.println("\n--- REPORTES ---");
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }

        // top 3 aulas por horas reservadas
        HashMap<String, Long> horas = new HashMap<String, Long>();
        for (Reserva r : reservas) {
            if (r.getEstado() == EstadoReserva.ACTIVA) {
                long h = r.horasDuracion();
                String id = r.getAula().getId();
                if (horas.containsKey(id)) h += horas.get(id);
                horas.put(id, h);
            }
        }

        System.out.println("TOP 3 AULAS MÁS USADAS:");
        ArrayList<Map.Entry<String, Long>> lista = new ArrayList<Map.Entry<String, Long>>(horas.entrySet());
        Collections.sort(lista, new Comparator<Map.Entry<String, Long>>() {
            public int compare(Map.Entry<String, Long> a, Map.Entry<String, Long> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });

        int top = 0;
        for (Map.Entry<String, Long> e : lista) {
            System.out.println("Aula " + e.getKey() + " - " + e.getValue() + " horas");
            top++;
            if (top == 3) break;
        }
    }

    // validaciones
    private static boolean hayConflicto(Reserva nueva) {
        for (Reserva r : reservas) {
            if (r.getEstado() == EstadoReserva.ACTIVA && r.solapa(nueva)) {
                return true;
            }
        }
        return false;
    }

    private static int leerInt() {
        try {
            String s = sc.nextLine();
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    // para que se guarde
    private static void guardarDatos() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(FILE_AULAS));
            for (Aula a : aulas) {
                pw.println(a.getId() + ";" + a.getNombre() + ";" + a.getTipo() + ";" + a.getCapacidad());
            }
            pw.close();

            pw = new PrintWriter(new FileWriter(FILE_RESERVAS));
            for (Reserva r : reservas) {
                pw.println(r.getId() + ";" + r.getAula().getId() + ";" + r.getFecha() + ";" +
                        r.getInicio() + ";" + r.getFin() + ";" + r.getResponsable() + ";" +
                        r.getEstado() + ";" + r.tipoReserva());
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error guardando datos: " + e.getMessage());
        }
    }

    private static void cargarDatos() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_AULAS));
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length >= 4) {
                    TipoAula tipo = TipoAula.valueOf(p[2]);
                    aulas.add(new Aula(p[0], p[1], tipo, Integer.parseInt(p[3])));
                }
            }
            br.close();
        } catch (Exception e) {
            
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_RESERVAS));
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length >= 8) {
                    Aula aula = null;
                    for (Aula a : aulas) if (a.getId().equals(p[1])) aula = a;
                    if (aula == null) continue;
                    LocalDate f = LocalDate.parse(p[2]);
                    LocalTime i = LocalTime.parse(p[3]);
                    LocalTime fin = LocalTime.parse(p[4]);
                    String resp = p[5];
                    EstadoReserva est = EstadoReserva.valueOf(p[6]);
                    String tipo = p[7];
                    Reserva r;
                    if (tipo.equals("CLASE"))
                        r = new Reserva.ReservaClase(p[0], aula, f, i, fin, resp, "MateriaX", "DocenteX");
                    else if (tipo.equals("PRACTICA"))
                        r = new Reserva.ReservaPractica(p[0], aula, f, i, fin, resp, "Equipo", 10);
                    else
                        r = new Reserva.ReservaEvento(p[0], aula, f, i, fin, resp, TipoEvento.REUNION, 20);
                    r.setEstado(est);
                    reservas.add(r);
                }
            }
            br.close();
        } catch (Exception e) {
            
        }
    }
}
