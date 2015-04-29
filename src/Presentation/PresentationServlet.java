package Presentation;

import Domain.Budget;
import Domain.Company;
import Domain.Controller;
import Domain.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

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

            getActiveBudget(request, response, cont);

            if(userPath.indexOf("/resources/") == 0) {
                serveResource(request, response, cont);
            } else {

                switch (userPath) {
                    case "/dashboard":
                        getDashboard(request, response, cont);
                        break;
                    case "/partners":
                        getPartners(request, response, cont);
                        break;
                    case "/partner":
                        getPartnerView(request, response, cont);
                        break;
                    case "/users":
                        getUsers(request, response, cont);
                        break;
                    case "/user":
                        getUserView(request, response, cont);
                        break;
                    case "/project-request":
                        request.getRequestDispatcher("/WEB-INF/view/createproject.jsp").forward(request, response);
                        break;
                    case "/project":
                        getProjectView(request, response, cont);
                        break;
                    case "/search":
                        search(request, response, cont);
                    case "/create-company":
                        getCreateCompanyView(request, response, cont);
                        break;
                    case "/getCompanyNames":
                        getCompanyNames(request, response, cont);
                        break;
                    case "/getStatuses":
                        getDistinctStatuses(request, response, cont);
                        break;
                    case "/getTypes":
                        getDistinctTypes(request, response, cont);
                        break;
                    case "/create-user":
                        getCreateUserView(request, response, cont);
                        break;
                    case "/logout":
                        logout(request, response, cont);
                        break;
                    case "/budget_view":
                        getBudgetView(request, response, cont);
                        break;
                    default:
                        response.sendRedirect("/dashboard");
                        break;
                }
            }
        } else {
            String userpath = request.getRequestURI();
            System.out.println(userpath);
            if(userpath.equals("/login"))
                request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
            else if(userpath.equals("/reset-password"))
                getCreatePasswordView(request, response, cont);
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

        getActiveBudget(request, response, cont);

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
                break;
            case "/uploadFile":
                createPoe(request, response, cont);
                break;
            case "/downloadFile":
                getPoes(request, response, cont);
                break;
            case "/api/deleteFile":
                deletePoe(request, response, cont);
                break;
            case "/createUser":
                createUser(request, response, cont);
                break;
            case "/reset-password":
                createPassword(request, response, cont);
                break;
            case "/createBudget":
                createBudget(request, response, cont);
                break;
            case "/modifyBudget":
                modifyBudget(request, response, cont);
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
        } else {
            request.setAttribute("message", "Incorrect login");
            request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
        }

    }

    void getDashboard(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getDashboard");

        User user = (User) request.getAttribute("User");
        System.out.println("state: " + request.getParameter("state"));
        System.out.println("type: " + request.getParameter("type"));
        if(request.getParameter("state") != null)
            request.setAttribute("projects", cont.getProjectsByState(request.getParameter("state"), user.getCompany_id()));
        else if (request.getParameter("type") != null)
            request.setAttribute("projects", cont.getProjectsByType(request.getParameter("type"), user.getCompany_id()));
        else if (request.getParameter("company") != null)
            request.setAttribute("projects", cont.getProjectsByCompanyName(request.getParameter("company"), user.getCompany_id()));
        else
            request.setAttribute("projects", cont.getProjectsByState("waitingForAction", user.getCompany_id()));


        request.setAttribute("statusCount", cont.getStatusCounts(user.getCompany_id()));

        request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
    }

    void getPartners(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getPartners");
        User user = (User) request.getAttribute("User");
        if (user.getCompany_id() != 1) {
            response.sendRedirect("/dashboard");
        } else {
            request.setAttribute("partners", cont.getCompanies());
            request.getRequestDispatcher("/WEB-INF/view/partners.jsp").forward(request, response);
        }
    }

    void getUsers(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getUsers");
        User user = (User) request.getAttribute("User");
        if (user.getCompany_id() != 1) {
            response.sendRedirect("/dashboard");
        } else {
            request.setAttribute("users", cont.getUsers());
            request.getRequestDispatcher("/WEB-INF/view/users.jsp").forward(request, response);
        }
    }

    void getProjectView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getProjectView");
        User user = (User) request.getAttribute("User");
        int projId = Integer.parseInt(request.getParameter("id"));
        Object proj = cont.getProjectById(projId, user.getCompany_id());
        if(proj == null) {
            error("Project not found", request, response, cont);
            return;
        }
        request.setAttribute("project", proj);
        request.setAttribute("messages", cont.getMessagesByProjectId(projId));
        request.setAttribute("stages", cont.getStagesByProjectId(projId));
        request.setAttribute("poes", cont.getPoe(projId));

        request.getRequestDispatcher("/WEB-INF/view/project.jsp").forward(request, response);
    }

    void getPartnerView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        User user = (User) request.getAttribute("User");
        if (user.getCompany_id() != 1) {
            response.sendRedirect("/dashboard");
        } else {
            int partnerId = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("partner", cont.getCompanyById(partnerId));
            request.setAttribute("users", cont.getUsersByCompanyId(partnerId));
            request.setAttribute("projects", cont.getProjectsByCompanyId(partnerId));

            request.getRequestDispatcher("/WEB-INF/view/partner.jsp").forward(request, response);
        }
    }

    void getUserView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getUserView");
        User user = (User) request.getAttribute("User");
        if (user.getCompany_id() != 1) {
            response.sendRedirect("/dashboard");
        } else {
            int userId = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("user", cont.getUserById(userId));

            request.getRequestDispatcher("/WEB-INF/view/user.jsp").forward(request, response);
        }
    }

    void postMessage(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("postMessage");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


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

        int projectId = cont.createProjectRequest(budget, project_body, user, project_type, execution_time);

        if (projectId != 0) {
            response.sendRedirect("/project?id=" + projectId);
        } else {
            System.out.println("Project ID is 0!");
        }
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

        response.sendRedirect("/project?id=" + projectId);
    }

    void logout(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            response.sendRedirect("/login");
        }
    }


    void createPoe(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        Part file = request.getPart("file");
        User u = (User) request.getAttribute("User");
        int project_id = Integer.parseInt(request.getParameter("proj_id"));
        int user_id = u.getId();
        int stage = -1;
        if(request.getParameter("stage") != null)
            stage = Integer.parseInt(request.getParameter("stage"));
        if(cont.addPoeFile(project_id, file, user_id, stage)) {
            response.sendRedirect("/project?id="+project_id);
        }
    }


    void deletePoe(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        String fileName = request.getParameter("fileName");
        int fileId = Integer.parseInt(request.getParameter("fileId"));
        boolean deleteFile = Boolean.parseBoolean(request.getParameter("deleteFile"));

        cont.deleteFile(fileName, projectId, fileId, deleteFile);

        response.sendRedirect("/project?id=" + projectId);
    }


    void getCreateCompanyView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        ArrayList<Company> companies = cont.getCompanies();
        request.setAttribute("companies", companies);
        request.getRequestDispatcher("/WEB-INF/view/create-company.jsp").forward(request, response);
    }

    void getCreateUserView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        ArrayList<Company> companies = cont.getCompanies();
        request.setAttribute("companies", companies);
        request.getRequestDispatcher("/WEB-INF/view/create-user.jsp").forward(request, response);
    }

    void createCompany(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String company_name = request.getParameter("companyName");
        String country_code = request.getParameter("countryCode");
        Part logo = null;
        if(request.getParameter("logo") != null)
            logo = request.getPart("logo");
        String logo_url = request.getParameter("logoUrl");

        int company_id = cont.createCompany(company_name, country_code, logo, logo_url);
        if (company_id > 0) { //if success
            //request.setAttribute("message", "Create the first user for this company by clicking 'Add user'");
            setMessage("Create the first user for this company by clicking 'Add user'", request);
            response.sendRedirect("/partner?id=" + company_id);
        } else {
            request.setAttribute("error", "Something went wrong, try again");
            request.getRequestDispatcher("/WEB-INF/view/create-company.jsp").forward(request, response);
        }
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

            String[] path = userpath.split("/");
            String filename = System.getenv("POE_FOLDER");

            for (int i=2; i < path.length; i++) {
                filename+= File.separator + path[i];
            }

            //String filename = System.getenv("POE_FOLDER") + File.separator + userpath.split("/")[2] + File.separator + userpath.split("/")[3];
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

    void createUser(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {

        String name = request.getParameter("userName");
        String role = request.getParameter("role");
        String email = request.getParameter("userEmail");
        String password = request.getParameter("password");
        int company_id;
        if (role.equals("Dell")) {
            company_id = 1;
        } else {
            company_id = Integer.parseInt(request.getParameter("selectedCompany"));
        }
        cont.createUser(name, role, email, password, company_id);

        response.sendRedirect("/create-user");

    }

    void createBudget(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int year = Integer.parseInt(request.getParameter("year"));
        int quarter = Integer.parseInt(request.getParameter("quarter"));
        int budget = Integer.parseInt(request.getParameter("initial_budget"));

        if (cont.addBudget(year, quarter, budget)) {
            cont.sendEmail("noobglivestream@gmail.com", "budget created", "hope this works!");
            getBudgetView(request, response, cont);
        } else {
            request.setAttribute("errorMes", "Quarter already exists, consider modifying the current budget or creating a new one.");
            response.sendRedirect("/budget_view");
        }
    }

    void getBudgetView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {

        request.setAttribute("budgetCollection", cont.getAllBudgets());

        getActiveBudget(request, response, cont);

        request.getRequestDispatcher("/WEB-INF/view/budget_view.jsp").forward(request, response);
    }


    void getDistinctStatuses(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(cont.getDistinctStatuses(request.getParameter("query")));
    }
    void getDistinctTypes(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        User user = (User) request.getAttribute("User");
        out.print(cont.getDistinctTypes(request.getParameter("query"), user.getCompany_id()));
    }
    void getCompanyNames(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        User user = (User) request.getAttribute("User");
        out.print(cont.getCompanyNames(request.getParameter("query"), user.getCompany_id()));
    }

    void search(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String q = request.getParameter("q");
        User user = (User) request.getAttribute("User");
        request.setAttribute("results", cont.search(q, user.getCompany_id()));
        request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
    }

    void error(String error, HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        request.setAttribute("error", error);
        getDashboard(request, response, cont);
    }

    void getActiveBudget(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        int currentQuarter = (currentMonth / 3 ) + 1;

        ArrayList<Budget> budgets = cont.getActiveBudget(currentYear, currentQuarter);
        if (budgets.isEmpty()) {
            request.setAttribute("activeBudget", null);
        } else {
            request.setAttribute("activeBudget", cont.getActiveBudget(currentYear, currentQuarter));
        }


    }

    void modifyBudget(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int newBudget = Integer.parseInt(request.getParameter("newBudget"));
        int year = Integer.parseInt(request.getParameter("year"));
        int quarter = Integer.parseInt(request.getParameter("quarter"));

        cont.modifyBudget(newBudget, year, quarter);

        response.sendRedirect("/budget_view");
    }

    void getCreatePasswordView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String nonce = request.getParameter("n");
        int userId = cont.getUserIdByNonce(nonce);
        request.setAttribute("userId", userId);

        request.getRequestDispatcher("/WEB-INF/view/reset.jsp").forward(request, response);
    }

    void createPassword(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String nonce = request.getParameter("nonce");
        String password = request.getParameter("pw");
        int id = Integer.parseInt(request.getParameter("user_id"));

        if(cont.createPassword(id, password, nonce)) {
            request.getSession().setAttribute("User", cont.getUserById(id));
            response.sendRedirect("/dashboard");
        } else {
            request.setAttribute("error", "Something went wrong, try recovering password again");
            request.getRequestDispatcher("/WEB-INF/view/reset.jsp").forward(request, response);
        }
    }

    void setMessage(String message, HttpServletRequest request) {
        request.getSession().setAttribute("message", message);
    }
    void setError(String error, HttpServletRequest request) {
        request.getSession().setAttribute("error", error);
    }

}
