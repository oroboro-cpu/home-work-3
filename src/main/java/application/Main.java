package application;

import java.util.ArrayList;
import java.util.List;
import project.lib.Injector;
import project.models.Car;
import project.models.Driver;
import project.models.Manufacturer;
import project.service.CarService;
import project.service.DriverService;
import project.service.ManufacturerService;

public class Main {
    private static final Injector injector = Injector.getInstance("project");

    public static void main(String[] args) {
        ManufacturerService manufacturerService =
                (ManufacturerService) injector.getInstance(ManufacturerService.class);
        Manufacturer manufacturer1 = new Manufacturer("Volga", "Russia");
        Manufacturer manufacturer2 = new Manufacturer("Daewoo", "Korea");
        Manufacturer manufacturer3 = new Manufacturer("Fiat", "Italy");

        manufacturerService.create(manufacturer1);
        manufacturerService.create(manufacturer2);
        manufacturerService.create(manufacturer3);
        System.out.println(manufacturerService.getAll());

        manufacturer1.setCountry("Ukraine");
        manufacturerService.update(manufacturer1);
        manufacturerService.delete(3L);
        System.out.println(manufacturerService.getAll());

        Driver driver1 = new Driver("Bob", "1111-qqqq");
        Driver driver2 = new Driver("Alice", "2222-wwww");
        Driver driver3 = new Driver("John", "3333-eeee");

        List<Driver> driverList1 = new ArrayList<>();
        List<Driver> driverList2 = new ArrayList<>();
        driverList1.add(driver1);
        driverList1.add(driver2);
        driverList2.add(driver3);
        driverList2.add(driver2);

        DriverService driverService = (DriverService) injector.getInstance(DriverService.class);
        driverService.create(driver1);
        driverService.create(driver2);
        driverService.create(driver3);
        System.out.println(driverService.getAll());

        driverService.delete(driver1.getId());
        driver2.setName("Vasya");
        driverService.update(driver2);
        System.out.println(driverService.getAll());

        CarService carService = (CarService) injector.getInstance(CarService.class);
        Car car1 = new Car("BMW", manufacturer1, driverList1);
        Car car2 = new Car("Toyota", manufacturer2, driverList2);
        carService.create(car1);
        carService.create(car2);
        System.out.println(carService.getAll());

        car1.setName("Lexus");
        carService.update(car1);
        carService.delete(car2.getId());
        System.out.println(carService.getAll());

        carService.addDriverToCar(driverService.create(new Driver("Alex", "4444-rrrr")), car1);
        System.out.println(carService.getAll());
        System.out.println(carService.getAllByDriver(driver1.getId()));
        carService.removeDriverFromCar(driver1, car1);
        System.out.println(carService.getAll());
    }
}
