package Pigliavento.db.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Boat {
    public static final String TABLE_NAME = "Barche";

    private final String model;
    private final int number;
    private final int capacity;

    public Boat(String model, int number, int capacity) {
        this.model = model;
        this.number = number;
        this.capacity = capacity;
    }

    public String getModel() {
        return this.model;
    }

    public int getNumber() {
        return this.number;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public static List<Boat> readBoatFromResultSet(final ResultSet resultSet) {
        final List<Boat> boats = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String model = resultSet.getString("Modello");
                final int number = resultSet.getInt("Numero");
                final int capacity = resultSet.getInt("Capienza");
                final Boat boat = new Boat(model, number, capacity);
                boats.add(boat);
            }
        } catch (final SQLException e) {}
        return boats;
    }
}
