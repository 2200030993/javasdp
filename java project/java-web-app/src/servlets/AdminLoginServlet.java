package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Maps this servlet to the "/AdminLoginServlet" URL pattern
@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the username and password from the request
        String username = request.getParameter("username"); // Ensure "username" matches the form field name
        String password = request.getParameter("password"); // Ensure "password" matches the form field name

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Use placeholders for the query
            String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Redirect to the admin review page if login is successful
                response.sendRedirect("admin_review.html");
            } else {
                // Show an error message if credentials are invalid
                out.write("<h3>Invalid credentials! Please try again.</h3>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.write("<h3>Database error occurred. Please try again later.</h3>");
        }
    }
}
