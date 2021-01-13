package project.dao;

import java.util.List;
import project.models.Driver;

public interface DriverDao {
    Driver create(Driver driver);

    Driver get(Long id);

    List<Driver> getAll();

    Driver update(Driver driver);

    boolean delete(Long id);
}
