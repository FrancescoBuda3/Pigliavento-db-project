package Pigliavento.db.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Umbrella {
    public static final String TABLE_NAME = "Ombrelloni";
    
    private final int number;
    private final float dailyPrice;
    private final String bathhouse;
    private final String address;

    public Umbrella(int number, float dailyPrice, String bathhouse, String address) {
        this.number = number;
        this.dailyPrice = dailyPrice;
        this.bathhouse = bathhouse;
        this.address = address;
    }

    public int getNumber() {
        return this.number;
    }

    public float getDailyPrice() {
        return this.dailyPrice;
    }

    public String getBathhouse() {
        return this.bathhouse;
    }

    public String getAddress() {
        return this.address;
    }

    public static List<Umbrella> readUmbrellaFromResultSet(final ResultSet resultSet) {
        final List<Umbrella> umbrellas = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int number = resultSet.getInt("Numero");
                final float dailyPrice = resultSet.getFloat("PrezzoGiornaliero");
                final String bathhouse = resultSet.getString("NomeStabilimento");
                final String address = resultSet.getString("IndirizzoStabilimento");
                final Umbrella umbrella = new Umbrella(number, dailyPrice, bathhouse, address);
                umbrellas.add(umbrella);
            }
        } catch (final SQLException e) {}
        return umbrellas;
    }
}
