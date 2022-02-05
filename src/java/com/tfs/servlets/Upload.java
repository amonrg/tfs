package com.tfs.servlets;

import com.tfs.db.DBProps;
import com.tfs.util.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.sql.DriverManager;

@WebServlet(name = "Upload", urlPatterns = {"/upload"})
@MultipartConfig
public class Upload extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        final String path = Utils.getFilesDir();
        final Part filePart = request.getPart("file");
        final String filename = getFileName(filePart);
        final PrintWriter writer = response.getWriter();        
        final String query = "INSERT INTO files (orig_filename, extension, size, upload_date, owner, password, short_url, filename)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        final OffsetDateTime date = OffsetDateTime.now();
        final String ext = filename.substring(filename.lastIndexOf("."), filename.length());
        final String disk_filename = Instant.now().getEpochSecond() + ext;
        final DBProps db = Utils.getDBProperties();
        
        try {
            Class.forName(db.getDriver());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        try (OutputStream out =
                new FileOutputStream(new File(path + disk_filename));
             InputStream filecontent = 
                filePart.getInputStream();
             Connection con = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPassword());
             PreparedStatement pstmt = con.prepareStatement(query);
             writer) {
            int read = 0;
            final byte[] bytes = new byte[1024];
            
            pstmt.setString(1, filename);
            pstmt.setString(2, ext);
            pstmt.setLong(3, filePart.getSize());
            pstmt.setObject(4, date);
            pstmt.setObject(5, null);
            pstmt.setString(6, "");
            pstmt.setString(7, "tfs/file/" + disk_filename);
            pstmt.setString(8, disk_filename);
            
            pstmt.executeUpdate();
            
            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            writer.println("New file " + filename + " created at " + path);
        } catch (FileNotFoundException fne) {
            writer.println("No file specified.");
            writer.println("<br/> ERROR: " + fne.getMessage());
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
        return "File upload servlet";
    }
    
    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
