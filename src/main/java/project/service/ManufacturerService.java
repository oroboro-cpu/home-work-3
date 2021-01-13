package project.service;

import java.util.List;
import project.models.Manufacturer;

public interface ManufacturerService {
    Manufacturer create(Manufacturer manufacturer);

    Manufacturer get(Long id);

    List<Manufacturer> getAll();

    Manufacturer update(Manufacturer manufacturer);

    boolean delete(Long id);
}
