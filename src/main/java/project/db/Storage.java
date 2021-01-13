package project.db;

import java.util.ArrayList;
import java.util.List;
import project.models.Manufacturer;

public class Storage {
    public static final List<Manufacturer> manufacturers = new ArrayList<>();
    private static long manufacturerId;

    public static void addManufacturer(Manufacturer manufacturer) {
        manufacturerId++;
        manufacturer.setId(manufacturerId);
        manufacturers.add(manufacturer);
    }
}
