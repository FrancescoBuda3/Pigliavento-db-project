package Pigliavento.db.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bathhouse {
    public static final String TABLE_NAME = "Stabilimenti";

    private final String phone;
    private final String name;
    private final String address;
    private final int nBookings;

    public Bathhouse(final String phone, final String name, final String address, final int nBookings) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.nBookings = nBookings;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public int getnBookings() {
        return this.nBookings;
    }

    public static List<Bathhouse> readBathhouseFromResultSet(final ResultSet resultSet) {
        final List<Bathhouse> bathhouses = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String phone = resultSet.getString("Telefono");
                final String name = resultSet.getString("Nome");
                final String address = resultSet.getString("Indirizzo");
                final int nBookings = resultSet.getInt("nPrenotazioni");
                final Bathhouse bathhouse = new Bathhouse(phone, name, address, nBookings);
                bathhouses.add(bathhouse);
            }
        } catch (final SQLException e) {}
        return bathhouses;
    }
}
