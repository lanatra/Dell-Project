package Domain;

import DataLayer.DatabaseFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Controller {

    DatabaseFacade facade;

    public Controller() {

        this.facade = new DatabaseFacade();

    }
    // Writers
    public boolean createProjectRequest(String budget, String project_body) {
        return facade.createProjectRequest(budget, project_body);
    }


   //Readers
    //User related
    public User getUserById(int user_id) { return facade.getUserById(user_id); }


    //Project related
    public DisplayProject getProjectById(int id, int companyId) {
        DisplayProject dp = facade.getProjectById(id, companyId);
        if((companyId == 1 && dp.isUnread_admin()) || (companyId != 1 && dp.isUnread_partner()))
            facade.markRead(id, companyId);
        return  dp; }
    public ArrayList getMessagesByProjectId(int projId) { return processMessages(facade.getMessagesByProjectId(projId)); }
    public String postMessage(int userId, int projId, String body) { return processMessage(facade.postMessage(userId, projId, body)).toHTML();}
    public ArrayList getProjectsByState(String state, int companyId) { return  facade.getProjectsByState(state, companyId); }
    public boolean verifyProjectRequest(String project_id) { return facade.verifyProjectRequest(project_id); }
    public int[] getStatusCounts(int companyId) { return facade.getStatusCounts(companyId); }

    public Company getCompanyById(int id) { return facade.getCompanyById(id); }

    //User Login / Registration
    public User login(String email, String password) {
        User user = facade.getUserByEmail(email);
        if(user != null) {
            if (Login.testPassword(password, user.password))
                return user;
            else
                return null;
        }
        return null;
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

        Collections.sort(messages, Comparators.TIME);

        return messages;

    }

    public Message processMessage(Message m) {
        m.user = facade.getUserById(m.getAuthor_id());
        m.company = facade.getCompanyById(m.user.getCompany_id());
        return m;
    }

}
