package application;

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
        Manufacturer manufacturer4 = new Manufacturer("BMW", "Germany");
        Manufacturer manufacturer5 = new Manufacturer("Suzuki", "Japan");

        manufacturerService.create(manufacturer1);
        manufacturerService.create(manufacturer2);
        manufacturerService.create(manufacturer3);
        manufacturerService.create(manufacturer4);
        manufacturerService.create(manufacturer5);
        System.out.println(manufacturerService.getAll());

        manufacturer1.setCountry("Ukraine");
        manufacturerService.update(manufacturer1);
        manufacturerService.delete(3L);
        manufacturerService.delete(4L);
        System.out.println(manufacturerService.getAll());

        Driver driver1 = new Driver("Bob", "1111-qqqq");
        Driver driver2 = new Driver("Alice", "2222-wwww");
        Driver driver3 = new Driver("John", "3333-eeee");
        Driver driver4 = new Driver("Bill", "4444-rrrr");
        Driver driver5 = new Driver("Rax", "5555-tttt");

        DriverService driverService = (DriverService) injector.getInstance(DriverService.class);
        driverService.create(driver1);
        driverService.create(driver2);
        driverService.create(driver3);
        driverService.create(driver4);
        driverService.create(driver5);
        System.out.println(driverService.getAll());

        driverService.delete(driver1.getId());
        driver2.setName("Vasya");
        driverService.update(driver2);
        System.out.println(driverService.getAll());

        CarService carService = (CarService) injector.getInstance(CarService.class);
        Car car1 = new Car("BMW", manufacturer1);
        Car car2 = new Car("Toyota", manufacturer2);
        Car car3 = new Car("Lexus", manufacturer3);
        Car car4 = new Car("Nissan", manufacturer4);
        Car car5 = new Car("Volga", manufacturer5);
        carService.create(car1);
        carService.create(car2);
        carService.create(car3);
        carService.create(car4);
        carService.create(car5);

        System.out.println(carService.getAll());
        car1.setName("Ford");
        carService.update(car1);
        carService.delete(car2.getId());
        carService.addDriverToCar(driverService.create(new Driver("Alex", "6666-oooo")), car1);
        System.out.println(carService.getAll());
        carService.removeDriverFromCar(driver1, car1);
        System.out.println(carService.getAll());
    }
}
