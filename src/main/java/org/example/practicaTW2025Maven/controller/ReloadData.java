package org.example.practicaTW2025Maven.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class ReloadData {
    private final DataSource dataSource;

    public ReloadData(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/reload-data")
    public String reloadData() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.sql");
            if (inputStream == null) return "No se encontró data.sql";
            String sql = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            for (String sentencia : sql.split(";")) {
                if (!sentencia.trim().isEmpty()) {
                    stmt.execute(sentencia);
                }
            }
            return "Datos recargados correctamente.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
