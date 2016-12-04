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
                // We get the limit value from the path of the URL of the request. If limit is not indicated in the URL, it takes the "-1" value
                limit = Integer.parseInt(req.getParameter("limit"));
            } catch (NumberFormatException e) {
                emptyLimit=true;
                limit=-1;
            }
            try {
                // We get the offset value from the path of the URL of the request. If offset is not indicated in the URL, it takes the "0" value
                offset = Integer.parseInt(req.getParameter("offset"));
            } catch (NumberFormatException e) {
                offset=0;
            }
            if (offset<0)
            {
//              // In case of offset value < 0, send error message
                resp.sendError(400, "offset must be superior or equal to 0");
                return;
            }
            if(limit < 0 && !emptyLimit)
            {
                // In case of limit exists and not empty,  and limit<0, send error message
                resp.sendError(400, "limit must be superior or equal to 0 ");
                return;

            }

            search=req.getParameter("search");
            DataRepository repos = DataUtils.getRepository();
           /*Get list of stations which match with the research criteria:
            search=name of the station(full or part of name)
            limit=number of stations to be visualized
            offset=Index of the first element of the stations list to be visualized*/
            List<Station> stations = repos.getStations(search,limit,offset);
            //In case of stations list is empty
            if (stations.size() == 0) {
                // No station was found , send a 404 error.
                resp.sendError(404, "No station was found");
                return;
            }
            //Create a new object listStations which returns, in addition to the list of stations,
            // the accumulated statistics of number of bikes and available bike'stands
            listStations list=new listStations(stations);
            //Define response format=JSON
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
