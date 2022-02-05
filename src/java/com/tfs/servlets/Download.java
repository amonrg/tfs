package com.tfs.servlets;

import com.tfs.util.Utils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Download", urlPatterns = {"/file/*"})
public class Download extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        final String reqFilename = pathInfo.substring(1, pathInfo.length());
        final String filePath = Utils.getFilesDir() + reqFilename;
        
        try (OutputStream os = response.getOutputStream();
             FileInputStream input = new FileInputStream(filePath)) {
            int read = 0;
            final byte[] bytes = new byte[4096];
            
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "filename=\"" + reqFilename + "\"");
            
            while ((read = input.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
        } catch (FileNotFoundException ex) {
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
        return "File download servlet";
    }
}
