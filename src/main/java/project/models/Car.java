package project.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Car {
    private Long id;
    private String name;
    private Manufacturer manufacturer;
    private List<Driver> drivers;

    public Car() {
    }

    public Car(String name, Manufacturer manufacturer) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.drivers = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManufacturer(project.models.Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return Objects.equals(id, car.id)
                && Objects.equals(name, car.name)
                && Objects.equals(manufacturer, car.manufacturer)
                && Objects.equals(drivers, car.drivers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, manufacturer, drivers);
    }

    @Override
    public String toString() {
        return "Car{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", manufacturer=" + manufacturer
                + ", drivers=" + drivers
                + '}';
    }
}
