package Presentation;


import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Created by Andreas Poulsen on 10-Apr-15.
 */
public class Static extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        RequestDispatcher rd = getServletContext().getNamedDispatcher("default");

        HttpServletRequest wrapped = new HttpServletRequestWrapper(req) {
            public String getServletPath() { return ""; }
        };

        rd.forward(wrapped, resp);
    }
}
