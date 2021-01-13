package project.dao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import project.db.Storage;
import project.lib.Dao;
import project.models.Car;
import project.models.Driver;

@Dao
public class CarDaoImpl implements CarDao {
    @Override
    public Car create(Car car) {
        Storage.addCar(car);
        return car;
    }

    @Override
    public Car get(Long id) {
        return Storage.cars.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .get();
    }

    @Override
    public List<Car> getAll() {
        return Storage.cars;
    }

    @Override
    public Car update(Car car) {
        IntStream.range(0, Storage.cars.size())
                .filter(i -> Storage.cars.get(i).getId().equals(car.getId()))
                .forEach(i -> Storage.cars.set(i, car));
        return car;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.cars.removeIf(c -> c.getId().equals(id));
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        return getAll().stream()
                .filter(c -> c.getDrivers().stream()
                        .map(Driver::getId)
                        .collect(Collectors.toList())
                        .contains(driverId))
                .collect(Collectors.toList());
    }
}
