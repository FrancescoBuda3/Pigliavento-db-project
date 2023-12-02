package Pigliavento.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import Pigliavento.db.Entities.AnnualCollection;
import Pigliavento.db.Entities.Boat;
import Pigliavento.db.Entities.Booking;
import Pigliavento.db.Entities.BookingCount;
import Pigliavento.db.Entities.Customer;
import Pigliavento.db.Entities.Hiker;
import Pigliavento.db.Entities.Instructor;
import Pigliavento.db.Entities.Trip;
import Pigliavento.db.Entities.Umbrella;
import Pigliavento.utils.Pair;

public interface Actions {
    boolean addHiker(Hiker hiker);
    boolean addCustomer(Customer customer);
    boolean RegisterHiker(String cf, String tripName, int tripNumber);
    boolean RegisterCustomer(String cf, String tripName, int tripNumber);
    List<Customer> getCustomers();
    List<Hiker> getHikers();
    List<Trip> getTrips();
    List<Boat> getBoats();
    List<Booking> getBookings();
    List<Instructor> getInstructors();
    Optional<Hiker> checkHiker(String cf);
    Optional<Trip> checkTrip(String tripName, int tripNumber);
    Optional<Boat> checkBoat(String boatModel, int boatNumber);
    Optional<Umbrella> checkUmbrella(String bathhouse, String address, int number);
    boolean addTrip(Trip trip);
    List<Umbrella> getAvailableUmbrellasInBathhouse(String bathhouse, String address, Date startDate, Date endDate);
    List<BookingCount> getBookingCountPerBathhouse();
    List<AnnualCollection> getCollectionPerYear();
    boolean addBooking(String cf, int nUmbrella, String bathhouse, String address, int code, List<Pair<Date, Date>> periods);
    boolean addPeriodIfAbsent(Date startDate, Date endDate);
    boolean addDiscountToBooking(int bookingID, float amount, String description);
    //boolean addEquipmentToBooking(int bookingID, String bathhouse, String address, int tipeID, int number);
    boolean addGuest(int bookingID, String cf);
}