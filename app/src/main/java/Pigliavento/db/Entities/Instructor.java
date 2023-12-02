package Pigliavento.db.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Instructor {
    public static final String TABLE_NAME = "Istruttori";   

    String cf;
    String name;
    String surname;
    Optional<String> phone;
    Optional<String> email;


    public Instructor(String cf, String name, String surname, Optional<String> phone, Optional<String> email) {
        this.cf = cf;
        this.name = name;
        this.surname = surname;
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

    public Optional<String> getPhone() {
        return this.phone;
    }

    public Optional<String> getEmail() {
        return this.email;
    }

    public static List<Instructor> readInstructorFromResultSet(ResultSet resultSet) {
        final List<Instructor> instructors = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final String cf = resultSet.getString("CodiceFiscale");
                final String name = resultSet.getString("Nome");
                final String surname = resultSet.getString("Cognome");
                final Optional<String> phone = Optional.ofNullable(resultSet.getString("Telefono"));
                final Optional<String> email = Optional.ofNullable(resultSet.getString("Email"));
                final Instructor instructor = new Instructor(cf, name, surname, phone, email);
                instructors.add(instructor);
            }
        } catch (final SQLException e) {}
        return instructors;
    }


}
