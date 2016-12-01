package fr.ecp.sio.filrougeapi.data;

import fr.ecp.sio.filrougeapi.model.Location;
import fr.ecp.sio.filrougeapi.model.Station;
import fr.ecp.sio.filrougeapi.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A DataRepository that returns fake data.
 */
public class DummyDataRepository implements DataRepository {
    @Override
    public Station getStation(long id) {
        if (id == 25) {
            Station station = new Station();
            station.setId(25);
            station.setName("Station de Nation");
            Location loc = new Location();
            loc.setLatitude(46.0f);
            loc.setLongitude(1.0f);
            station.setLocation(loc);
            station.setAvailableBikes(10);
            station.setAvailableBikeStands(20);
            return station;
        } else if (id == 26) {
            Station station = new Station();
            station.setId(26);
            station.setName("Station de Bercy");
            Location loc = new Location();
            loc.setLatitude(47.0f);
            loc.setLongitude(1.2f);
            station.setLocation(loc);
            station.setAvailableBikes(10);
            station.setAvailableBikeStands(20);
            return station;
        } else if (id == 27) {
            Station station = new Station();
            station.setId(27);
            station.setName("Station de Daumesnil");
            Location loc = new Location();
            loc.setLatitude(48.0f);
            loc.setLongitude(2.0f);
            station.setLocation(loc);
            station.setAvailableBikes(10);
            station.setAvailableBikeStands(20);
            return station;
        } else if (id == 28) {
            Station station = new Station();
            station.setId(28);
            station.setName("Station de Voltaire");
            Location loc = new Location();
            loc.setLatitude(49.0f);
            loc.setLongitude(2.2f);
            station.setLocation(loc);
            station.setAvailableBikes(10);
            station.setAvailableBikeStands(20);
            return station;
        } else {
            return null;
        }
    }

    @Override
    public List<Station> getStations(String search, int limit, int offset) {
        List<Station>  stations = Arrays.asList(getStation(25),getStation(26),getStation(27),getStation(28));

        if (offset >= stations.size()) {
            stations= stations.subList(0, 0); //return empty.
        }
        else if (offset>0) {
            if (limit >-1 ) {
                //apply offset and limit
                stations= stations.subList(offset, Math.min(offset+limit, stations.size()));
            } else  {
                //apply just offset
                stations= stations.subList(offset, stations.size());
            }
        } else if (limit >-1) {
            //apply just limit
            stations= stations.subList(0, Math.min(limit, stations.size()));
        }  else {
            stations= stations.subList(0, stations.size());
        }
        List<Station> resultsList = new ArrayList<> ();
        if(search != null && !search.equals(""))
        {
            for (Station station : stations) {
                if (station.getName().toLowerCase().contains(search.toLowerCase())){
                    resultsList.add(station);
                }
            }
            stations=resultsList;
        }
        return stations;

    }

    @Override
    public User getUser(long id) {
        if (id == 1) {
            User user = new User();
            user.setId(1);
            user.setLogin("abdel");
            user.setPassWord("nouma");
            user.setKey("1234");
            return user;
        } else if (id == 2) {
            User user = new User();
            user.setId(2);
            user.setLogin("abderrahman");
            user.setPassWord("klach");
            user.setKey("12345");
            return user;
        } else if (id == 3) {
            User user = new User();
            user.setId(3);
            user.setLogin("khaoula");
            user.setPassWord("klach");
            user.setKey("1234");
            return user;
        }  else {
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return  Arrays.asList(getUser(1), getUser(2), getUser(3));
    }
}
