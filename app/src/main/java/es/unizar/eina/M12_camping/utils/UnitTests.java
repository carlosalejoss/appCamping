package es.unizar.eina.M12_camping.utils;

import android.util.Log;

import java.util.Date;

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

        Log.d("UnitTests", "Iniciando pruebas...");

        // Ejecutar las pruebas de crear y editar parcelas
        unitTests.testCrearParcela();
        unitTests.testEditarParcela();

        Log.d("UnitTests", "Pruebas finalizadas.");
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
        Log.d("UnitTests", "==== INICIANDO testCrearParcela ====");

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
            // 2) Nombre no es único
            Parcela parcelaNombreDuplicado = new Parcela("Parcela1", 4, 12.0, "Duplicado");
            verificarInsercionParcela(parcelaNombreDuplicado, "Prueba inválida: Nombre no único");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Nombre duplicado. ERROR: " + e.getMessage());
        }

        try {
            // 3) Nombre es null
            Parcela parcelaNombreNull = new Parcela(null, 4, 12.0, "Nombre null");
            verificarInsercionParcela(parcelaNombreNull, "Prueba inválida: Nombre es null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Nombre null. ERROR: " + e.getMessage());
        }

        try {
            // 4) maxOcupantes <= 0
            Parcela parcelaMaxOcupantesCero = new Parcela("Parcela3", 0, 15.0, "Ocupantes 0");
            verificarInsercionParcela(parcelaMaxOcupantesCero, "Prueba inválida: maxOcupantes <= 0");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: maxOcupantes <= 0. ERROR: " + e.getMessage());
        }

        try {
            // 5) maxOcupantes es null
            Parcela parcelaMaxOcupantesNull = new Parcela("Parcela5", null, 15.0, "maxOcupantes null");
            verificarInsercionParcela(parcelaMaxOcupantesNull, "Prueba inválida: maxOcupantes es null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: maxOcupantes null. ERROR: " + e.getMessage());
        }

        try {
            // 6) precioXpersona <= 0
            Parcela parcelaPrecioNegativo = new Parcela("Parcela6", 4, -10.0, "Precio negativo");
            verificarInsercionParcela(parcelaPrecioNegativo, "Prueba inválida: precioXpersona <= 0");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: precioXpersona <= 0. ERROR: " + e.getMessage());
        }

        try {
            // 7) precioXpersona es null
            Parcela parcelaPrecioNull = new Parcela("Parcela8", 4, null, "precioXpersona null");
            verificarInsercionParcela(parcelaPrecioNull, "Prueba inválida: precioXpersona es null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: precioXpersona null. ERROR: " + e.getMessage());
        }

        try {
            // 8) Descripción no es una cadena válida (simulado como null)
            Parcela parcelaDescripcionNull = new Parcela("Parcela9", 4, 12.0, null);
            verificarInsercionParcela(parcelaDescripcionNull, "Prueba inválida: Descripción es null");
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Descripción null. ERROR: " + e.getMessage());
        }

//         9) maxOcupantes contiene valor no numérico
//        Parcela parcelaMaxOcupantesInvalido = new Parcela("Parcela4", a, 15.0, "Ocupantes negativos");
//        verificarInsercionParcela(parcelaMaxOcupantesInvalido, "Prueba inválida: maxOcupantes no válido");
//         10) precioXpersona formato inválido
//        Parcela parcelaPrecioInvalido = new Parcela("Parcela7", 4, 2.0.3, "Formato precio");
//        verificarInsercionParcela(parcelaPrecioInvalido, "Prueba inválida: precioXpersona formato incorrecto");

        Log.d("UnitTests", "==== FINALIZADO testCrearParcela ====");
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
        Log.d("UnitTests", "==== INICIANDO testEditarParcela ====");

        Parcela parcelaExistente;

        // ===== PRUEBA DE EDICIÓN VÁLIDA =====
        // Crear una parcela base válida
        Parcela parcelaBase = new Parcela("Parcela Base", 4, 10.0, "Descripción válida");
        long id = parcelaRepository.insert(parcelaBase);
        Log.d("UnitTests", "Parcela base creada con ID: " + id);
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
        Log.d("UnitTests", "Parcela base creada con ID: " + id2);

        try {
            // Caso 1: Editar nombre a una cadena vacía
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setNombre("");
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: Nombre vacío");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Nombre vacío. ERROR: " + e.getMessage());
        }

        try {
            // Caso 2: Editar nombre a un valor ya existente
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setNombre("Parcela Editada");
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: Nombre no único");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Nombre no único. ERROR: " + e.getMessage());
        }

        try {
            // Caso 3: Editar maxOcupantes a 0
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setMaxOcupantes(0);
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: maxOcupantes = 0");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: maxOcupantes = 0. ERROR: " + e.getMessage());
        }

        try {
            // Caso 4: Editar precioXpersona a un valor negativo
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setPrecioXpersona(-5.0);
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: Precio por persona negativo");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Precio negativo. ERROR: " + e.getMessage());
        }

        try {
            // Caso 5: Editar descripción a null
            parcelaExistente = parcelaRepository.getParcelaById((int) id2);
            if (parcelaExistente != null) {
                parcelaExistente.setDescripcion(null);
                verificarEdicionParcela(parcelaExistente, "Prueba inválida: Descripción null");
            }
        } catch (Exception e) {
            Log.e("UnitTests", "Prueba inválida: Descripción null. ERROR: " + e.getMessage());
        }

        Log.d("UnitTests", "==== FINALIZADO testEditarParcela ====");
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

}
