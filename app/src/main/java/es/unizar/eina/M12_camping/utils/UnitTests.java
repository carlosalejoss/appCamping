package es.unizar.eina.M12_camping.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.unizar.eina.M12_camping.database.Parcela;
import es.unizar.eina.M12_camping.database.ParcelaReservada;
import es.unizar.eina.M12_camping.database.Reserva;
import es.unizar.eina.M12_camping.database.ParcelaRepository;
import es.unizar.eina.M12_camping.database.ReservaRepository;

/**
 * Clase que realiza pruebas unitarias sobre las funcionalidades de parcelas y reservas.
 * Cada método cubre los casos válidos e inválidos definidos en las clases de equivalencia.
 */
public class UnitTests {

    private static UnitTests unitTests = null;
    private final ParcelaRepository parcelaRepository;
    private final ReservaRepository reservaRepository;

    // Constructor
    public UnitTests(ParcelaRepository parcelaRepository, ReservaRepository reservaRepository) {
        this.parcelaRepository = parcelaRepository;
        this.reservaRepository = reservaRepository;
    }

    // Método para obtener la instancia singleton
    public static UnitTests getInstance(ParcelaRepository parcelaRepository, ReservaRepository reservaRepository) {
        if (unitTests == null) {
            unitTests = new UnitTests(parcelaRepository, reservaRepository);
        }
        return unitTests;
    }


    public static void ejecutarTests() {
        if (unitTests == null) {
            Log.e("UnitTests", "Error: UnitTests no está inicializado.");
            return;
        }

        Log.i("UnitTests", "Iniciando pruebas...");

        // Ejecutar las pruebas de crear y editar parcelas
        unitTests.testCrearParcela();
        unitTests.testEditarParcela();
        unitTests.testBorrarParcela();

        unitTests.testCrearReserva();
        unitTests.testEditarReserva();
        unitTests.testBorrarReserva();

        Log.i("UnitTests", "Pruebas finalizadas.");
    }

    /**
     * Prueba la creación de una parcela con todas las clases de equivalencia válidas e inválidas.
     * Se validan las entradas para:
     * - nombre (único, no nulo)
     * - maxOcupantes (>0, válido y no nulo)
     * - precioXpersona (>0, regex y no nulo)
     * - descripcion (string, nula o no nula permitida)
     */
    public void testCrearParcela() {
        Log.i("UnitTests", "==== INICIANDO testCrearParcela ====");

        // ===== PRUEBAS CLASES VÁLIDAS =====
        try {
            // 1) Nombre único, maxOcupantes > 0, precioXpersona > 0, descripción válida
            Parcela parcelaValida = new Parcela("Parcela1", 5, 15.5, "Parcela con sombra");
            verificarInsercionParcela(parcelaValida, "Prueba válida: Parcela con nombre único y parámetros válidos");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba válida fallida: " + e.getMessage());
        }

        // ===== PRUEBAS CLASES INVÁLIDAS =====
        try {
            // 4) Nombre es la cadena vacia
            Parcela parcelaNombreDuplicado = new Parcela("", 4, 12.0, "Nombre vacío");
            verificarInsercionParcela(parcelaNombreDuplicado, "Prueba inválida: Nombre vacío");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Nombre vacío. ERROR: " + e.getMessage());
        }

        try {
            // 4) Nombre es null
            Parcela parcelaNombreNull = new Parcela(null, 4, 12.0, "Nombre null");
            verificarInsercionParcela(parcelaNombreNull, "Prueba inválida: Nombre null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Nombre null. ERROR: " + e.getMessage());
        }

        try {
            // 6) maxOcupantes <= 0
            Parcela parcelaMaxOcupantesCero = new Parcela("Parcela3", 0, 15.0, "Ocupantes 0");
            verificarInsercionParcela(parcelaMaxOcupantesCero, "Prueba inválida: maxOcupantes <= 0");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: maxOcupantes <= 0. ERROR: " + e.getMessage());
        }

        try {
            // 10) maxOcupantes es null
            Parcela parcelaMaxOcupantesNull = new Parcela("Parcela5", null, 15.0, "maxOcupantes null");
            verificarInsercionParcela(parcelaMaxOcupantesNull, "Prueba inválida: maxOcupantes null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: maxOcupantes null. ERROR: " + e.getMessage());
        }

        try {
            // 12) precioXpersona <= 0
            Parcela parcelaPrecioNegativo = new Parcela("Parcela6", 4, -10.0, "Precio negativo");
            verificarInsercionParcela(parcelaPrecioNegativo, "Prueba inválida: precioXpersona <= 0");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: precioXpersona <= 0. ERROR: " + e.getMessage());
        }

        try {
            // 16) precioXpersona es null
            Parcela parcelaPrecioNull = new Parcela("Parcela8", 4, null, "precioXpersona null");
            verificarInsercionParcela(parcelaPrecioNull, "Prueba inválida: precioXpersona null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: precioXpersona null. ERROR: " + e.getMessage());
        }

        try {
            // 20) Descripción no es una cadena válida (simulado como null)
            Parcela parcelaDescripcionNull = new Parcela("Parcela9", 4, 12.0, null);
            verificarInsercionParcela(parcelaDescripcionNull, "Prueba inválida: Descripción null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Descripción null. ERROR: " + e.getMessage());
        }

        /*
         CASOS NO PROBADOS:

         - Casos no probados porque no se pueden simular en esta prueba:
         El siguiente caso no se puede probar directamente, ya que la gestión de nombre duplicados no se
         realiza en la base de datos sino en ParcelaEdit, por lo que no se puede simular en esta prueba.
         Si se descomenta la siguiente sección de código y se lanzan los tests, lo que sucederá
         será que la inserción funcionará correctamente, aunque no debería hacerlo.
         Por este motivo es que se ha comentado esta sección de código.

         // 2) Nombre no es único
         try {
             Parcela parcelaNombreDuplicado = new Parcela("Parcela1", 4, 12.0, "Duplicado");
             verificarInsercionParcela(parcelaNombreDuplicado, "Prueba inválida: Nombre duplicado");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: Nombre duplicado. ERROR: " + e.getMessage());
         }


         - Casos no probados porque darían errores de compilación:
         try {
             // 8) maxOcupantes no es un número
             Parcela parcelaMaxOcupantesInvalido = new Parcela("Parcela4", "a", 15.0, "Ocupantes negativos");
             verificarInsercionParcela(parcelaMaxOcupantesInvalido, "Prueba inválida: maxOcupantes no válido");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: maxOcupantes no válido. ERROR: " + e.getMessage());
         }

         try {
             // 14) precioXpersona no es un número
             Parcela parcelaPrecioInvalido = new Parcela("Parcela7", 4, 2.0.3, "Formato precio");
             verificarInsercionParcela(parcelaPrecioInvalido, "Prueba inválida: precioXpersona no válido");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: precioXpersona no válido. ERROR: " + e.getMessage());
         }

         try {
             // 20) Descripción no es una cadena válida (simulado como null)
             Parcela parcelaDescripcionNoValida = new Parcela("Parcela9", 4, 12.0, 2);
             verificarInsercionParcela(parcelaDescripcionNoValida, "Prueba inválida: Descripción no válida");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: Descripción no valida. ERROR: " + e.getMessage());
         }
        */

        Log.i("UnitTests", "==== FINALIZADO testCrearParcela ====");
    }

    /**
     * Intenta insertar una parcela en la base de datos y verifica si se ha añadido correctamente.
     *
     * @param parcela       La parcela que se intenta insertar.
     * @param descripcionPrueba Descripción de la prueba actual (para los logs).
     */
    private void verificarInsercionParcela(Parcela parcela, String descripcionPrueba) {
        long id = parcelaRepository.insert(parcela);
        if (id > 0) {
            Log.d("UnitTests", descripcionPrueba + " --> EXITO (ID: " + id + ")");
        } else {
            Log.e("UnitTests", descripcionPrueba + " --> FALLO (No insertada)");
        }
    }

    /**
     * Prueba la edición de una parcela existente con diferentes clases de equivalencia válidas e inválidas.
     * Se comprueba si las modificaciones son aceptadas o rechazadas y se registra el resultado.
     */
    public void testEditarParcela() {
        Log.i("UnitTests", "==== INICIANDO testEditarParcela ====");

        Parcela parcelaExistente;

        // ===== PRUEBA DE EDICIÓN VÁLIDA =====
        // Crear una parcela base válida
        Parcela parcelaBase = new Parcela("Parcela Base", 4, 10.0, "Descripción válida");
        long id = parcelaRepository.insert(parcelaBase);
        try {
            // Caso 0: Editar con datos válidos
            parcelaExistente = parcelaRepository.getParcelaById((int) id);
            if (parcelaExistente != null) {
                parcelaExistente.setNombre("Parcela Editada");
                parcelaExistente.setMaxOcupantes(6);
                parcelaExistente.setPrecioXpersona(15.0);
                parcelaExistente.setDescripcion("Descripción actualizada");
                verificarEdicionParcela(parcelaExistente, "Prueba válida: Edición exitosa");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba válida: Edición fallida. ERROR: " + e.getMessage());
        }

        // ===== PRUEBAS DE EDICIÓN INVÁLIDAS =====
        // Crear una parcela errores válida que debería mostrarse igual tras las ediciones
        Parcela parcelaErrores = new Parcela("Parcela Errores", 10, 8.0, "Descripción válida");
        long id2 = parcelaRepository.insert(parcelaErrores);

        try {
            // 4) Editar nombre a una cadena vacía
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setNombre("");
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: Nombre vacío");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Nombre vacío. ERROR: " + e.getMessage());
        }

        try {
            // 6) Editar maxOcupantes <= 0
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setMaxOcupantes(0);
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: maxOcupantes = 0");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: maxOcupantes = 0. ERROR: " + e.getMessage());
        }

        try {
            // 10) Editar maxOcupantes a null
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setMaxOcupantes(null);
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: maxOcupantes = 0");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: maxOcupantes es null. ERROR: " + e.getMessage());
        }

        try {
            // 12) Editar precioXpersona <= 0
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setPrecioXpersona(-5.0);
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: Precio por persona negativo");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Precio por persona negativo. ERROR: " + e.getMessage());
        }

        try {
            // 16) Editar precioXpersona a null
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setPrecioXpersona(null);
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: Precio por persona negativo");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Precio por persona es null. ERROR: " + e.getMessage());
        }

        try {
            // 20) Editar descripción a null
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setDescripcion(null);
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: Descripción null");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Descripción null. ERROR: " + e.getMessage());
        }

        /*
         CASOS NO PROBADOS:

         Casos no probados porque no se pueden simular en esta prueba:
         Este caso no se puede probar directamente, ya que la gestión de nombre duplicados no se realiza
         en la base de datos sino en ParcelaEdit, por lo que no se puede simular en esta prueba.
         Si se descomenta la siguiente sección de código y se lanzan los tests, lo que sucederá
         será que la edición funcionará correctamente, aunque no debería hacerlo.
         Por este motivo es que se ha comentado esta sección de código.

         try {
             // 2) Editar nombre a un nombre ya existente
             parcelaExistente = parcelaRepository.getParcelaById((int) id2);
             if (parcelaExistente != null) {
                 parcelaExistente.setNombre("Parcela Editada");
                 verificarEdicionParcela(parcelaExistente, "Prueba inválida: Nombre duplicado");
             }
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: Nombre duplicado. ERROR: " + e.getMessage());
         }

         Casos no probados porque darían errores de compilación:
         try {
             // 8) Editar maxOcupantes a un valor no numérico
             parcelaExistente = parcelaRepository.getParcelaById((int) id2);
             if (parcelaExistente != null) {
                 parcelaExistente.setMaxOcupantes("a");
                 verificarEdicionParcela(parcelaExistente, "Prueba inválida: maxOcupantes no válido");
             }
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: Descripción null. ERROR: " + e.getMessage());
         }

         try {
             // 14) Editar precioXpersona a un valor no numérico
             parcelaExistente = parcelaRepository.getParcelaById((int) id2);
             if (parcelaExistente != null) {
                 parcelaExistente.setPrecioXpersona(2.0.3);
                 verificarEdicionParcela(parcelaExistente, "Prueba inválida: precioXpersona no válido");
             }
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: Descripción null. ERROR: " + e.getMessage());
         }

         try {
             // 20) Editar descripción a un valor no válido
             parcelaExistente = parcelaRepository.getParcelaById((int) id2);
             if (parcelaExistente != null) {
                 parcelaExistente.setDescripcion(2);
                 verificarEdicionParcela(parcelaExistente, "Prueba inválida: Descripción no valida");
             }
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: Descripción null. ERROR: " + e.getMessage());
         }

        */

        Log.i("UnitTests", "==== FINALIZADO testEditarParcela ====");
    }

    /**
     * Verifica si la edición de una parcela fue exitosa o no.
     *
     * @param parcela La parcela que se desea actualizar.
     * @param descripcionPrueba Descripción de la prueba actual.
     */
    private void verificarEdicionParcela(Parcela parcela, String descripcionPrueba) {
        int filasAfectadas = parcelaRepository.update(parcela);
        if (filasAfectadas > 0) {
            Log.d("UnitTests", descripcionPrueba + " --> EXITO (Filas afectadas: " + filasAfectadas + ")");
        } else {
            Log.e("UnitTests", descripcionPrueba + " --> FALLO (No se actualizó)");
        }
    }

    /**
     * Prueba la eliminación de parcelas en el sistema.
     * Se verifican los siguientes casos:
     * - Eliminación de una parcela existente.
     * - Intento de eliminar una parcela inexistente.
     */
    public void testBorrarParcela() {
        Log.i("UnitTests", "==== INICIANDO testBorrarParcela ====");

        // ===== PRUEBA 1: Eliminar una parcela existente =====
        Parcela parcelaExistente = new Parcela("parcelaBorrar", 5, 5.5, "no deberia existir");
        long id = parcelaRepository.insert(parcelaExistente);

        if (id > 0) {
            int result = parcelaRepository.delete(parcelaRepository.getParcelaById((int) id));

            if (result > 0) {
                Log.d("UnitTests", "Prueba válida: Parcela eliminada correctamente --> EXITO");
            } else {
                Log.e("UnitTests", "Prueba válida: No se eliminó la parcela existente --> FALLO (No se eliminó)");
            }
        } else {
            Log.e("UnitTests", "Error al crear la parcela inicial para la prueba.");
        }

        // ===== PRUEBA 2: Eliminar una parcela inexistente =====
        int result = parcelaRepository.delete(parcelaRepository.getParcelaById(-1)); // ID inexistente
        if (result > 0) {
            Log.d("UnitTests", "Prueba inválida: Se eliminó incorrectamente una parcela inexistente --> EXITO (Se eliminó una parcela inexistente)");
        } else {
            Log.e("UnitTests", "Prueba inválida: Eliminación de parcela inexistente --> FALLO (No eliminó nada)");
        }

        Log.i("UnitTests", "==== FINALIZADO testBorrarParcela ====");
    }

    /**
     * Prueba la creación de una reserva con todas las clases de equivalencia válidas e inválidas.
     * Se validan:
     * - nombreCliente (string, no nulo)
     * - numeroMovil (numérico, no nulo)
     * - fechaEntrada (formato correcto, no anterior a hoy, válida)
     * - fechaSalida (formato correcto, posterior a fechaEntrada)
     * - parcelasReservadas (no vacías, ocupantes válidos y sin solapamientos)
     */
    public void testCrearReserva() {
        Log.i("UnitTests", "==== INICIANDO testCrearReserva ====");

        // Crear una parcela para unir a las reservas
        Parcela parcelaReservas = new Parcela("ParcelaReservasCreacion", 5, 5.5, "Parcela para reservas");
        long idParcela = parcelaRepository.insert(parcelaReservas);

        try {
            // 1) Reserva válida con todos los parámetros correctos
            Reserva reservaValida = new Reserva("Cliente1", 123456789, obtenerFecha("16-12-2025"),
                    obtenerFecha("20-12-2025"), 0.0);
            verificarInsercionReserva(reservaValida, idParcela, 2, "Prueba válida: Reserva con parámetros correctos");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba válida: Reserva con parámetros correctos --> ERROR: " + e.getMessage());
        }

        // ===== CLASES INVÁLIDAS =====
        try {
            // 4) nombreCliente es null
            Reserva nombreNull = new Reserva(null, 123456789, obtenerFecha("16-12-2025"),
                    obtenerFecha("20-12-2025"), 0.0);
            verificarInsercionReserva(nombreNull, idParcela, 1, "Prueba inválida: nombreCliente null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: nombreCliente null --> ERROR: " + e.getMessage());
        }

        try {
            // 8) numeroMovil es null
            Reserva numeroMovilNull = new Reserva("numeroMovilNull", null, obtenerFecha("16-12-2025"),
                    obtenerFecha("20-12-2025"), 0.0);
            verificarInsercionReserva(numeroMovilNull, idParcela, 1, "Prueba inválida: numeroMovil null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: numeroMovil null --> ERROR: " + e.getMessage());
        }

        try {
            // 16) fechaEntrada es null
            Reserva fechaEntradaNull = new Reserva("fechaEntradaNull", 123456789, null,
                    obtenerFecha("20-12-2025"), 0.0);
            verificarInsercionReserva(fechaEntradaNull, idParcela, 1, "Prueba inválida: fechaEntrada null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: fechaEntrada null --> ERROR: " + e.getMessage());
        }

        try {
            // 26) fechaSalida es null
            Reserva fechaSalidaNull = new Reserva("fechaSalidaNull", 123456789, obtenerFecha("20-12-2025"),
                   null , 0.0);
            verificarInsercionReserva(fechaSalidaNull, idParcela, 1, "Prueba inválida: fechaSalida null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: fechaSalida null --> ERROR: " + e.getMessage());
        }

        /*
         CASOS NO PROBADOS:

         - Casos no probados porque los errores no se gestionan en la base de datos sino en ReservaEdit:
         try {
             // 12) fechaEntrada anterior a la fecha actual
             Reserva fechaEntradaAntigua = new Reserva("fechaEntradaAntigua", 123456789, obtenerFecha("01-01-2023"),
                     obtenerFecha("20-12-2025"), 0.0);
             verificarInsercionReserva(fechaEntradaAntigua, idParcela, 1, "Prueba inválida: fechaEntrada anterior a hoy");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: fechaEntrada anterior a hoy --> ERROR: " + e.getMessage());
         }

         try {
             // 14) fechaEntrada no es una fecha del calendario gregoriano
             Reserva fechaEntradaInvalida = new Reserva("fechaEntradaInvalida", 123456789, obtenerFecha("40-30-2025"),
                     obtenerFecha("20-12-2025"), 0.0);
             verificarInsercionReserva(fechaEntradaInvalida, idParcela, 1, "Prueba inválida: fechaEntrada no válida");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: fechaEntrada no válida --> ERROR: " + e.getMessage());
         }

         try {
             // 24) fechaSalida anterior a fechaEntrada
             Reserva fechaSalidaIncorrecta = new Reserva("fechaSalidaIncorrecta", 123456789, obtenerFecha("16-12-2025"),
                     obtenerFecha("15-12-2025"), 0.0);
             verificarInsercionReserva(fechaSalidaIncorrecta, idParcela, 1, "Prueba inválida: fechaSalida anterior a fechaEntrada");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: fechaSalida anterior a fechaEntrada --> ERROR: " + e.getMessage());
         }

         try {
             // 28) No hay parcelas reservadas
             Reserva sinParcelas = new Reserva("sinParcelas", 123456789, obtenerFecha("16-12-2025"),
                     obtenerFecha("20-12-2025"), 0.0);
             // NO agregamos parcelas asociadas
             verificarInsercionReserva(sinParcelas, -1, -1, "Prueba inválida: Sin parcelas reservadas");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: Sin parcelas reservadas --> ERROR: " + e.getMessage());
         }

         try {
             // 30) numOcupantes > maxOcupantes
             Reserva ocupantesExcedidos = new Reserva("ocupantesExcedidos", 123456789, obtenerFecha("16-12-2025"),
                     obtenerFecha("20-12-2025"), 0.0);
             verificarInsercionReserva(ocupantesExcedidos, idParcela, 100, "Prueba inválida: Ocupantes excedidos");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: Ocupantes excedidos --> ERROR: " + e.getMessage());
         }

         try {
             // 32) Hay solapamiento de fechas
             Reserva solapamiento = new Reserva("solapamiento", 123456789, obtenerFecha("18-12-2025"),
                     obtenerFecha("22-12-2025"), 0.0);
             verificarInsercionReserva(solapamiento, idParcela, 2, "Prueba inválida: Solapamiento de fechas");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: Solapamiento de fechas --> ERROR: " + e.getMessage());
         }

         try {
             // 34) numOcupantes es null
             Reserva ocupantesNull = new Reserva("ocupantesNull", 123456789, obtenerFecha("16-12-2025"),
                     obtenerFecha("20-12-2025"), 0.0);
             verificarInsercionReserva(ocupantesNull, idParcela, null, "Prueba inválida: numOcupantes null");
         } catch (Exception e) {
             Log.e("UnitTests", "Prueba inválida: numOcupantes null --> ERROR: " + e.getMessage());
         }

        */

        Log.i("UnitTests", "==== FINALIZADO testCrearReserva ====");
    }

    /**
     * Función auxiliar para convertir una cadena de fecha en un objeto Date.
     *
     * @param fechaString Fecha en formato dd-MM-yyyy
     * @return Objeto Date correspondiente
     */
    private Date obtenerFecha(String fechaString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            return sdf.parse(fechaString);
        } catch (Exception e) {
            Log.e("UnitTests", "Error al convertir fecha: " + fechaString);
            return null;
        }
    }

    /**
     * Intenta insertar una reserva en la base de datos y verifica si se ha añadido correctamente.
     *
     * @param reserva       La reserva que se intenta insertar.
     * @param descripcionPrueba Descripción de la prueba actual (para los logs).
     */
    private void verificarInsercionReserva(Reserva reserva, long idParcela, Integer numOcupantes, String descripcionPrueba) {
        long id = reservaRepository.insert(reserva);
        if (id > 0) {
            ParcelaReservada parcelaReservada = new ParcelaReservada((int) id, (int) idParcela, numOcupantes);
            long idParRes = reservaRepository.insertParcelaReservada(parcelaReservada);
            if (idParRes > 0) {
                Log.d("UnitTests", descripcionPrueba + " --> EXITO (ID: " + id + ")");
            } else {
                Log.e("UnitTests", descripcionPrueba + " --> FALLO (Reserva Insertada, ParcelaReservada no insertada)");
            }
        } else {
            Log.e("UnitTests", descripcionPrueba + " --> FALLO (Reserva no insertada)");
        }
    }

    /**
     * Prueba la edición de una reserva existente con diferentes clases de equivalencia válidas e inválidas.
     * Se comprueba si las modificaciones son aceptadas o rechazadas y se registra el resultado.
     */
    public void testEditarReserva() {
        Log.i("UnitTests", "==== INICIANDO testEditarReserva ====");

        Reserva reservaExistente;
        // Crear una parcela para unir a las reservas
        Parcela parcelaReservas = new Parcela("ParcelaReservasEdicion", 5, 5.5, "Parcela para reservas");
        long idParcela = parcelaRepository.insert(parcelaReservas);

        // Crear una reserva base válida
        Reserva reservaBase = new Reserva("Reserva Base", 123456789, obtenerFecha("20-12-2025"),
                obtenerFecha("30-12-2025"), 0.0);
        long id = reservaRepository.insert(reservaBase);

        // ===== PRUEBA DE EDICIÓN VÁLIDA =====
        try {
            // Caso 0: Editar con datos válidos
            reservaExistente = reservaRepository.getReservaById((int) id);
            if (reservaExistente != null) {
                reservaExistente.setNombreCliente("Reserva Editada");
                reservaExistente.setNumeroMovil(987654321);
                reservaExistente.setFechaEntrada(obtenerFecha("22-12-2025"));
                reservaExistente.setFechaSalida(obtenerFecha("28-12-2025"));
                verificarEdicionReserva(reservaExistente, "Prueba válida: Edición exitosa");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba válida: Edición fallida. ERROR: " + e.getMessage());
        }

        // ===== PRUEBAS DE EDICIÓN INVÁLIDAS =====
        // Crear una reserva errores válida que debería mostrarse igual tras las ediciones
        Reserva reservaErrores = new Reserva("Reserva Errores", 123456789, obtenerFecha("20-12-2025"),
                obtenerFecha("30-12-2025"), 0.0);
        long id2 = reservaRepository.insert(reservaErrores);

        try {
            // 4) nombreCliente es null
            reservaExistente = reservaRepository.getReservaById((int) id2);
            if (reservaExistente != null) {
                reservaExistente.setNombreCliente(null);
                verificarEdicionReserva(reservaExistente, "Prueba inválida: Nombre null");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Nombre null. ERROR: " + e.getMessage());
        }

        try {
            // 8) numeroMovil es null
            reservaExistente = reservaRepository.getReservaById((int) id2);
            if (reservaExistente != null) {
                reservaExistente.setNumeroMovil(null);
                verificarEdicionReserva(reservaExistente, "Prueba inválida: numeroMovil null");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: numeroMovil null. ERROR: " + e.getMessage());
        }

        try {
            // 16) fechaEntrada es null
            reservaExistente = reservaRepository.getReservaById((int) id2);
            if (reservaExistente != null) {
                reservaExistente.setFechaEntrada(null);
                verificarEdicionReserva(reservaExistente, "Prueba inválida: fechaEntrada null");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: fechaEntrada null. ERROR: " + e.getMessage());
        }

        try {
            // 16) fechaSalida es null
            reservaExistente = reservaRepository.getReservaById((int) id2);
            if (reservaExistente != null) {
                reservaExistente.setFechaSalida(null);
                verificarEdicionReserva(reservaExistente, "Prueba inválida: fechaSalida null");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: fechaSalida null. ERROR: " + e.getMessage());
        }

        /*
         CASOS NO PROBADOS:
         Habría que probar, siguiendo la misma estructura de esta función, las mismas cosas que en
         la función de testCrearReserva, pero con la edición de las reservas en lugar de la creación.
         Como simplemente sería repetir lo mismo cambiando la estructura y principalmente porque
         ni se ejecutaría porque son comentarios se ha considerado innecesario hacer las pruebas
         que no funcionarían porque los errores se gestionarían en reservaEdit.
        */

        Log.i("UnitTests", "==== FINALIZADO testEditarParcela ====");
    }

    /**
     * Verifica si la edición de una reserva fue exitosa o no.
     *
     * @param reserva La reserva que se desea actualizar.
     * @param descripcionPrueba Descripción de la prueba actual.
     */
    private void verificarEdicionReserva(Reserva reserva, String descripcionPrueba) {
        int filasAfectadas = reservaRepository.update(reserva);
        if (filasAfectadas > 0) {
            Log.d("UnitTests", descripcionPrueba + " --> EXITO (Filas afectadas: " + filasAfectadas + ")");
        } else {
            Log.e("UnitTests", descripcionPrueba + " --> FALLO (No se actualizó)");
        }
    }

    /**
     * Prueba la eliminación de reservas en el sistema.
     * Se verifican los siguientes casos:
     * - Eliminación de una reserva existente.
     * - Intento de eliminar una reserva inexistente.
     */
    public void testBorrarReserva() {
        Log.i("UnitTests", "==== INICIANDO testBorrarReserva ====");

        // ===== PRUEBA 1: Eliminar una reserva existente =====
        Reserva reservaExistente = new Reserva("reservaBorrar", 123456789, obtenerFecha("20-12-2025"),
                obtenerFecha("30-12-2025"), 0.0);
        long id = reservaRepository.insert(reservaExistente);

        if (id > 0) {
            int result = reservaRepository.delete(reservaRepository.getReservaById((int) id));

            if (result > 0) {
                Log.d("UnitTests", "Prueba válida: Reserva eliminada correctamente --> EXITO");
            } else {
                Log.e("UnitTests", "Prueba válida: No se eliminó la reserva existente --> FALLO (No se eliminó)");
            }
        } else {
            Log.e("UnitTests", "Error al crear la reserva inicial para la prueba.");
        }

        // ===== PRUEBA 2: Eliminar una reserva inexistente =====
        int result = reservaRepository.delete(reservaRepository.getReservaById(-1)); // ID inexistente
        if (result > 0) {
            Log.d("UnitTests", "Prueba inválida: Se eliminó incorrectamente una reserva inexistente --> EXITO (Se eliminó una reserva inexistente)");
        } else {
            Log.e("UnitTests", "Prueba inválida: Eliminación de reserva inexistente --> FALLO (No eliminó nada) ");
        }

        Log.i("UnitTests", "==== FINALIZADO testBorrarReserva ====");
    }
}
