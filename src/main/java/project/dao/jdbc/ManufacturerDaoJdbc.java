package project.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import project.dao.ManufacturerDao;
import project.exception.DataProcessingException;
import project.lib.Dao;
import project.models.Manufacturer;
import project.util.ConnectionUtil;

@Dao
public class ManufacturerDaoJdbc implements ManufacturerDao {

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        String query = "INSERT INTO manufacturers (name, country) "
                + "VALUES (?, ?);";
        try (PreparedStatement preparedStatement
                     = ConnectionUtil.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, manufacturer.getName());
            preparedStatement.setString(2, manufacturer.getCountry());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                manufacturer.setId(resultSet.getObject("GENERATED_KEY", Long.class));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't create manufacturer - " + manufacturer, ex);
        }
        return manufacturer;
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        String query = "SELECT * FROM manufacturers WHERE id = ? AND deleted = false";
        Manufacturer manufacturer = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement gettingStatement = connection.prepareStatement(query)) {
            gettingStatement.setLong(1, id);
            ResultSet result = gettingStatement.executeQuery();
            if (result.next()) {
                manufacturer = getManufacturerFromResultSet(result);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can`t get manufacturer by id" + id, ex);
        }
        return Optional.ofNullable(manufacturer);
    }

    @Override
    public List<Manufacturer> getAll() {
        String query = "SELECT * FROM manufacturers WHERE deleted = false";
        List<Manufacturer> manufacturerList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement gettingAllStatement = connection.prepareStatement(query)) {
            ResultSet result = gettingAllStatement.executeQuery();
            while (result.next()) {
                manufacturerList.add(getManufacturerFromResultSet(result));
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can`t get all manufacturers", ex);
        }
        return manufacturerList;
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        String query = "UPDATE manufacturers SET name = ?, country = ? "
                + "WHERE id = ? AND deleted = false";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updatedStatement = connection.prepareStatement(query)) {
            updatedStatement.setString(1, manufacturer.getName());
            updatedStatement.setString(2, manufacturer.getCountry());
            updatedStatement.setLong(3, manufacturer.getId());
            updatedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can`t update manufacturer - " + manufacturer, ex);
        }
        return manufacturer;
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE manufacturers SET deleted = TRUE WHERE id = ?";
        int result;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deletedStatement = connection.prepareStatement(query)) {
            deletedStatement.setLong(1, id);
            result = deletedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can`t delete manufacturer by id" + id, ex);
        }
        return result > 0;
    }

    private Manufacturer getManufacturerFromResultSet(ResultSet result) {
        try {
            Long id = result.getObject("id", Long.class);
            String name = result.getString("name");
            String country = result.getString("country");
            Manufacturer manufacturer = new Manufacturer(name, country);
            manufacturer.setId(id);
            return manufacturer;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't get manufacturers from ResultSet", ex);
        }
    }
}
