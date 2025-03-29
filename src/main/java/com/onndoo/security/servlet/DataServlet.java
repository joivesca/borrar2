package com.onndoo.security.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
    @Resource(name = "jdbc/H2DB")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM MY_TABLE");
            while (rs.next()) {
                resp.getWriter().println("Data: " + rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            resp.getWriter().println("Error accessing the database.");
            e.printStackTrace();
        }
    }
}
