package project.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, I> {
    T create(T entity);

    Optional<T> get(I id);

    List<T> getAll();

    T update(T entity);

    boolean delete(I id);
}
