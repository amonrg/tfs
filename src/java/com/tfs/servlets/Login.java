package com.tfs.servlets;

import com.tfs.db.DBProps;
import com.tfs.util.Utils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        DBProps db = Utils.getDBProperties();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String query = "SELECT id, pgp_sym_decrypt(decode(name, 'base64'), '" + db.getPgp_key() + "') as name, pgp_sym_decrypt(decode(email, 'base64'), '" + db.getPgp_key() + "') as email, password " +
                       "FROM users WHERE pgp_sym_decrypt(decode(name, 'base64'), '" + db.getPgp_key() + "') = ?";
        
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
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                if (BCrypt.checkpw(password, rs.getString("password"))) {
                    session.setAttribute("user_id", rs.getString("id"));
                    session.setAttribute("username", rs.getString("name"));
                    response.sendRedirect("");
                }
            } else {
                System.out.println("User not found");
            }
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
        return "Login servlet";
    }
}
