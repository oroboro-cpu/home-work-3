package project.db;

import java.util.ArrayList;
import java.util.List;
import project.models.Car;
import project.models.Driver;
import project.models.Manufacturer;

public class Storage {
    public static final List<Manufacturer> manufacturers = new ArrayList<>();
    public static final List<Car> cars = new ArrayList<>();
    public static final List<Driver> drivers = new ArrayList<>();
    private static long manufacturerId;
    private static long carId;
    private static long driverId;

    public static void addManufacturer(Manufacturer manufacturer) {
        manufacturerId++;
        manufacturer.setId(manufacturerId);
        manufacturers.add(manufacturer);
    }

    public static void addCar(Car car) {
        carId++;
        car.setId(carId);
        cars.add(car);
    }

    public static void addDriver(Driver driver) {
        driverId++;
        driver.setId(driverId);
        drivers.add(driver);
    }
}
