package project.service;

import java.util.List;
import java.util.Optional;
import project.models.Driver;

public interface DriverService {
    Driver create(Driver driver);

    Optional<Driver> get(Long id);

    List<Driver> getAll();

    Driver update(Driver driver);

    boolean delete(Long id);
}
