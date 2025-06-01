package org.marcospardavila.practicaTW2025Maven.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@Controller
@RequestMapping("/test")
public class TestController {

    private final DataSource dataSource;

    public TestController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/db-info")
    public Map<String, Object> obtenerInfoBD() {
        Map<String, Object> info = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            // Información de la conexión
            info.put("database_url", conn.getMetaData().getURL());
            info.put("database_product_name", conn.getMetaData().getDatabaseProductName());
            info.put("database_product_version", conn.getMetaData().getDatabaseProductVersion());
            info.put("driver_name", conn.getMetaData().getDriverName());
            info.put("driver_version", conn.getMetaData().getDriverVersion());
            info.put("username", conn.getMetaData().getUserName());
            info.put("catalog", conn.getCatalog());
            info.put("schema", conn.getSchema());

        } catch (Exception e) {
            info.put("error", e.getMessage());
        }

        return info;
    }

    @GetMapping("/tables")
    public Map<String, Object> verificarTablas() {
        Map<String, Object> resultado = new HashMap<>();

        // Lista de todas las tablas en tu esquema
        List<String> todasLasTablas = Arrays.asList(
                "USUARIO",
                "INGREDIENTE",
                "PRODUCTO",
                "PRODUCTO_INGREDIENTE",
                "PEDIDO",
                "DETALLE_PEDIDO",
                "PERSONALIZACION"
        );

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Información de la BD primero
            resultado.put("database_url", conn.getMetaData().getURL());
            resultado.put("database_name", conn.getMetaData().getDatabaseProductName());

            // Verificar si existen las tablas en la BD
            ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            List<String> tablasExistentesEnBD = new ArrayList<>();
            while (rs.next()) {
                tablasExistentesEnBD.add(rs.getString("TABLE_NAME"));
            }
            resultado.put("tablas_existentes_en_bd", tablasExistentesEnBD);

            // Iterar sobre todas las tablas del esquema y contar registros si existen
            Map<String, Integer> conteoPorTabla = new HashMap<>();
            for (String nombreTabla : todasLasTablas) {
                if (tablasExistentesEnBD.contains(nombreTabla.toUpperCase())) { // Convertir a mayúsculas para HSQLDB/H2
                    try (ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) as total FROM " + nombreTabla)) {
                        if (countRs.next()) {
                            conteoPorTabla.put(nombreTabla.toLowerCase() + "_count", countRs.getInt("total"));
                        }
                    } catch (Exception e) {
                        // Capturar errores si una tabla no es accesible por alguna razón
                        conteoPorTabla.put(nombreTabla.toLowerCase() + "_count_error", -1);
                        resultado.put("error_conteo_" + nombreTabla.toLowerCase(), e.getMessage());
                    }
                } else {
                    conteoPorTabla.put(nombreTabla.toLowerCase() + "_count", 0); // Si la tabla no existe, el conteo es 0
                }
            }
            resultado.put("conteo_registros", conteoPorTabla);

        } catch (Exception e) {
            resultado.put("error_general", e.getMessage());
        }

        return resultado;
    }


    @GetMapping("/test")
    public String test() {
        System.out.println("CONTROLADOR EJECUTADO"); // Para traza
        return "test";
    }
}