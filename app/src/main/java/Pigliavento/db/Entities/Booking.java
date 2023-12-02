package Pigliavento.db.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Booking {
    public static final String TABLE_NAME = "Prenotazioni";

    private final int ID;
    private final float account;
    private final boolean isAccountSettled;
    private final float discount;
    private final String discountDescription;
    private final Date date;
    private final String bathhouse;
    private final String address;
    private final int nUmbrella;
    private final String cfCustomer;

    public Booking (final int ID, final float account, final boolean isAccountSettled, final float discount,
                    final String discountDescription, final Date date, final String bathhouse,
                    final String address, final int nUmbrella, final String cfCustomer) {
        this.ID = ID;
        this.account = account;
        this.isAccountSettled = isAccountSettled;
        this.discount = discount;
        this.discountDescription = discountDescription;
        this.date = date;
        this.bathhouse = bathhouse;
        this.address = address;
        this.nUmbrella = nUmbrella;
        this.cfCustomer = cfCustomer;
    }

    public int getID() {
        return this.ID;
    }

    public float getAccount() {
        return this.account;
    }

    public boolean getIsAccountSettled() {
        return this.isAccountSettled;
    }

    public float getDiscount() {
        return this.discount;
    }

    public String getDiscountDescription() {
        return this.discountDescription;
    }

    public Date getDate() {
        return this.date;
    }

    public String getBathhouse() {
        return this.bathhouse;
    }

    public String getAddress() {
        return address;
    }

    public int getnUmbrella() {
        return this.nUmbrella;
    }

    public String getCfCustomer() {
        return this.cfCustomer;
    }

    public static List<Booking> readBookingFromResultSet(final ResultSet resultSet) {
        final List<Booking> bookings = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int ID = resultSet.getInt("CodicePrenotazione");
                final float account = resultSet.getFloat("Conto");
                final boolean isAccountSettled = resultSet.getBoolean("ContoSaldato");
                final float discount = resultSet.getFloat("ScontoApplicato");
                final String discountDescription = resultSet.getString("DescrizioneSconto");
                final Date date = resultSet.getDate("Data");
                final String bathhouse = resultSet.getString("NomeStabilimento");
                final String address = resultSet.getString("IndirizzoStabilimento");
                final int nUmbrella = resultSet.getInt("NumeroOmbrellone");
                final String cfCustomer = resultSet.getString("CF_Titolare");
                final Booking booking = new Booking(ID, account, isAccountSettled, discount, discountDescription, date, bathhouse, address, nUmbrella, cfCustomer);
                bookings.add(booking);
            }
        } catch (final SQLException e) {}
        return bookings;
    }
}
