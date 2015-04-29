package Domain;

import DataLayer.BudgetMapper;
import DataLayer.DatabaseFacade;


import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.http.Part;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    DatabaseFacade facade;
    IdGenerator gen;

    public Controller() {

        this.facade = new DatabaseFacade();
        this.gen = new IdGenerator();

    }
    // Writers
    public int createProjectRequest(String budget, String project_body, User user, String project_type, Timestamp execution_date) {
        return facade.createProjectRequest(budget, project_body, user, project_type, execution_date);
    }
    public int createCompany(String company_name, String country_code, Part logo, String logo_url) {
        int company_id = facade.createCompany(company_name, country_code);
        if(company_id != -1) { //if success
            String filename = "";
            if(logo != null) {
                FileHandling handler = new FileHandling();

                try {
                    handler.putLogo(logo, company_id);
                } catch(Exception e) {}

                filename = handler.getFileName();
                facade.updateCompanyLogo(filename, company_id);
            } else if(logo_url != null) {
                System.out.println(new File(System.getenv("POE_FOLDER") + File.separator + "companies" + File.separator + company_id + File.separator + "logo." + logo_url.substring(logo_url.lastIndexOf(".") + 1, logo_url.length())).getPath());
                try {
                    URL website = new URL(logo_url);
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    File outputFolder = new File(System.getenv("POE_FOLDER") + File.separator + "companies" + File.separator + company_id );
                    outputFolder.mkdirs();
                    filename = "logo." + logo_url.substring(logo_url.lastIndexOf(".") + 1, logo_url.length());
                    FileOutputStream fos = new FileOutputStream(
                            new File(outputFolder.getAbsolutePath() + File.separator + filename));
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Error in saving logo from url");
                }
                facade.updateCompanyLogo(filename, company_id);
            }


            return company_id;
        } else {
            return -1;
        }
    }

    public boolean changeProjectStatus(int project_id, String current_status, String answer, int companyId, int userId) {
        String new_status = "";
        if(answer.equals("approved")) {
            if(current_status.equals("Waiting Project Verification"))
                new_status = "Project Approved";
            else if(current_status.equals("Project Approved"))
                new_status = "Waiting Claim Verification";
            else if(current_status.equals("Waiting Claim Verification"))
                new_status = "Project Finished";
            else if(current_status.equals("Claim Rejected"))
                new_status = "Waiting Claim Verification";

        } else if(answer.equals("denied")) {
            if(current_status.equals("Waiting Project Verification"))
                new_status = "Project Rejected";
            else if(current_status.equals("Waiting Claim Verification"))
                new_status = "Claim Rejected";

        } else if(answer.equals("cancelled")) {
            new_status = "Cancelled";
        }

        if(facade.changeProjectStatus(project_id, new_status, companyId, userId)) {
            ArrayList<String> emails = facade.getEmailsInvolvedInProjectById(project_id, userId);

            for (String email : emails) {
                if(email.matches("^(\\w)+@(\\w)+\\.(\\w){2,}$"))
                    sendEmail(email, "New Status for project #" + project_id, "Project #" + project_id + " has advanced to a new step, " + new_status + "\n" +
                        "Click here to review the project: http://localhost:8080/project?id="+project_id);
            }
            return true;
        } else
            return false;

    }
    //User related
    public boolean createUser(String name, String user_role, String user_email, String password, int company_id) {
        if(password != null)
            password = Login.createPassword(password);

        int id = facade.createUser(name, user_role, user_email, password, company_id);

        if(id != -1) {
            if (password == null)
                createPasswordResetNonce(id, user_email);
        }

        return (id != -1);
    }

   //Readers
    //User related
    public User getUserById(int user_id) { return facade.getUserById(user_id); }
    public ArrayList<User> getUsersByCompanyId(int company_id) {
        return facade.getUsersByCompanyId(company_id);
    }
    public ArrayList<User> getUsers() {return facade.getUsers();}


    //Project related
    public DisplayProject getProjectById(int id, int companyId) {
        DisplayProject dp = facade.getProjectById(id, companyId);
        if(dp == null)
            return null;
        if((companyId == 1 && dp.isUnread_admin()) || (companyId != 1 && dp.isUnread_partner()))
            facade.markRead(id, companyId);
        return  dp; }
    public ArrayList getStagesByProjectId(int project_id) {
        return proccessStages(facade.getStagesByProjectId(project_id)); }
    public ArrayList getMessagesByProjectId(int projId) { return processMessages(facade.getMessagesByProjectId(projId)); }

    public String postMessage(int userId, int projId, String body, int companyId) { return processMessage(facade.postMessage(userId, projId, body, companyId)).toHTML();}
    public ArrayList getProjectsByState(String state, int companyId) { return  facade.getProjectsByState(state, companyId); }
    public ArrayList getProjectsByType(String type, int companyId) { return  facade.getProjectsByType(type, companyId); }
    public ArrayList getProjectsByCompanyName(String companyName, int companyId) { return  facade.getProjectsByCompanyName(companyName, companyId); }

    //Search
    public ArrayList search(String q, int companyId) {
        ArrayList<Result> res = facade.search(q, companyId);
        ArrayList<ResultsContainer> container = new ArrayList<>();
        String t;
        boolean skip = false;
        for (Result r : res) {
            skip = false;
            t = r.getType();
            for (int i = 0; i < container.size(); i++) {
                if(container.get(i).getType().equals(t)) {
                    container.get(i).getContainer().add(r);
                    skip = true;
                    break;
                }
            }
            if(skip)
                continue;
            container.add(new ResultsContainer(t));
            container.get(container.size() - 1).getContainer().add(r);
        }

        return container;
    }

    //public boolean changeProjectStatus(String project_id, String new_status, String usertype) { return facade.verifyProjectRequest(project_id, new_status, usertype); }
    public int[] getStatusCounts(int companyId) { return facade.getStatusCounts(companyId); }


    public ArrayList<Project> getProjectsByCompanyId(int company_id) {
        return facade.getProjectsByCompanyId(company_id);
    }

    public Company getCompanyById(int id) { return facade.getCompanyById(id); }

    //User Login / Registration
    public User login(String email, String password) {
        User user = facade.getUserByEmail(email);
        if(user != null) {
            if (Login.testPassword(password, user.password)) {
                user.company = facade.getCompanyById(user.getCompany_id()); // Assign company to user
                return user;
            }
            else
                return null;
        }
        return null;
    }
    // Companies
    public ArrayList<Company> getCompanies() {
        return facade.getCompanies();
    }
    public String getCompanyNames(String query, int companyId) {
        return JSONTranslator.stringArrayList(facade.getCompanyNames(query, companyId));
    }

    public ArrayList proccessStages(ArrayList stages) {
        HashMap userMap = new HashMap();
        HashMap companyMap = new HashMap();
        User user = null;
        for (Stage s : (ArrayList<Stage>) stages) {

            if(userMap.containsKey(s.user_id))
                s.user = (User) userMap.get(s.user_id);
            else {
                user = facade.getUserById(s.user_id);
                s.user = user;
                userMap.put(s.user_id, user);
            }
            if(companyMap.containsKey(s.user.getCompany_id()))
                s.user.company = (Company) companyMap.get(s.user.getCompany_id());
            else {
                s.user.company = facade.getCompanyById(s.user.getCompany_id());
                companyMap.put(s.user.getCompany_id(), s.user.company);
            }
        }

        Collections.sort(stages, Stage.TIME);

        return stages;

    }



    public ArrayList processMessages(ArrayList messages) {
        HashMap companyMap = new HashMap();
        HashMap userMap = new HashMap();
        User user = null;
        Company company = null;
        for (Message m : (ArrayList<Message>) messages) {
            if(userMap.containsKey(m.author_id))
                m.user = (User) userMap.get(m.author_id);
            else {
                user = facade.getUserById(m.author_id);
                m.user = user;
                userMap.put(m.author_id, user);
                company = facade.getCompanyById(user.getCompany_id());
                companyMap.put(user.getCompany_id(), company);
            }
            if(m.company == null) {
                if(companyMap.containsKey(m.user.getCompany_id()))
                    m.company = (Company) companyMap.get(m.user.getCompany_id());
                else {
                    company = facade.getCompanyById(m.user.getCompany_id());
                    m.company = company;
                    companyMap.put(company.id, company);
                }

            }
        }

        Collections.sort(messages, Message.TIME);

        return messages;

    }

    public Message processMessage(Message m) {
        m.user = facade.getUserById(m.getAuthor_id());
        m.company = facade.getCompanyById(m.user.getCompany_id());
        return m;
    }
    // POE
    public boolean addPoeFile(int project_id, Part file, int user_id, int stage) throws IOException {
        FileHandling handler = new FileHandling();

        handler.putFile(file, project_id);
        String filename = handler.getFileName();
        String filetype = handler.getFileType();

        System.out.println(project_id);
        System.out.println(filename);
        System.out.println(filetype);
        System.out.println(user_id);

        return facade.addPoeFile(project_id, filename, user_id, filetype, stage);

    }

    public ArrayList<Poe> getPoe(int project_id) {

        return facade.getPoe(project_id);
    }

    public boolean deleteFile(String filename, int project_id, int fileId, boolean deleteFile) throws IOException {
        FileHandling handler = new FileHandling();
        if(deleteFile) {
            if (!handler.deleteFile(filename, project_id))
                return false;
            return facade.deletePoe(fileId);
        } else {
            return facade.markDeletePoe(fileId);
        }
    }

    public void sendEmail(String recipient, String subject, String body) {
        new Notifications().sendEmail(recipient, subject, body);
    }

    public String getDistinctStatuses(String query) {
        return JSONTranslator.stringArrayList(facade.getDistinctStatuses(query));
    }
    public String getDistinctTypes(String query, int companyId) {
        return JSONTranslator.stringArrayList(facade.getDistinctTypes(query, companyId));
    }

    // BUDGETSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
    public boolean addBudget(int year, int quarter, int budget) {
        return facade.addBudget(year, quarter, budget);
    }

    public boolean modifyBudget(int new_budget, int year, int quarter) {
        return facade.modifyBudget(new_budget, year, quarter);
    }

    public ArrayList<Budget> getActiveBudget(int year, int quarter) {
        return facade.getActiveBudget(year, quarter);
    }

    public ArrayList<Budget> getAllBudgets() {
        return facade.getAllBudgets();
    }

    public int getAvailableFunds(int year, int quarter) {
        return facade.getAvailableFunds(year, quarter);
    }


    //Nonce / Email
    public void createPasswordResetNonce(int id, String email){
        Nonce nonce = new Nonce(-1, gen.nextNonce(), id, null, "PasswordReset");
        int nonceId = facade.addNonce(nonce);
        if(email != null && nonceId != -1)
            sendEmail(email, "Reset Password", "http://localhost:8080/reset-password?n=" + nonce.getNonce());
    }

    public int getUserIdByNonce(String nonce) {
        return facade.getUserIdByNonce(nonce);
    }

    public boolean createPassword(int id, String password, String nonce) {
        if(facade.createPassword(id, Login.createPassword(password))) {
            facade.deleteNonce(nonce);
            return true;
        } else {
            return false;
        }
    }


}


