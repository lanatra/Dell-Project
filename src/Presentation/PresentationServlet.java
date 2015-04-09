package Presentation;

import Domain.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Lasse on 09-04-2015.
 */
@WebServlet(name = "PresentationServlet")
public class PresentationServlet extends HttpServlet {

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        //out.println("SERVLET REACHED");

        Controller cont = AssignController(request);

        request.setAttribute("testHole", "hole through");
        request.getRequestDispatcher("index.jsp").forward(request, response);

        // The action parameter is a hidden field in html/jsp that designates where the servlet should redirect
        String action = request.getParameter("action");

        // Can use a switch here instead of if / if-else but I didn't have the right version of the JDK (couldn't use strings in a switch statement)
        if(request.getSession().getAttribute("User") != null) {
            if (action.equals("getUser")) {
               //request.setAttribute(Controller.getUser(), "userInfo");
                request.setAttribute("testHole", "hole through");
               request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } else {
            if(action.equals("login")) {
                Object user = cont.login(request.getParameter("email"), request.getParameter("password"));
                if (user != null) {
                    request.getSession().setAttribute("User", user);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "Incorrect password");
                }
            }
        }
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }

    private Controller AssignController(HttpServletRequest request) {

        HttpSession sessionObj = request.getSession();
        Controller cont = (Controller) sessionObj.getAttribute("Controller"); // becomes null initially
        if (cont == null)
        {
            // Start new session
            // Not using singleton; each user will be given their own controller for use throughout their session
            cont = new Controller();
            sessionObj.setAttribute("Controller", cont);
        } else
        {
            // Continue ongoing session
            cont = (Controller) sessionObj.getAttribute("Controller");
        }
        return cont;

    }




    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }
}
