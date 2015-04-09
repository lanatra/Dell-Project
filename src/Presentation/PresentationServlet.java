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
public class PresentationServlet extends HttpServlet {

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        //out.println("SERVLET REACHED");

        Controller cont = AssignController(request);


        // The action parameter is a hidden field in html/jsp that designates where the servlet should redirect
        String action = "";
        if(request.getParameter("action") != null)
            action = request.getParameter("action");
        System.out.println("hiJensa2");
        // Can use a switch here instead of if / if-else but I didn't have the right version of the JDK (couldn't use strings in a switch statement)
        if(request.getSession().getAttribute("User") != null) {
            request.setAttribute("User", request.getSession().getAttribute("User"));
            System.out.println("loggedin");
            if (action.equals("getUser")) {
               //request.setAttribute(Controller.getUser(), "userInfo");
                request.setAttribute("testHole", "hole through");
               request.getRequestDispatcher("index.jsp").forward(request, response);
            }
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            if(action.equals("login")) {
                Object user = cont.login(request.getParameter("email"), request.getParameter("password"));
                if (user != null) {
                    System.out.println("correct");
                    request.getSession().setAttribute("User", user);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    System.out.println("incorrect");
                    request.setAttribute("message", "Incorrect password");
                }
            }
            System.out.println("fallback");
            response.sendRedirect("/login.jsp");
        }
        //response.setHeader("Location", "/login.jsp");

        //request.getRequestDispatcher("login.jsp").forward(request, response);
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
