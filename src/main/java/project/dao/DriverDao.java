package project.dao;

import java.util.Optional;
import project.models.Driver;

public interface DriverDao extends GenericDao<Driver, Long> {
    Optional<Driver> getByLogin(String login);
}
