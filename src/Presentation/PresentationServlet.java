package Presentation;

import Domain.Controller;
import Domain.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;

@MultipartConfig
public class PresentationServlet extends HttpServlet {


    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Controller cont = AssignController(request);

        System.out.println(request.getRequestURI());

        // if logged in
        Object userObj = request.getSession().getAttribute("User");
        if (userObj != null) {
            request.setAttribute("User", userObj); // passing user object to request

            String userPath = request.getServletPath();
            System.out.println(userPath);

            if(userPath.indexOf("/resources/") == 0) {
                serveResource(request, response, cont);
            } else {

                switch (userPath) {
                    case "/dashboard":
                        getDashboard(request, response, cont);
                        break;
                    case "/project-request":
                        request.getRequestDispatcher("/WEB-INF/view/createproject.jsp").forward(request, response);
                        break;
                    case "/project":
                        getProjectView(request, response, cont);
                        break;
                    case "/create-project":
                        getCreateProjectView(request, response, cont);
                        break;
                    case "/logout":
                        logout(request, response, cont);
                        break;
                    default:
                        response.sendRedirect("/dashboard");
                        break;
                }
            }
        } else {
            System.out.println(request.getRequestURI());
            if(request.getRequestURI().equals("/login"))
                request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
            else
                response.sendRedirect("/login");
        }
    }


    protected void processPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Controller cont = AssignController(request);
        String path = request.getRequestURI();

        System.out.println(path);

        Object userObj = request.getSession().getAttribute("User");
        if (userObj != null)
            request.setAttribute("User", userObj); // passing user object to request

        switch (path) {
            case "/login":
                login(request, response, cont);
                break;
            case "/api/getUserById":
                getUserById(request, response, cont);
                break;
            case "/api/postMessage":
                postMessage(request, response, cont);
                break;
            case "/api/getProjectsByState":
                getProjectsByState(request, response, cont);
                break;
            case "/api/changeProjectStatus":
                changeProjectStatus(request, response, cont);
                break;
            case "/api/createCompany":
                createCompany(request, response, cont);
                break;
            case "/api/createUser":
                createUser(request, response, cont);
                break;
            case "/project-request":
                createProjectRequest(request, response, cont);
                //changeProjectStatus(request, response, cont);
                break;
            case "/uploadFile":
                createPoe(request, response, cont);
                break;
            case "/downloadFile":
                getPoes(request, response, cont);
                break;
            default:
                getDashboard(request, response, cont);
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
        processPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    void login(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        Object user = cont.login(request.getParameter("email"), request.getParameter("password"));
        if (user != null) {
            request.getSession().setAttribute("User", user);
            response.sendRedirect("/dashboard");
            //request.setAttribute("User", user);
            //getDashboard(request, response, cont);
        } else {
            request.setAttribute("message", "Incorrect login");
            request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
        }
    }

    void getDashboard(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getDashboard");

        User user = (User) request.getAttribute("User");
        if(request.getParameter("state") == null)
            request.setAttribute("projects", cont.getProjectsByState("waitingForAction", user.getCompany_id()));
        else
            request.setAttribute("projects", cont.getProjectsByState(request.getParameter("state"), user.getCompany_id()));;

        request.setAttribute("statusCount", cont.getStatusCounts(user.getCompany_id()));

        request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
    }

    void getProjectView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getProjectView");
        User user = (User) request.getAttribute("User");
        int projId = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("project", cont.getProjectById(projId, user.getCompany_id()));;
        request.setAttribute("messages", cont.getMessagesByProjectId(projId));
        request.setAttribute("stages", cont.getStagesByProjectId(projId));
        request.setAttribute("poes", cont.getPoe(projId));

        request.getRequestDispatcher("/WEB-INF/view/project.jsp").forward(request, response);
    }

    void postMessage(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("postMessage");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String newLine = System.getProperty("line.separator");

        int userId = Integer.parseInt(request.getParameter("userId"));
        int companyId = Integer.parseInt(request.getParameter("companyId"));
        int projectId = Integer.parseInt(request.getParameter("projectId"));

        String body = request.getParameter("body");

        

        out.println(cont.postMessage(userId, projectId, body, companyId));
    }

    void getUserById (HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        User user = cont.getUserById(user_id);
        String user_info = user.toString();
        request.setAttribute("userInfo", user_info);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    void createProjectRequest(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String project_body = request.getParameter("body");
        String budget = request.getParameter("budget");
        String project_type = request.getParameter("type");

        if (project_type.equals("other")) {
            project_type = request.getParameter("customType");
        }

        int execution_year = Integer.parseInt(request.getParameter("execution_year"));
        int execution_month = Integer.parseInt(request.getParameter("execution_month"));
        int execution_day = Integer.parseInt(request.getParameter("execution_day"));

        Timestamp execution_time;

        if (execution_day == 0) {
            execution_time = Timestamp.valueOf(execution_year + "-" + execution_month + "-01" + " 00:00:01");
        } else {
            execution_time = Timestamp.valueOf(execution_year + "-" + execution_month + "-" + execution_day + " 00:00:00");
        }


        Object userObj = request.getSession().getAttribute("User");
        if (userObj != null) {
            request.setAttribute("User", userObj);}
            User user = (User) request.getAttribute("User");

        if (cont.createProjectRequest(budget, project_body, user, project_type, execution_time)) {
            request.getRequestDispatcher("/WEB-INF/view/createproject.jsp").forward(request, response);
        }
        response.sendRedirect("/");

    }

    void getProjectsByState(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        User user = (User) request.getAttribute("User");
        request.setAttribute("projects", cont.getProjectsByState(request.getParameter("state"), user.getCompany_id()));
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }



    void changeProjectStatus(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        String currentType = request.getParameter("currentType");
        String answer = request.getParameter("answer");
        User u = (User) request.getAttribute("User");
        int companyId = u.getCompany_id();
        int userId = u.getId();

        cont.changeProjectStatus(projectId, currentType, answer, companyId, userId);

        response.sendRedirect("/dashboard");
    }

    void logout(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            response.sendRedirect("/login");
        }
    }


    void createCompany(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String company_name = request.getParameter("company_name");

        if (cont.createCompany(company_name)) {
            request.setAttribute("createCompanyResult", true);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        request.setAttribute("createCompanyResult", false);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }


    // Creates a new user; if a given company name already exists, assign user to that company - otherwise make new company with that name.
    void createUser(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String name = request.getParameter("name");
        String user_role = request.getParameter("user_role");
        String user_email = request.getParameter("user_email");
        String password = request.getParameter("password");
        // String company_name = request.getParameter("company_name");

                if (cont.createUser(name, user_role, user_email, password, 1)) {
                    request.setAttribute("createUserResult", true);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;
                }

        request.setAttribute("createUserResult", false);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
    void createPoe(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        Part file = request.getPart("file");
        User u = (User) request.getAttribute("User");
        int project_id = Integer.parseInt(request.getParameter("proj_id"));
        int user_id = u.getId();
        if(cont.addPoeFile(project_id, file, user_id)) {
            response.sendRedirect("/project?id="+project_id);
        }
    }

    void getCreateProjectView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        request.setAttribute("companies", "ayy");
        request.getRequestDispatcher("/WEB-INF/view/create-company.jsp").forward(request, response);
    }

    void getPoes(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int project_id = Integer.parseInt(request.getParameter("proj_id"));
        String filename = URLDecoder.decode(request.getParameter("filename"));

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + filename);

        // testing first poe
        String path = System.getenv("POE_FOLDER") + File.separator + project_id + File.separator + filename;

        File file = new File(path);
        FileInputStream fileIn = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();

        byte[] outputByte = new byte[4096];

        while(fileIn.read(outputByte, 0, 4096) != -1)
        {
            out.write(outputByte, 0, 4096);
        }
        fileIn.close();
        out.flush();
        out.close();


         response.sendRedirect("/");

    }

    void serveResource(HttpServletRequest request, HttpServletResponse response, Controller cont) {
        try {
            String userpath = request.getServletPath();
            boolean download = Boolean.parseBoolean(request.getParameter("download"));

            String filename = System.getenv("POE_FOLDER") + File.separator + userpath.split("/")[2] + File.separator + userpath.split("/")[3];
            File file = new File(filename);

            if(download) {
                response.setContentType("application/force-download");
                //response.setContentLength(-1);
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setHeader("Content-Disposition","attachment; filename=\"" + file.getName() + "\"");
            } else {
                ServletContext cntx= getServletContext();

                // retrieve mimeType dynamically
                String mime = cntx.getMimeType(filename);
                if (mime == null) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
                response.setContentType(mime);
            }


            response.setContentLength((int)file.length());

            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            out.close();
            in.close();
        } catch (Exception e) {};

    }
}
