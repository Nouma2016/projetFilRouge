package fr.ecp.sio.filrougeapi.data;

import fr.ecp.sio.filrougeapi.model.Station;
import fr.ecp.sio.filrougeapi.model.User;

import java.io.IOException;
import java.util.List;

/**
 * An interface for a "Repository" component that will expose methods to interact with the data layer of our project.
 * All servlets will get an instance of the Repository (with DataUtils).
 */
public interface DataRepository {

    /*
        Get a station by id.
        Returns null if the station is not found.
     */
    Station getStation(long id) throws IOException;

    /*
        Get a list of all stations.
        Added parameters: search, limit, offset
     */
    List<Station> getStations(String search,int limit,int offset) throws IOException;

    /*
            Get a user by id.
            Returns null if the user is not found.
         */
    User getUser(long id) throws IOException;
    /*
            Get a list of all users.
         */
    List<User> getUsers() throws IOException;

}
