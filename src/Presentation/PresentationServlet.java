package Presentation;

import Domain.Controller;
import Domain.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class PresentationServlet extends HttpServlet {

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            Controller cont = AssignController(request);
            String action = ""; // The action parameter is a hidden field in html/jsp that designates where the servlet should redirect
            if (request.getParameter("action") != null)
                action = request.getParameter("action");
            // Can use a switch here instead of if / if-else but I didn't have the right version of the JDK (couldn't use strings in a switch statement)

        // if logged in
        if (request.getSession().getAttribute("User") != null) {
            request.setAttribute("User", request.getSession().getAttribute("User")); // passing user object to request

            switch (action) {
                case "getUser":
                    getUser(request, response, cont);
                    break;
                case "createProjectRequest":
                    createProjectRequest(request, response, cont);
                    break;

                /*
                // It's good to put actions into separate methods like this, it would be mess in little while otherwise.
                case "anotherAction":
                    anotherAction(request, response, cont);
                    break;
                 */
                    default:
                        String url = request.getRequestURI();
                        request.setAttribute("url", url);
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                }

        } else {
            if (action.equals("login")) {
                Object user = cont.login(request.getParameter("email"), request.getParameter("password"));
                if (user != null) {
                    request.getSession().setAttribute("User", user);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "Email or password is incorrect.<br/>Please try again.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
            response.sendRedirect("/login.jsp");
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

    void getUser (HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String user_id = request.getParameter("user_id");
        User user = cont.getUser(user_id);
        String user_info = user.toString();
        request.setAttribute("userInfo", user_info);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    void createProjectRequest(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String project_body = request.getParameter("project_body");
        String budget = request.getParameter("budget");

        if (cont.createProjectRequest(budget, project_body)) {
            request.setAttribute("submitCheck", true);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
        request.setAttribute("submitCheck", false);
        request.getRequestDispatcher("index.jsp").forward(request, response);

    }
}
