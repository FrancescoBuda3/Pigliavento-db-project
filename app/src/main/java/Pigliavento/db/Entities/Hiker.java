package Pigliavento.db.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Hiker extends Customer{
    public static final String TABLE_NAME = "Escursionisti";

    private final float account;

    public Hiker(final String cf, final String name, final String surname, final Date birthDate, final Optional<String> phone, final Optional<String> email, final float account) {
        super(cf, name, surname, birthDate, phone, email);
        this.account = account;
    }

    public float getAccount() {
        return this.account;
    }

    public static List<Hiker> readHikerFromResultSet(ResultSet resultSet) {
        final List<Hiker> hikers = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String cf = resultSet.getString("CodiceFiscale");
                final String name = resultSet.getString("Nome");
                final String surname = resultSet.getString("Cognome");
                final Date birthDate = resultSet.getDate("DataNascita");
                final Optional<String> phone = Optional.ofNullable(resultSet.getString("Telefono"));
                final Optional<String> email = Optional.ofNullable(resultSet.getString("Email"));
                final float account = resultSet.getFloat("Conto");
                final Hiker hiker = new Hiker(cf, name, surname, birthDate, phone, email, account);
                hikers.add(hiker);
            }
        } catch (final SQLException e) {}
        return hikers;
    }
}
