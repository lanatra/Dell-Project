import javax.servlet.RequestDispatcher;
import java.io.IOException;

/**
 * Created by Andreas Poulsen on 08-Apr-15.
 */
public class TheServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        request.setAttribute("name", request.getParameter("name"));
        dispatcher.forward(request, response);
    }
}
