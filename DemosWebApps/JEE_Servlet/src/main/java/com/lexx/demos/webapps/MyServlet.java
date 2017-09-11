package com.lexx.demos.webapps;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Created by Alex Corghencea on 18 July 2017.
 */
@WebServlet("/myservlet")
public class MyServlet extends HttpServlet {

    public MyServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        String htmlCode =
                "<!DOCTYPE html>\n" +
                "<html>\n<head>\n" +
                "<title>My Servlet</title>\n" +
                "</head>\n<body>\n\n" +
                "<h1>MyServlet HTML heading</h1>\n" +
                "<p>MyServlet HTML paragraph.</p>\n" +
                "</body>\n\n</html>";
        printWriter.println(htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
