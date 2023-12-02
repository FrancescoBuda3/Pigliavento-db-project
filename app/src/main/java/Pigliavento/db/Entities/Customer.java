package Pigliavento.db.Entities;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Customer {
    public static final String TABLE_NAME = "Clienti";

    private final String cf;
    private final String name;
    private final String surname;
    private final Date birthDate;
    private final Optional<String> phone;
    private final Optional<String> email;

    public Customer(final String cf, final String name, final String surname, final Date birthDate, final Optional<String> phone, final Optional<String> email) {
        this.cf = cf;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
    }

    public String getCf() {
        return this.cf;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public Optional<String> getPhone() {
        return this.phone;
    }

    public Optional<String> getEmail() {
        return this.email;
    }

    public static List<Customer> readCustomerFromResultSet(ResultSet resultSet) {
        final List<Customer> customers = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String cf = resultSet.getString("CodiceFiscale");
                final String name = resultSet.getString("Nome");
                final String surname = resultSet.getString("Cognome");
                final Date birthDate = resultSet.getDate("DataNascita");
                final Optional<String> phone = Optional.ofNullable(resultSet.getString("Telefono"));
                final Optional<String> email = Optional.ofNullable(resultSet.getString("Email"));
                final Customer customer = new Customer(cf, name, surname, birthDate, phone, email);
                customers.add(customer);
            }
        } catch (final Exception e) {}
        return customers;
    }
}
