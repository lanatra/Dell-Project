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

import static org.apache.commons.lang3.StringEscapeUtils.*;
import static org.apache.commons.lang3.StringUtils.*;

@MultipartConfig
public class PresentationServlet extends HttpServlet {

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Controller cont = AssignController(request);

        System.out.println(request.getRequestURI());

        //request.setAttribute("error", false);

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
                    case "/budgets":
                        getBudgets(request, response, cont);
                        break;
                    case "/edit-budget":
                        getEditBudget(request, response, cont);
                        break;
                    case "/project-request":
                        request.getRequestDispatcher("/WEB-INF/view/createproject.jsp").forward(request, response);
                        break;
                    case "/project":
                        getProjectView(request, response, cont);
                        break;
                    case "/create-company":
                        getCreateCompanyView(request, response, cont);
                        break;
                    case "/getCompanyNames":
                        getCompanyNames(request, response, cont);
                        break;
                    case "/statistics":
                        getStatisticsView(request, response, cont);
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
                    case "/create-budget":
                        request.getRequestDispatcher("/WEB-INF/view/create-budget.jsp").forward(request, response);
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

        //request.setAttribute("error", false);

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
            case "/markUserDeleted":
                deleteUser(request, response, cont);
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
        Object user = cont.login(getString("email", request), getString("password", request));
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

        //if search
        if(getLazyString("q", request) != null) {
            String q = getLazyString("q", request);
            request.setAttribute("results", cont.search(q, user.getCompany_id()));
        } else {
            if(getLazyString("state", request) != null)
                request.setAttribute("projects", cont.getProjectsByState(getLazyString("state", request), user.getCompany_id()));
            else if (getLazyString("type", request) != null)
                request.setAttribute("projects", cont.getProjectsByType(getLazyString("type", request), user.getCompany_id()));
            else if (getLazyString("company", request) != null)
                request.setAttribute("projects", cont.getProjectsByCompanyName(getLazyString("company", request), user.getCompany_id()));
            else
                request.setAttribute("projects", cont.getProjectsByState("waitingForAction", user.getCompany_id()));
        }

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

    void getBudgets(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getBudgets");
        User user = (User) request.getAttribute("User");
        if (user.getCompany_id() != 1) {
            response.sendRedirect("/dashboard");
        } else {
            request.setAttribute("budgets", cont.getAllBudgets());
            request.getRequestDispatcher("/WEB-INF/view/budgets.jsp").forward(request, response);
        }
    }

    void getEditBudget(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        request.setAttribute("initialbudget", getString("initialbudget", request));
        int year = getInt("year", request);
        request.setAttribute("year", year);
        int quarter = getInt("quarter", request);
        request.setAttribute("quarter", quarter);

        request.getRequestDispatcher("/WEB-INF/view/edit-budget.jsp").forward(request, response);
    }

    void getProjectView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("getProjectView");
        User user = (User) request.getAttribute("User");
        int projId = getInt("id", request);
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
            int partnerId = getInt("id", request);
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
            int userId = getInt("id", request);
            User tempUser = cont.getUserById(userId);
            request.setAttribute("user", tempUser);
            try {
                Company company = cont.getCompanyById(tempUser.getCompany_id());
                request.setAttribute("partner", company);
                request.setAttribute("projects", cont.getProjectsByUserId(userId));
            } catch (NullPointerException e) {
                getUsers(request, response, cont);
                return;
            }

            request.getRequestDispatcher("/WEB-INF/view/user.jsp").forward(request, response);
        }
    }

    void postMessage(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        System.out.println("postMessage");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int userId = getInt("userId", request);
        int companyId = getInt("companyId", request);
        int projectId = getInt("projectId", request);

        String body = getString("body", request);

        if(request.getAttribute("error") != null)
            out.println("Error - Invalid input");
        else
            out.println(cont.postMessage(userId, projectId, body, companyId));
    }

    void getUserById (HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int user_id = getInt("user_id", request);
        User user = cont.getUserById(user_id);
        String user_info = user.toString();
        request.setAttribute("userInfo", user_info);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    void createProjectRequest(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String project_body = getString("body", request);
        int budget = getInt("budget", request);
        String project_type = getString("type", request);


        int execution_year = getInt("execution_year", request);
        int execution_month = getInt("execution_month", request);
        int execution_day = getInt("execution_day", request);

        Timestamp execution_time;

        if (execution_day == 0) {
            execution_time = Timestamp.valueOf(execution_year + "-" + execution_month + "-01" + " 00:00:01");
        } else {
            execution_time = Timestamp.valueOf(execution_year + "-" + execution_month + "-" + execution_day + " 00:00:00");
        }
        System.out.println("error?: " + request.getAttribute("error"));
        if(request.getAttribute("error") != null) {
            ArrayList<String[]> formData = new ArrayList<>();
            formData.add(new String[]{"body", project_body.replaceAll("\r\n", "\\\\n")});
            formData.add(new String[] {"budget", request.getParameter("budget")});
            formData.add(new String[] {"type", project_type});
            formData.add(new String[] {"execution_year", String.valueOf(execution_year)});
            formData.add(new String[] {"execution_month", String.valueOf(execution_month)});
            formData.add(new String[] {"execution_day", String.valueOf(execution_day)});

            request.getSession().setAttribute("formData", formData);
            response.sendRedirect("/project-request");
        } else {
            User user = (User) request.getAttribute("User");

            int projectId = cont.createProjectRequest(budget, project_body, user, project_type, execution_time);

            if (projectId != 0) {
                response.sendRedirect("/project?id=" + projectId);
            } else {
                System.out.println("Project ID is 0!");
            }
        }
    }

    void getProjectsByState(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        User user = (User) request.getAttribute("User");
        request.setAttribute("projects", cont.getProjectsByState(getString("state", request), user.getCompany_id()));
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }



    void changeProjectStatus(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int projectId = getInt("projectId", request);
        String currentType = getString("currentType", request);
        String answer = getString("answer", request);
        User u = (User) request.getAttribute("User");
        int companyId = u.getCompany_id();
        int userId = u.getId();

        if(request.getAttribute("error") == null)
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
        int project_id = getInt("proj_id", request);
        int user_id = u.getId();
        int stage = -1;
        if(getString("stage", request) != null)
            stage = getInt("stage", request);
        if(cont.addPoeFile(project_id, file, user_id, stage)) {
            response.sendRedirect("/project?id=" + project_id);
        }
    }


    void deletePoe(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int projectId = getInt("projectId", request);
        String fileName = getString("fileName", request);
        int fileId = getInt("fileId", request);
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
        request.setAttribute("partnerName", request.getParameter("partnerName"));
        request.getRequestDispatcher("/WEB-INF/view/create-user.jsp").forward(request, response);
    }

    void createCompany(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String company_name = getString("companyName", request);
        String country_code = getString("countryCode", request);
        Part logo = null;
        if(request.getParameter("logo") != null)
            logo = request.getPart("logo");
        String logo_url = getLazyString("logoUrl", request);

        
        if(request.getAttribute("error") == null) {
            int company_id = cont.createCompany(company_name, country_code, logo, logo_url);
            if (company_id > 0) { //if success
                //request.setAttribute("message", "Create the first user for this company by clicking 'Add user'");
                setMessage("Create the first user for this company by clicking 'Add user'", request);
                response.sendRedirect("/partner?id=" + company_id);
            } else {
                request.setAttribute("error", "Something went wrong, try again");
                request.getRequestDispatcher("/WEB-INF/view/create-company.jsp").forward(request, response);
            }
        } else
            response.sendRedirect("/create-company");
    }

    void getPoes(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int project_id = getInt("proj_id", request);
        String filename = URLDecoder.decode(getString("filename", request));

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
        String name = getString("userName", request);
        String email = getString("userEmail", request);
        int company_id = getInt("selectedCompany", request);
        if(request.getAttribute("error") == null) {
            int id = cont.createUser(name, email, company_id);
            if (id == -1) {
                setError("Something went wrong.", null, request);
                response.sendRedirect("/create-user");
            } else
                response.sendRedirect("/user?id=" + id);
        } else {
            ArrayList<String[]> formData = new ArrayList<>();
            formData.add(new String[] {"userName", name});
            formData.add(new String[] {"userEmail", email});
            formData.add(new String[] {"selectedCompany", String.valueOf(company_id)});
            request.getSession().setAttribute("formData", formData);
            response.sendRedirect("/create-user");
        }

    }

    void createBudget(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int year = getInt("year", request);
        int quarter = getInt("quarter", request);
        int budget = getInt("initial_budget", request);

        if(request.getAttribute("error") == null) {
            if (cont.addBudget(year, quarter, budget)) {
                cont.sendEmail("noobglivestream@gmail.com", "budget created", "hope this works!");
                response.sendRedirect("/budgets");
            } else {
                request.setAttribute("errorMes", "Quarter already exists, consider modifying the current budget or creating a new one.");
                response.sendRedirect("/budget_view");
            }
        } else
            response.sendRedirect("/create-budget");
    }

    void getStatisticsView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        request.setAttribute("statistics", cont.getStatistics(getLazyString("quarter", request)));
        request.setAttribute("budgets", cont.getAllBudgets());
        request.getRequestDispatcher("/WEB-INF/view/statistics.jsp").forward(request, response);
    }


    void getDistinctStatuses(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(cont.getDistinctStatuses(getString("query", request)));
    }
    void getDistinctTypes(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        User user = (User) request.getAttribute("User");
        out.print(cont.getDistinctTypes(getString("query", request), user.getCompany_id()));
    }
    void getCompanyNames(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        User user = (User) request.getAttribute("User");
        out.print(cont.getCompanyNames(getString("query", request), user.getCompany_id()));
    }


    void error(String error, HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        request.setAttribute("error", error);
        getDashboard(request, response, cont);
    }

    void getActiveBudget(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        System.out.println(currentMonth);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentQuarter = (currentMonth / 3 ) + 1;

        request.setAttribute("activeBudget", cont.getActiveBudget(currentYear, currentQuarter));



    }

    void modifyBudget(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int newBudget = getInt("newBudget", request);
        int year = getInt("year", request);
        int quarter = getInt("quarter", request);


        if(request.getAttribute("error") == null) {
            cont.modifyBudget(newBudget, year, quarter);
        }
        response.sendRedirect("/budget_view");
    }

    void getCreatePasswordView(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String nonce = getString("n", request);
        int userId = cont.getUserIdByNonce(nonce);
        request.setAttribute("userId", userId);

        request.getRequestDispatcher("/WEB-INF/view/reset.jsp").forward(request, response);
    }

    void createPassword(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        String nonce = getString("nonce", request);
        String password = getString("pw", request);
        int id = getInt("user_id", request);

        if(request.getAttribute("error") == null) {

            if (cont.createPassword(id, password, nonce)) {
                request.getSession().setAttribute("User", cont.getUserById(id));
                response.sendRedirect("/dashboard");
            } else {
                request.setAttribute("error", "Something went wrong, try recovering password again");
                request.getRequestDispatcher("/WEB-INF/view/reset.jsp").forward(request, response);
            }
        } else
            response.sendRedirect("/reset-password");
    }

    String getString(String p, HttpServletRequest request) {
        String s = request.getParameter(p);
        if(s == null || s.length() == 0 || s.equals("")) {
            setError("Empty field", p, request);
            request.setAttribute("error", true);
        }
        s = escapeHtml4(s);
        return s;
    }
    String getLazyString(String p, HttpServletRequest request) {
        return escapeHtml4(request.getParameter(p));
    }
    int getInt(String p, HttpServletRequest request) {
        String s = request.getParameter(p);
        if(s == null || s.length() == 0 || s.equals("")) {
            request.setAttribute("error", true);
            setError("Missing field", p, request);
        }
        int i = -1;
        if(isNumeric(s))
            i = Integer.parseInt(s);
        else {
            request.setAttribute("error", true);
            setError("Enter only numbers ",p , request);
        }

        return i;
    }

    void setMessage(String message, HttpServletRequest request) {
        request.getSession().setAttribute("message", message);
    }
    void setError(String error, String field, HttpServletRequest request) {
        String e = (String) (request.getSession().getAttribute("errorMessage"));
        if(e == null)
            request.getSession().setAttribute("errorMessage", error + "|" + field);
        else
            request.getSession().setAttribute("errorMessage", e + "," + field);
    }

    void deleteUser(HttpServletRequest request, HttpServletResponse response, Controller cont) throws ServletException, IOException {
        int viewedUser = Integer.parseInt(request.getParameter("viewedUser"));

        cont.markUserDeleted(viewedUser);
        response.sendRedirect("/users");
    }

}
