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

        // if logged in
        Object userObj = request.getSession().getAttribute("User");
        if (userObj != null) {
            request.setAttribute("User", userObj); // passing user object to request

            switch (action) {
                case "getUser":
                    getUser(request, response, cont);
                    break;
                case "createProjectRequest":
                    createProjectRequest(request, response, cont);
                    break;
                case "getProjectsByState":
                    getProjectsByState(request, response, cont);
                    break;
                case "verifyProjectRequestByProjectId":
                    verifyProjectRequestByProjectId(request, response, cont);
                    break;
                default:
                    String url = request.getRequestURI();

                    if(url.equals("/logout")) {
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            session.invalidate();
                            request.getRequestDispatcher("login.jsp").forward(request, response);
                        }
                    } else {
                        getDashboard(request, response, cont);
                    }
                }
            } else {
                if (action.equals("login"))
                    login(request, response, cont);
                else
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

    void login(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        Object user = cont.login(request.getParameter("email"), request.getParameter("password"));
        if (user != null) {
            request.getSession().setAttribute("User", user);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            request.setAttribute("message", "Incorrect password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    void getDashboard(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getDashboard");
        User user = (User) request.getAttribute("User");
        if(request.getParameter("state") == null)
            request.setAttribute("projects", cont.getProjectsByState("waitingForAction", user.getCompany_id()));
        else
            request.setAttribute("projects", cont.getProjectsByState(request.getParameter("state"), user.getCompany_id()));;

        request.getRequestDispatcher("index.jsp").forward(request, response);
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
            return;
        }
    }
    void getProjectsByState(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        User user = (User) request.getAttribute("User");
        request.setAttribute("projects", cont.getProjectsByState(request.getParameter("state"), user.getCompany_id()));
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    void verifyProjectRequestByProjectId(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String project_id = request.getParameter("project_id");
        if (cont.verifyProjectRequest(project_id)) {
            request.setAttribute("verificationCheck", true);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        request.setAttribute("verificationCheck", false);
        request.getRequestDispatcher("index.jsp").forward(request, response);

    }
}
