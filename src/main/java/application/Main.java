package application;

import project.lib.Injector;
import project.models.Manufacturer;
import project.service.ManufacturerService;

public class Main {
    private static Injector injector = Injector.getInstance("project");

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
    }
}
