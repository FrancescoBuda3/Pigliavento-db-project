package Pigliavento.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

public class Utils {
    private Utils() {}
    
    public static java.util.Date sqlDateToDate(final java.sql.Date sqlDate) {
        return sqlDate == null ? null : new java.util.Date(sqlDate.getTime());
    }
    
    public static java.sql.Date dateToSqlDate(final java.util.Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }
    
    public static Optional<java.util.Date> buildDate(final int day, final int month, final int year) {
        try {
            final String dateFormatString = "dd/MM/yyyy";
            final String dateString = day + "/" + month + "/" + year;
            final java.util.Date date = new SimpleDateFormat(dateFormatString, Locale.ITALIAN).parse(dateString);
            return Optional.of(date);
        } catch (final ParseException e) {
            return Optional.empty();
        }
    }

    public static int daysBetween(final java.util.Date date1, final java.util.Date date2) {
        final long diff = date2.getTime() - date1.getTime();
        return (int) (diff / (24 * 60 * 60 * 1000));
    }
}
