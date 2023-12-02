package Pigliavento.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import Pigliavento.db.Entities.Customer;
import Pigliavento.db.Entities.Hiker;
import Pigliavento.db.Entities.Instructor;
import Pigliavento.db.Entities.Trip;
import Pigliavento.db.Entities.Umbrella;
import Pigliavento.db.Entities.AnnualCollection;
import Pigliavento.db.Entities.Bathhouse;
import Pigliavento.db.Entities.Boat;
import Pigliavento.db.Entities.Booking;
import Pigliavento.db.Entities.BookingCount;
import Pigliavento.utils.Pair;
import Pigliavento.utils.Utils;

public class ActionsImpl implements Actions{
    private static final String HIKER_REGISTRATION_TABLE_NAME = "iscrizioni_escursionisti";
    private static final String CUSTOMER_REGISTRATION_TABLE_NAME = "iscrizioni_clienti";
    private static final String PERIOD_TABLE_NAME = "Periodi";
    private static final String VALIDITY_TABLE_NAME = "Validit\u00E0";
    private static final String GUEST_TABLE_NAME = "Ospiti";

    private final Connection connection; 
    
    public ActionsImpl(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public boolean addHiker(Hiker hiker) {
        final String query = "INSERT INTO " + Hiker.TABLE_NAME + " (DataNascita, Conto, CodiceFiscale, Nome, Cognome, Telefono, Email) VALUES (?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(hiker.getBirthDate()));
            statement.setFloat(2, hiker.getAccount());
            statement.setString(3, hiker.getCf());
            statement.setString(4, hiker.getName());
            statement.setString(5, hiker.getSurname());
            statement.setString(6, hiker.getPhone().orElse(null));
            statement.setString(7, hiker.getEmail().orElse(null));
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean addCustomer(Customer customer) {
        final String query = "INSERT INTO " + Customer.TABLE_NAME + " (DataNascita, CodiceFiscale, Nome, Cognome, Telefono, Email) VALUES (?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(customer.getBirthDate()));
            statement.setString(2, customer.getCf());
            statement.setString(3, customer.getName());
            statement.setString(4, customer.getSurname());
            statement.setString(5, customer.getPhone().orElse(null));
            statement.setString(6, customer.getEmail().orElse(null));
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean RegisterHiker(String cf, String tripName, int tripNumber) {
        final Optional<Trip> trip = checkTrip(tripName, tripNumber);
        if (checkTrip(tripName, tripNumber).isEmpty()) {
            System.out.println("escursione non esistente");
            return false;
        }

        if (trip.get().getMaxPartecipants() <= trip.get().getnPartecipants()) {
            System.out.println("escursione piena");
            return false;
        }

        final String query = "INSERT INTO " + HIKER_REGISTRATION_TABLE_NAME + " (NomeEscursione, NumeroEscursione, CF_Escursionista) VALUES (?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, tripName);
            statement.setInt(2, tripNumber);
            statement.setString(3, cf);
            statement.executeUpdate();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
        trip.get().setnPartecipants(trip.get().getnPartecipants() + 1);
        trip.get().update(connection);

        final String query2 = "UPDATE " + Hiker.TABLE_NAME + " SET Conto = Conto + ? WHERE CodiceFiscale = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query2)) {
            statement.setFloat(1, trip.get().getPrice());
            statement.setString(2, cf);
            statement.executeUpdate();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
        return true;
    }

    @Override
    public boolean RegisterCustomer(String cf, String tripName, int tripNumber) {

        final Optional<Trip> trip = checkTrip(tripName, tripNumber);
        if (checkTrip(tripName, tripNumber).isEmpty()) {
            return false;
        }

        if (trip.get().getMaxPartecipants() <= trip.get().getnPartecipants()) {
            return false;
        }

        String query = "INSERT INTO " + CUSTOMER_REGISTRATION_TABLE_NAME + " (NomeEscursione, NumeroEscursione, CF_cliente) VALUES (?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, tripName);
            statement.setInt(2, tripNumber);
            statement.setString(3, cf);
            statement.executeUpdate();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
        trip.get().setnPartecipants(trip.get().getnPartecipants() + 1);
        trip.get().update(connection);

        query = "UPDATE " + Booking.TABLE_NAME + " SET Conto = Conto + ? WHERE CodicePrenotazione = (SELECT CodicePrenotazione FROM " + GUEST_TABLE_NAME + " NATURAL JOIN " +VALIDITY_TABLE_NAME+ " WHERE CF_Ospite = ? AND DataInizio <= ? AND DataFine >= ?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setFloat(1, trip.get().getPrice());
            statement.setString(2, cf);
            statement.setDate(3, Utils.dateToSqlDate(trip.get().getDate()));
            statement.setDate(4, Utils.dateToSqlDate(trip.get().getDate()));
            statement.executeUpdate();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
        return true;
        
    }

    @Override
    public List<Customer> getCustomers() {
        final String query = "SELECT * FROM " + Customer.TABLE_NAME;

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            return Customer.readCustomerFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Hiker> checkHiker(String cf) {
        final String query = "SELECT * FROM " + Hiker.TABLE_NAME + " WHERE CodiceFiscale = ?";

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, cf);
            final ResultSet resultSet = statement.executeQuery();
            return Hiker.readHikerFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean addTrip(Trip trip) {

        final String query = "INSERT INTO " + Trip.TABLE_NAME + " (ModelloBarca, NumeroBarca, MaxPartecipanti, NumPartecipanti, Data, OraInizio, OraFine, Prezzo, Nome, Numero, CF_istruttore) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, trip.getBoatModel());
            statement.setInt(2, trip.getBoatNumber());
            statement.setInt(3, trip.getMaxPartecipants());
            statement.setInt(4, trip.getnPartecipants());
            statement.setDate(5, Utils.dateToSqlDate(trip.getDate()));
            statement.setTime(6, trip.getStartTime());
            statement.setTime(7, trip.getEndTime());
            statement.setFloat(8, trip.getPrice());
            statement.setString(9, trip.getName());
            statement.setInt(10, trip.getNumber());
            statement.setString(11, trip.getCfInstructor());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Umbrella> getAvailableUmbrellasInBathhouse(String bathhouse, String address, Date startDate, Date endDate) {
        final String query = "SELECT * FROM " + Umbrella.TABLE_NAME + " WHERE NomeStabilimento = ? and IndirizzoStabilimento = ? " + 
        "AND Numero NOT IN ( SELECT o.Numero " +
                            "FROM " + Umbrella.TABLE_NAME + " o NATURAL JOIN " + Booking.TABLE_NAME + " p NATURAL JOIN " + VALIDITY_TABLE_NAME + " v " +
                            "WHERE o.NomeStabilimento = ? AND o.IndirizzoStabilimento = ? AND o.Numero = p.NumeroOmbrellone " +
                            "AND ((v.DataInizio >= ? AND v.DataInizio <= ?) OR (v.DataFine >= ? AND  v.DataFine <= ?)))";
        
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, bathhouse);
            statement.setString(2, address);
            statement.setString(3, bathhouse);
            statement.setString(4, address);
            statement.setDate(5, Utils.dateToSqlDate(startDate));
            statement.setDate(6, Utils.dateToSqlDate(endDate));
            statement.setDate(7, Utils.dateToSqlDate(startDate));
            statement.setDate(8, Utils.dateToSqlDate(endDate));
            final ResultSet resultSet = statement.executeQuery();
            return Umbrella.readUmbrellaFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<BookingCount> getBookingCountPerBathhouse() {
        final String query = "SELECT Nome, Indirizzo, nPrenotazioni FROM " + Bathhouse.TABLE_NAME;
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            return BookingCount.readBookingCountFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<AnnualCollection> getCollectionPerYear() {
        final String query = "SELECT YEAR(Data) as Anno, SUM(Conto) - SUM(ScontoApplicato) as Incasso FROM " + Booking.TABLE_NAME + " WHERE contoSaldato = true" + 
        " GROUP BY Anno  HAVING Anno >= YEAR(CURDATE()) - 9";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            return AnnualCollection.readAnnualCollectionFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean addBooking(String cf, int nUmbrella, String bathhouse, String address, int code, List<Pair<Date, Date>> periods) {

        final Optional<Umbrella> umbrella = checkUmbrella(bathhouse, address, nUmbrella);
        if (umbrella.isEmpty()) {
            return false;
        }

        if (periods.isEmpty()) {
            return false;
        }

        periods.stream().forEach(period -> {
            addPeriodIfAbsent(period.getFirst(), period.getSecond());
        });

        String query = "INSERT INTO " + Booking.TABLE_NAME + " (Data, CodicePrenotazione, CF_Titolare, NomeStabilimento, IndirizzoStabilimento, NumeroOmbrellone, "
                + "Conto, ContoSaldato, ScontoApplicato, DescrizioneSconto) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(new Date()));
            statement.setInt(2, code);
            statement.setString(3, cf);
            statement.setString(4, bathhouse);
            statement.setString(5, address);
            statement.setInt(6, nUmbrella);
            statement.setFloat(7, periods.stream().map(period -> Utils.daysBetween(period.getFirst(), period.getSecond())).reduce((a,b) -> (a + b)).get() * umbrella.get().getDailyPrice());
            statement.setBoolean(8, true);
            statement.setFloat(9, 0);
            statement.setString(10, "nessuno sconto");
            statement.executeUpdate();
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }

        query = "UPDATE " + Bathhouse.TABLE_NAME + " SET nPrenotazioni = nPrenotazioni + 1 WHERE Nome = ? AND Indirizzo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, bathhouse);
            statement.setString(2, address);
            statement.executeUpdate();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }

        for (final Pair<Date, Date> period : periods) {
            query = "INSERT INTO " + VALIDITY_TABLE_NAME + " (DataInizio, DataFine, CodicePrenotazione) VALUES (?,?,?)";
            try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setDate(1, Utils.dateToSqlDate(period.getFirst()));
                statement.setDate(2, Utils.dateToSqlDate(period.getSecond()));
                statement.setInt(3, code);
                statement.executeUpdate();
            } catch (final SQLIntegrityConstraintViolationException e) {
                throw new IllegalStateException(e);
            } catch (final SQLException e) {
                throw new IllegalStateException(e);
            }
        }

        this.addGuest(code, cf);
        return true;
    }

    @Override
    public boolean addPeriodIfAbsent(Date startDate, Date endDate) {
        String query = "SELECT * FROM " + PERIOD_TABLE_NAME + " WHERE DataInizio = ? AND DataFine = ?";

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(startDate));
            statement.setDate(2, Utils.dateToSqlDate(endDate));
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                query = "INSERT INTO " + PERIOD_TABLE_NAME + " (DataInizio, DataFine) VALUES (?,?)";
                try (final PreparedStatement statement2 = this.connection.prepareStatement(query)) {
                    statement2.setDate(1, Utils.dateToSqlDate(startDate));
                    statement2.setDate(2, Utils.dateToSqlDate(endDate));
                    statement2.executeUpdate();
                } catch (final SQLIntegrityConstraintViolationException e) {
                    throw new IllegalStateException(e);
                } catch (final SQLException e) {
                    throw new IllegalStateException(e);
                }
            }
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
        return true;
    }

    @Override
    public boolean addDiscountToBooking(int bookingID, float amount, String description) {
        final String query = "UPDATE " + Booking.TABLE_NAME + " SET ScontoApplicato = ?, DescrizioneSconto = ? WHERE CodicePrenotazione = ?";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setFloat(1, amount);
            statement.setString(2, description);
            statement.setInt(3, bookingID);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean addGuest(int bookingID, String cf) {
        String query = "INSERT INTO " + GUEST_TABLE_NAME + " (CodicePrenotazione, CF_Ospite) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, bookingID);
            statement.setString(2, cf);
            statement.executeUpdate();
        } catch (final SQLIntegrityConstraintViolationException e) {
            throw new IllegalStateException(e);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
        return true;
    }

    @Override
    public Optional<Trip> checkTrip(String tripName, int tripNumber) {
        final String query = "SELECT * FROM " + Trip.TABLE_NAME + " WHERE Nome = ? AND Numero = ?";

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, tripName);
            statement.setInt(2, tripNumber);
            final ResultSet resultSet = statement.executeQuery();
            return Trip.readTripFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Boat> checkBoat(String boatModel, int boatNumber) {
        final String query = "SELECT * FROM " + Boat.TABLE_NAME + " WHERE Modello = ? AND Numero = ?";

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, boatModel);
            statement.setInt(2, boatNumber);
            final ResultSet resultSet = statement.executeQuery();
            return Boat.readBoatFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Umbrella> checkUmbrella(String bathhouse, String address, int nUmbrella) {
        final String query = "SELECT * FROM " + Umbrella.TABLE_NAME + " WHERE Numero = ? AND NomeStabilimento = ? AND IndirizzoStabilimento = ?";

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, nUmbrella);
            statement.setString(2, bathhouse);
            statement.setString(3, address);
            final ResultSet resultSet = statement.executeQuery();
            return Umbrella.readUmbrellaFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    
    @Override
    public List<Hiker> getHikers() {
        final String query = "SELECT * FROM " + Hiker.TABLE_NAME;

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            return Hiker.readHikerFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Trip> getTrips() {
        final String query = "SELECT * FROM " + Trip.TABLE_NAME;

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            return Trip.readTripFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Boat> getBoats() {
        final String query = "SELECT * FROM " + Boat.TABLE_NAME;

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            return Boat.readBoatFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Booking> getBookings() {
        final String query = "SELECT * FROM " + Booking.TABLE_NAME;

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            return Booking.readBookingFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Instructor> getInstructors() {
        final String query = "SELECT * FROM " + Instructor.TABLE_NAME;

        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            return Instructor.readInstructorFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
