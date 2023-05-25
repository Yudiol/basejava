package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class ResumeServlet extends HttpServlet {
    private static Storage storage;


    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write("<html><body><table border=1><tr><th>UUID</th><th>FullName</th></tr>");
        try {
            for (Resume resume : storage.getAllSorted()) {
                response.getWriter().write("<tr><td>" + resume.getUuid() + "</td><td>" + resume.getFullName() + "</td></tr>");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write("</table></body></html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
