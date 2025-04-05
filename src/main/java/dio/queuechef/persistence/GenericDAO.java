package dio.queuechef.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T, ID> implements GenericCRUD<T, ID> {
    protected Connection connection;
    protected  String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    protected abstract String getInsertQuery();
    protected abstract String getUpdateQuery();
    protected abstract void setInsertParameters(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract String getIdColumnName();

    @Override
    public void create(T entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getInsertQuery())) {
            setInsertParameters(stmt, entity);
            stmt.executeUpdate();
        }
    }

    @Override
    public T findById(ID id) throws SQLException {
        String query = "SELECT * FROM " + "`" + tableName + "`" + " WHERE " + getIdColumnName() + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<T> findAll() throws SQLException {
        List<T> entities = new ArrayList<>();
        String query = "SELECT * FROM " + "`" + tableName + "`";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        }
        return entities;
    }

    @Override
    public void update(T entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getUpdateQuery())) {
            setUpdateParameters(stmt, entity);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(ID id) throws SQLException {
        String query = "DELETE FROM " + "`" + tableName + "`" + " WHERE " + getIdColumnName() + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    protected String formatLocalDateTime(LocalDateTime date){
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
