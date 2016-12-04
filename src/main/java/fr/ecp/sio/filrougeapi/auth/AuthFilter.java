package fr.ecp.sio.filrougeapi.auth;

import fr.ecp.sio.filrougeapi.data.DataRepository;
import fr.ecp.sio.filrougeapi.data.DataUtils;
import fr.ecp.sio.filrougeapi.data.DummyDataRepository;
import fr.ecp.sio.filrougeapi.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A filter, declared in web.xml, that will intercept requests to our API and check for valid credentials before the request reaches the servlet. -->
 */
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to do when the filter is initialized (at server's startup or first request).
    }

    // All requests and responses are forwarded to the doFilter() method.
    // It is your responsibility to call filterChain.doFilter() somewhere in the implementation for the request to continue to the servlet.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        *********************
        // If the servlet is /auth/token, do not apply the filter
        String path = ((HttpServletRequest) servletRequest).getRequestURI();
        if (!path.startsWith("/auth/token")) {
            // Let's inspect HTTP headers of the request.
            String auth = ((HttpServletRequest) servletRequest).getParameter("token");

            boolean existkey=false;

            DataRepository rep = new DummyDataRepository();
            // To get all users
            List<User> users = rep.getUsers();
            //fetch the user who match to the given token
            for (User user : users) {
                if(user.getKey().equals(auth)){
                    existkey=true;
                }
            }
            //In case of user does not exist, send error message
            if (!existkey){
                ((HttpServletResponse) servletResponse).sendError(403, "Forbidden");
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() { }

}
