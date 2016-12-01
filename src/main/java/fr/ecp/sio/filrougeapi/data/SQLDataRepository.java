package fr.ecp.sio.filrougeapi.data;

import fr.ecp.sio.filrougeapi.model.Location;
import fr.ecp.sio.filrougeapi.model.Station;
import fr.ecp.sio.filrougeapi.model.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Driver;

/**
 * A DataRepository backed by an SQL database.
 * This repository depends on the MySQL Connector dependency to connect to the SQL server.
 */
public class SQLDataRepository implements DataRepository {

    // The URL of the SQL server to connect to, including the host (localhost), the port, the database name (fil_rouge) and some options.
//    private static final String DB_URL = "jdbc:google:mysql://localhost:3306/velib?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/fil_rouge";

    // Credentials to connect to the SQL server. It is a good practice to have credentials dedicated to the API for access.
    //TODO: We should not use root!
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection openConnection() throws SQLException {
        try {
            // This weird call is apparently useless but it ensures the SQL driver is registered.
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // This exception should never occur, wrap it as a SQLException an rethrow to avoid dealing with ClassNotFoundException.
            throw new SQLException(e);
        }
    }

    // A private method to create a station object from the current row of a ResultSet.
    private Station createStation(ResultSet rs) throws SQLException {
        Station s = new Station();
        s.setId(rs.getLong("id"));
        Location l = new Location();
        l.setLatitude(rs.getFloat("latitude"));
        l.setLongitude(rs.getFloat("longitude"));
        l.setAltitude(rs.getFloat("altitude"));

        s.setLocation(l);
        s.setName(rs.getString("name"));
        s.setAvailableBikes(rs.getInt("availableBikes"));
        s.setAvailableBikeStands(rs.getInt("availableBikeStands"));
        return s;
    }

    // A private method to create a user object from the current row of a ResultSet.
    private User createUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setLogin(rs.getString("login"));
        u.setPassWord(rs.getString("password"));
        u.setKey(rs.getString("key"));
        return u;
    }

    @Override
    public Station getStation(long id) throws IOException {
        // This syntax is a "try-with-resources".
        // We can instantiate in the brackets classes that implements AutoClosable (= have a close() method).
        // These 'resources' will have their close() method called at the end of the block, even in case of error.
        try (
            // Open a connection to the database.
            Connection c = openConnection();
            // Prepare a statement, with '?' placeholders.
            // Prepared statements are efficiently cached and allow a safe handling of placeholders.
            PreparedStatement stmt = c.prepareStatement("SELECT *, location.latitude as latitude , location.longitude as longitude, location.altitude as altitude  FROM station INNER JOIN location ON station.location = location.id WHERE  station.id = ? ");
        ) {
            // Provide values for placeholders (indexes start at 1 is SQL!)
            stmt.setLong(1, id);
            // The statement is ready, we can execute it and get the result.
            try (ResultSet rs = stmt.executeQuery()) {
                // ResultSet works like an iterator: we call next() to position to the next line, then read with other methods.
                if (!rs.next()) return null;
                return createStation(rs);
            }
        } catch (SQLException e) {
            // The DataRepository interface only allows exceptions of type IOException to be thrown, so we encapsulate the SQLException.
            throw new IOException("Database error", e);
        }
    }

    @Override
    public List<Station> getStations(String search, int limit, int offset) throws IOException {

            String sqlReq="SELECT *, location.latitude as latitude , location.longitude as longitude, location.altitude as altitude  FROM station INNER JOIN location ON station.location = location.id ";
        try {
                if(search != null && search != "" && limit == -1)
                {
                      sqlReq += " WHERE UPPER(name) LIKE ? LIMIT ?,18446744073709551610 ";
                }else if(search != null && search != "" ){
                     sqlReq += " WHERE UPPER(name) LIKE ? LIMIT ?,?";
                }else if ( limit == -1){
                     sqlReq += " LIMIT ?,18446744073709551610 ";
                }else{
                     sqlReq += " LIMIT ?,?";
                }

                Connection c = openConnection();
                PreparedStatement stmt = c.prepareStatement(sqlReq);
                if( search != null && search != "" && limit == -1)
                {
                    stmt.setString(1, "%" + search.toUpperCase() + "%");
                    stmt.setInt(2, offset);
                }else if(search != null && search != "" ){
                    stmt.setString(1, "%" + search.toUpperCase() + "%");
                    stmt.setInt(2, offset);
                    stmt.setInt(3, limit);
                }else if ( limit == -1){
                    stmt.setInt(1, offset);
                }else{
                    stmt.setInt(1, offset);
                    stmt.setInt(2, limit);
                }
            ResultSet rs = stmt.executeQuery();
            List<Station> list = new ArrayList<>();
            // Iterate over all the lines of the result.
            while (rs.next()) {
                list.add(createStation(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new IOException("Database error", e);
        }
    }
    @Override
    public User getUser(long id) throws IOException {
        // This syntax is a "try-with-resources".
        // We can instantiate in the brackets classes that implements AutoClosable (= have a close() method).
        // These 'resources' will have their close() method called at the end of the block, even in case of error.
        try (
                // Open a connection to the database.
                Connection c = openConnection();
                // Prepare a statement, with '?' placeholders.
                // Prepared statements are efficiently cached and allow a safe handling of placeholders.
                PreparedStatement stmt = c.prepareStatement("SELECT * FROM user WHERE id = ?");
        ) {
            // Provide values for placeholders (indexes start at 1 is SQL!)
            stmt.setLong(1, id);
            // The statement is ready, we can execute it and get the result.
            try (ResultSet rs = stmt.executeQuery()) {
                // ResultSet works like an iterator: we call next() to position to the next line, then read with other methods.
                if (!rs.next()) return null;
                return createUser(rs);
            }
        } catch (SQLException e) {
            // The DataRepository interface only allows exceptions of type IOException to be thrown, so we encapsulate the SQLException.
            throw new IOException("Database error", e);
        }
    }

    @Override
    public List<User> getUsers() throws IOException {
        try (
                Connection c = openConnection();
                PreparedStatement stmt = c.prepareStatement("SELECT * FROM user");
                ResultSet rs = stmt.executeQuery();
        ) {
            List<User> list = new ArrayList<>();
            // Iterate over all the lines of the result.
            while (rs.next()) {
                list.add(createUser(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new IOException("Database error", e);
        }
    }
}
