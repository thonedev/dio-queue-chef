package dio.queuechef.persistence;

import java.sql.SQLException;
import java.util.List;

public interface GenericCRUD<T, ID> {
    void create(T entity) throws SQLException;
    T findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T entity) throws SQLException;
    void delete(ID id) throws SQLException;
}
