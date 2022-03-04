package com.tfs.servlets;

import com.tfs.db.DBProps;
import com.tfs.util.Utils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "Register", urlPatterns = {"/register"})
public class Register extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        DBProps db = Utils.getDBProperties();
        String query = "INSERT INTO users (name, email, password) "
                    + "VALUES (encode(pgp_sym_encrypt(?, '" + db.getPgp_key() + "'), 'base64'), encode(pgp_sym_encrypt(?, '" + db.getPgp_key() + "'), 'base64'), ?)";
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt(12));
        
        try {
            Class.forName(db.getDriver());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        try (Connection con = 
                DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPassword());
             PreparedStatement pstmt = 
                con.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Account registration servlet";
    }
}
