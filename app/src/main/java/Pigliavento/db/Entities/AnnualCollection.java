package Pigliavento.db.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnualCollection {
    
    private final int year;
    private final float amount;

    public AnnualCollection(int year, float amount) {
        this.year = year;
        this.amount = amount;
    }

    public int getYear() {
        return this.year;
    }

    public float getAmount() {
        return this.amount;
    }

    public static List<AnnualCollection> readAnnualCollectionFromResultSet(final ResultSet resultSet) {
        final List<AnnualCollection> annualCollections = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int year = resultSet.getInt("anno");
                final float amount = resultSet.getFloat("incasso");
                final AnnualCollection annualCollection = new AnnualCollection(year, amount);
                annualCollections.add(annualCollection);
            }
        } catch (final SQLException e) {}
        return annualCollections;
    }
}
