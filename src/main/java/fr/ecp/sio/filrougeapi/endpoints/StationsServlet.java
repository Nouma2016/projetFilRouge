package fr.ecp.sio.filrougeapi.endpoints;

import fr.ecp.sio.filrougeapi.data.DataRepository;
import fr.ecp.sio.filrougeapi.data.DataUtils;
import fr.ecp.sio.filrougeapi.model.Station;
import fr.ecp.sio.filrougeapi.model.listStations;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * A servlet to handle requests for a list of stations.
 * This servlet receives requests to URLs /stations/{search}/{limit}/{offset}.
 */
public class StationsServlet extends ApiServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search;
        int limit;
        int offset;
        try {
            boolean emptyLimit=false;
            try {
                limit = Integer.parseInt(req.getParameter("limit"));
            } catch (NumberFormatException e) {
                emptyLimit=true;
                limit=-1;
            }
            try {
                offset = Integer.parseInt(req.getParameter("offset"));
            } catch (NumberFormatException e) {
                offset=0;
            }
            if (offset<0)
            {
                resp.sendError(400, "offset doit etre superieur ou egal à 0");
                return;
            }
                if(limit <= -1 && !emptyLimit)
            {
                resp.sendError(400, "la limite doit etre superier  ou egal à 1  ");
                return;

            }

            search=req.getParameter("search");
            DataRepository repos = DataUtils.getRepository();
            List<Station> stations = repos.getStations(search,limit,offset);
            if (stations.size() == 0) {
                // A stations was not found , send a 404 error.
                resp.sendError(404, "No station was found");
                return;
            }
            listStations list=new listStations(stations);

            resp.setContentType("application/json");
            sendResponse(list, resp);
            return;
        } catch (NumberFormatException ex) {
            // An error occured. Please try again(500 code).
            resp.sendError(500, "An error occured. Please try again.");
            return;
        }


    }

}
