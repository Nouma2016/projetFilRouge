package fr.ecp.sio.filrougeapi.endpoints;

import fr.ecp.sio.filrougeapi.data.DataRepository;
import fr.ecp.sio.filrougeapi.data.DataUtils;
import fr.ecp.sio.filrougeapi.data.DummyDataRepository;
import fr.ecp.sio.filrougeapi.data.SQLDataRepository;
import fr.ecp.sio.filrougeapi.model.Station;
import fr.ecp.sio.filrougeapi.model.User;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 * A servlet to handle requests for a single station.
 * This servlet receives requests to URLs /stations/{id}.
 */
public class AuthServlet extends ApiServlet {

    // HttpServlet has protected doXxx() methods that correspond to HTTP verbs (GET, POST...).
    // These methods receive objects to represent both the request (to inspect or read from) and the response (to write to).
    // Default implementation sends a 405 error, so we must override doGet() to support GET requests and NOT call super implementation.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login;
        String passWord;
        login=req.getParameter("login");
        passWord=req.getParameter("password");
        try {
                //Verify if login and password exist and not empty
                if (login == null || login.isEmpty() || passWord == null || passWord.isEmpty()){
                    resp.sendError(401, "Unauthorized");
                    return;
                }
                //DummyDataRepository is used to create users
                DataRepository repos = new DummyDataRepository();
                List<User> users = repos.getUsers();
                for(User user:users)
                {
                    //To check inserted login/password
                    if(user.getLogin().equals(login) && user.getPassWord().equals(passWord)){
                        resp.setContentType("application/json");
                        // In case of login/password matching, key is sent
                        sendResponse(user.getKey(), resp);
                        return;
                    }

                }
                //In case of login/password non-matching, error is sent
                resp.sendError(403, "User does not exist");
                return;

        } catch (NumberFormatException ex) {
            // An error occured. Please try again(500 code).
            resp.sendError(500, "An error occured. Please try again.");
            return;
        }

    }

}
