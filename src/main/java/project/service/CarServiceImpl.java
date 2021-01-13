package project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import project.dao.CarDao;
import project.lib.Inject;
import project.lib.Service;
import project.models.Car;
import project.models.Driver;

@Service
public class CarServiceImpl implements CarService {
    @Inject
    private CarDao carDao;

    @Override
    public Car create(Car car) {
        return carDao.create(car);
    }

    @Override
    public Car get(Long id) {
        return carDao.get(id);
    }

    @Override
    public List<Car> getAll() {
        return carDao.getAll();
    }

    @Override
    public Car update(Car car) {
        return carDao.update(car);
    }

    @Override
    public boolean delete(Long id) {
        return carDao.delete(id);
    }

    @Override
    public void addDriverToCar(Driver driver, Car car) {
        List<Driver> drivers = new ArrayList<>(car.getDrivers());
        drivers.add(driver);
        car.setDrivers(drivers);
        update(car);
    }

    @Override
    public void removeDriverFromCar(Driver driver, Car car) {
        List<Driver> driverList = new ArrayList<>(car.getDrivers());
        Driver driverToRemove = car.getDrivers().stream()
                .filter(d -> d.equals(driver))
                .findFirst()
                .orElseThrow();
        driverList.remove(driverToRemove);
        car.setDrivers(driverList);
        update(car);
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
