package project.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import project.dao.DriverDao;
import project.exception.DataProcessingException;
import project.lib.Dao;
import project.models.Driver;
import project.util.ConnectionUtil;

@Dao
public class DriverDaoJdbc implements DriverDao {
    @Override
    public Driver create(Driver driver) {
        String createQuery = "INSERT INTO drivers (name, "
                + "license_number, login, password) VALUES (?,?,?,?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(createQuery,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, driver.getName());
            statement.setString(2, driver.getLicenseNumber());
            statement.setString(3, driver.getLogin());
            statement.setString(4, driver.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                driver.setId(resultSet.getObject("GENERATED_KEY", Long.class));
            }
            return driver;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't create a driver in the DB - " + driver, ex);
        }
    }

    @Override
    public Optional<Driver> get(Long id) {
        String getQuery = "SELECT * FROM drivers WHERE id=? AND deleted=false;";
        Driver driver = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                driver = createDriver(resultSet);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get driver from DB with driver id: " + id, ex);
        }
        return Optional.ofNullable(driver);
    }

    @Override
    public List<Driver> getAll() {
        String getAllQuery = "SELECT * FROM drivers WHERE deleted=false";
        List<Driver> outputList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getAllQuery)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                outputList.add(createDriver(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get all drivers from DB", ex);
        }
        return outputList;
    }

    @Override
    public Driver update(Driver driver) {
        String updateQuery = "UPDATE drivers SET name=?, license_number=?, login=?, password=?"
                + " WHERE id=? AND deleted=false;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, driver.getName());
            statement.setString(2, driver.getLicenseNumber());
            statement.setString(3, driver.getLogin());
            statement.setString(4, driver.getPassword());
            statement.setLong(5, driver.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't update driver - " + driver, ex);
        }
        return driver;
    }

    @Override
    public boolean delete(Long id) {
        String deleteQuery = "UPDATE drivers SET deleted=true WHERE id=?;";
        int result;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setLong(1, id);
            result = statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't delete driver from DB by id - " + id, ex);
        }
        return result > 0;
    }

    private Driver createDriver(ResultSet resultSet) {
        try {
            String name = resultSet.getString("name");
            String driverLicense = resultSet.getString("license_number");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            Long driverId = resultSet.getObject("id", Long.class);
            Driver driver = new Driver(name, driverLicense);
            driver.setId(driverId);
            driver.setLogin(login);
            driver.setPassword(password);
            return driver;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't create new driver", ex);
        }
    }

    @Override
    public Optional<Driver> getByLogin(String login) {
        String getQuery = "SELECT * FROM drivers WHERE login=? AND deleted=false;";
        Driver driver = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getQuery)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                driver = createDriver(resultSet);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't find driver from DB by login: " + login, ex);
        }
        return Optional.ofNullable(driver);
    }
}
