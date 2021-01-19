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
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement stSelectById = connection.prepareStatement(selectQuery)) {
            stSelectById.setLong(1, id);
            ResultSet resultSet = stSelectById.executeQuery();
            Car car = null;
            if (resultSet.next()) {
                car = getCarFromResultSet(resultSet);
            }
            return Optional.of(car);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get car with id " + id, e);
        }
    }

    @Override
    public List<Car> getAll() {
        String selectQuery = "SELECT c.car_id, c.car_model,"
                + "c.manufacturer_id, cd.driver_id FROM cars c "
                + "INNER JOIN manufacturers m ON m.manufacturer_id=c.manufacturer_id "
                + "INNER JOIN cars_drivers cd ON cd.car_id=c.car_id "
                + "INNER JOIN drivers d ON d.driver_id=cd.driver_id "
                + "WHERE c.deleted=FALSE AND d.deleted=FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement selectAll = connection.prepareStatement(selectQuery)) {
            ResultSet resultSet = selectAll.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (resultSet.next()) {
                cars.add(getCarFromResultSet(resultSet));
            }
            return cars;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all cars", e);
        }
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        String selectQuery = "SELECT c.car_id, c.car_model,"
                + "c.manufacturer_id, d.driver_id AS driver_id "
                + "FROM cars c "
                + "INNER JOIN cars_drivers cd ON cd.car_id=c.car_id "
                + "INNER JOIN drivers d ON cd.driver_id=d.driver_id "
                + "WHERE cd.driver_id=? AND c.deleted=FALSE AND d.deleted=FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement selectCars = connection.prepareStatement(selectQuery)) {
            selectCars.setLong(1, driverId);
            ResultSet resultSet = selectCars.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (resultSet.next()) {
                cars.add(getCarFromResultSet(resultSet));
            }
            return cars;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get cars by driver id " + driverId, e);
        }
    }

    @Override
    public Car update(Car car) {
        String updateQuery = "UPDATE cars SET car_model=?, manufacturer_id=? WHERE car_id=? "
                + "AND deleted=FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement stUpdateCar = connection
                        .prepareStatement(updateQuery)) {
            stUpdateCar.setString(1, car.getName());
            stUpdateCar.setLong(2, car.getManufacturer().getId());
            stUpdateCar.setLong(3, car.getId());
            stUpdateCar.executeUpdate();
            stUpdateCar.close();
            deleteOldDrivers(car, connection);
            putDrivers(car, connection);
            return car;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update car " + car, e);
        }
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

    private void deleteOldDrivers(Car car, Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM cars_drivers WHERE car_id=?";
        PreparedStatement deleteStatement = connection
                .prepareStatement(deleteQuery);
        deleteStatement.setLong(1, car.getId());
        deleteStatement.executeUpdate();
        deleteStatement.close();
    }

    private void putDrivers(Car car, Connection connection) throws SQLException {
        String queryPut = "INSERT INTO cars_drivers (driver_id, car_id)"
                + "VALUES(?, ?)";
        PreparedStatement stInsertDrivers = connection
                .prepareStatement(queryPut);
        stInsertDrivers.setLong(2, car.getId());
        for (project.models.Driver driver : car.getDrivers()) {
            stInsertDrivers.setLong(1, driver.getId());
            stInsertDrivers.executeUpdate();
        }
        stInsertDrivers.close();
    }

    private Car getCarFromResultSet(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        car.setId(resultSet.getObject("car_id", Long.class));
        car.setName(resultSet.getString("car_model"));
        car.setManufacturer(getManufacturerById(resultSet.getObject("manufacturer_id",
                Long.class)));
        List<Driver> drivers = new ArrayList<>();
        while (resultSet.next()) {
            drivers.add(getDriverById(resultSet.getObject("driver_id", Long.class)));
        }
        car.setDrivers(drivers);
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
                manufacturer.setName(resultSet.getString("manufacturer_name"));
                manufacturer.setCountry(resultSet.getString("manufacturer_country"));
            }
            return manufacturer;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get manufacturer with id " + id, e);
        }
    }

    private Driver getDriverById(Long id) {
        String selectQuery = "SELECT * FROM drivers WHERE driver_id=? AND deleted=FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setLong(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
            Driver driver = new Driver();
            if (resultSet.next()) {
                driver.setId(resultSet.getObject("driver_id", Long.class));
                driver.setName(resultSet.getString("driver_name"));
                driver.setLicenseNumber(resultSet.getString("driver_license_number"));
            }
            return driver;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get driver with id " + id, e);
        }
    }
}
