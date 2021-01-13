package project.dao;

import java.util.List;
import java.util.stream.IntStream;
import project.db.Storage;
import project.lib.Dao;
import project.models.Car;

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
}
