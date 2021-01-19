package project.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import project.dao.CarDao;
import project.exception.DataProcessingException;
import project.lib.Dao;
import project.models.Car;
import project.models.Driver;
import project.models.Manufacturer;
import project.util.ConnectionUtil;

@Dao
public class CarDaoJdbc implements CarDao {
    @Override
    public Car create(Car car) {
        String selectQuery = "INSERT INTO cars (car_model, manufacturer_id) "
                + "VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement stInsert = connection.prepareStatement(selectQuery,
                        Statement.RETURN_GENERATED_KEYS)) {
            stInsert.setString(1, car.getName());
            stInsert.setLong(2, car.getManufacturer().getId());
            stInsert.execute();
            ResultSet resultSet = stInsert.getGeneratedKeys();
            if (resultSet.next()) {
                car.setId(resultSet.getObject("GENERATED_KEY", Long.class));
            }
            return car;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't insert car " + car, e);
        }
    }

    @Override
    public Optional<Car> get(Long id) {
        String selectQuery = "SELECT c.car_id, c.car_model,"
                + "c.manufacturer_id, cd.driver_id FROM cars c "
                + "INNER JOIN manufacturers m ON m.manufacturer_id=c.manufacturer_id "
                + "INNER JOIN cars_drivers cd ON cd.car_id=c.id "
                + "INNER JOIN drivers d ON d.driver_id=cd.driver_id "
                + "WHERE c.deleted=FALSE AND d.deleted=FALSE AND c.car_id=?";
        Car car = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement selectById = connection.prepareStatement(selectQuery)) {
            selectById.setLong(1, id);
            ResultSet resultSet = selectById.executeQuery();
            if (resultSet.next()) {
                car = getCarFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get car with id " + id, e);
        }
        if (car != null) {
            car.setDrivers(getDriverByCarId(id));
        }
        return Optional.ofNullable(car);
    }

    @Override
    public List<Car> getAll() {
        String selectQuery = "SELECT c.car_id, c.car_model, c.manufacturer_id,"
                + "m.manufacturer_name, m.manufacturer_country "
                + "FROM cars c "
                + "INNER JOIN manufacturers m ON m.manufacturer_id=c.manufacturer_id "
                + "WHERE c.deleted=FALSE";
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement selectAll = connection.prepareStatement(selectQuery)) {
            ResultSet resultSet = selectAll.executeQuery();
            while (resultSet.next()) {
                cars.add(getCarFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all cars", e);
        }
        for (Car car : cars) {
            car.setDrivers(getDriverByCarId(car.getId()));
        }
        return cars;
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        String querySelectCars = "SELECT * FROM cars c"
                + " INNER JOIN cars_drivers cd ON cd.car_id = c.car_id"
                + " INNER JOIN manufacturers m ON c.manufacturer_id = m.manufacturer_id"
                + " INNER JOIN drivers d ON d.driver_id = cd.driver_id"
                + " WHERE cd.driver_id = ? AND m.deleted = false"
                + " AND d.deleted = false AND c.deleted = false";
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement selectCars = connection.prepareStatement(querySelectCars)) {
            selectCars.setLong(1, driverId);
            ResultSet resultSet = selectCars.executeQuery();
            while (resultSet.next()) {
                cars.add(getCarFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get cars by driver id: " + driverId, ex);
        }
        for (Car car : cars) {
            car.setDrivers(getDriverByCarId(car.getId()));
        }
        return cars;
    }

    @Override
    public Car update(Car car) {
        String updateQuery = "UPDATE cars SET car_model=?, manufacturer_id=? WHERE car_id=? "
                + "AND deleted=FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement update = connection
                        .prepareStatement(updateQuery)) {
            update.setString(1, car.getName());
            update.setLong(2, car.getManufacturer().getId());
            update.setLong(3, car.getId());
            update.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update car " + car, e);
        }
        deleteOldDrivers(car);
        putDrivers(car);
        return car;
    }

    @Override
    public boolean delete(Long id) {
        String deleteQuery = "UPDATE cars SET deleted=TRUE WHERE car_id=?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setLong(1, id);
            return deleteStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete car with id " + id, e);
        }
    }

    private void deleteOldDrivers(Car car) {
        String deleteQuery = "DELETE FROM cars_drivers WHERE car_id=?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setLong(1, car.getId());
            deleteStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't delete car: " + car + " from DB", ex);
        }
    }

    private void putDrivers(Car car) {
        String queryPut = "INSERT INTO cars_drivers (driver_id, car_id)"
                + "VALUES(?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(queryPut)) {
            statement.setLong(2, car.getId());
            for (project.models.Driver driver : car.getDrivers()) {
                statement.setLong(1, driver.getId());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't put car: " + car + " to the DB", ex);
        }
    }

    private Car getCarFromResultSet(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        car.setId(resultSet.getObject("car_id", Long.class));
        car.setName(resultSet.getString("car_model"));
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(resultSet.getObject("manufacturer_id", Long.class));
        manufacturer.setName(resultSet.getObject("manufacturer_name", String.class));
        manufacturer.setCountry(resultSet.getObject("manufacturer_country", String.class));
        car.setManufacturer(manufacturer);
        return car;
    }

    private Manufacturer getManufacturerById(Long id) {
        String selectQuery = "SELECT * FROM manufacturers WHERE manufacturer_id=?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setLong(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
            Manufacturer manufacturer = new Manufacturer();
            if (resultSet.next()) {
                manufacturer.setId(id);
                manufacturer.setName(resultSet.getObject("manufacturer_name", String.class));
                manufacturer.setCountry(resultSet.getObject("manufacturer_country", String.class));
            }
            return manufacturer;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get manufacturer by id: " + id, ex);
        }
    }

    private List<Driver> getDriverByCarId(Long id) {
        String selectQuery = "SELECT * FROM cars_drivers cd"
                + " INNER JOIN drivers d ON cd.driver_id = d.driver_id"
                + " WHERE cd.car_id = ? AND d.deleted = false";
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement selectDrivers = connection
                        .prepareStatement(selectQuery)) {
            selectDrivers.setLong(1, id);
            ResultSet resultSet = selectDrivers.executeQuery();
            while (resultSet.next()) {
                Driver driver = new Driver();
                driver.setId(resultSet.getObject("driver_id", Long.class));
                driver.setName(resultSet.getObject("driver_name", String.class));
                driver.setLicenseNumber(resultSet
                        .getObject("driver_license_number", String.class));
                drivers.add(driver);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get drivers by id: " + id, ex);
        }
        return drivers;
    }
}
