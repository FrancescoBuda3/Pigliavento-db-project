package Pigliavento.db.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingCount {
    private final int count;
    private final String bathhouse;
    private final String address;

    public BookingCount(int count, String bathhouse, String address) {
        this.count = count;
        this.bathhouse = bathhouse;
        this.address = address;
    }

    public int getCount() {
        return this.count;
    }

    public String getBathhouse() {
        return this.bathhouse;
    }

    public String getAddress() {
        return this.address;
    }

    public static List<BookingCount> readBookingCountFromResultSet(final ResultSet resultSet) {
        final List<BookingCount> bookingCounts = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int count = resultSet.getInt("nPrenotazioni");
                final String bathhouse = resultSet.getString("Nome");
                final String address = resultSet.getString("Indirizzo");
                final BookingCount bookingCount = new BookingCount(count, bathhouse, address);
                bookingCounts.add(bookingCount);
            }
        } catch (final SQLException e) {}
        return bookingCounts;
    }
}
