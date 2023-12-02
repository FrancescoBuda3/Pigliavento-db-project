package Pigliavento.db.Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Pigliavento.utils.Utils;

public class Trip {
    public static final String TABLE_NAME = "Escursioni";
    
    private final int maxPartecipants;
    private final String name;
    private final int number;
    private final Date date;
    private final Time startTime;
    private final Time endTime;
    private final float price;
    private final String boatModel;
    private final int boatNumber;
    private final String cfInstructor;
    private int nPartecipants;
    
    public Trip(int maxPartecipants, String name, int number, Date date, Time startTime, Time endTime,
                float price, String boatModel, int boatNumber, String cfInstructor, int nPartecipants) {
        this.maxPartecipants = maxPartecipants;
        this.name = name;
        this.number = number;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.boatModel = boatModel;
        this.boatNumber = boatNumber;
        this.cfInstructor = cfInstructor;
        this.nPartecipants = nPartecipants;
    }

    public int getMaxPartecipants() {
        return this.maxPartecipants;
    }

    public String getName() {
        return this.name;
    }

    public int getNumber() {
        return this.number;
    }

    public Date getDate() {
        return this.date;
    }

    public Time getStartTime() {
        return this.startTime;
    }

    public Time getEndTime() {
        return this.endTime;
    }

    public float getPrice() {
        return this.price;
    }

    public String getBoatModel() {
        return this.boatModel;
    }

    public int getBoatNumber() {
        return this.boatNumber;
    }

    public String getCfInstructor() {
        return this.cfInstructor;
    }

    public int getnPartecipants() {
        return this.nPartecipants;
    }

    public void setnPartecipants(int nPartecipants) {
        this.nPartecipants = nPartecipants;
    }

    public boolean update(Connection connection) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "maxPartecipanti = ?," + 
                "Nome = ?," +
                "Numero = ?, " + 
                "Data = ?, " +
                "OraInizio = ?, " +
                "OraFine = ?, " +
                "Prezzo = ?, " +
                "ModelloBarca = ?, " +
                "NumeroBarca = ?, " +
                "CF_Istruttore = ?, " +
                "NumPartecipanti = ? " +
            "WHERE Nome = ? AND Numero = ?";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, maxPartecipants);
            statement.setString(2, name);
            statement.setInt(3, number);
            statement.setDate(4, Utils.dateToSqlDate(date));
            statement.setTime(5, startTime);
            statement.setTime(6, endTime);
            statement.setFloat(7, price);
            statement.setString(8, boatModel);
            statement.setInt(9, boatNumber);
            statement.setString(10, cfInstructor);
            statement.setInt(11, nPartecipants);
            statement.setString(12, name);
            statement.setInt(13, number);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<Trip> readTripFromResultSet(ResultSet resultSet) {
        final List<Trip> trips = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int maxPartecipants = resultSet.getInt("MaxPartecipanti");
                final String name = resultSet.getString("Nome");
                final int number = resultSet.getInt("Numero");
                final Date date = resultSet.getDate("Data");
                final Time startTime = resultSet.getTime("OraInizio");
                final Time endTime = resultSet.getTime("OraFine");
                final float price = resultSet.getFloat("Prezzo");
                final String boatModel = resultSet.getString("ModelloBarca");
                final int boatNumber = resultSet.getInt("NumeroBarca");
                final String cfInstructor = resultSet.getString("CF_Istruttore");
                final int nPartecipants = resultSet.getInt("NumPartecipanti");
                final Trip trip = new Trip(maxPartecipants, name, number, date, startTime, endTime, price, boatModel, boatNumber, cfInstructor, nPartecipants);
                trips.add(trip);
            }
        } catch (final SQLException e) {}
        return trips;
    }
}
