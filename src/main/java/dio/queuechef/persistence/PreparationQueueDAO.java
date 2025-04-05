package dio.queuechef.persistence;

import dio.queuechef.entity.PreparationQueue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreparationQueueDAO extends GenericDAO<PreparationQueue, Long> {
    private static final String tableName = "preparation_queue";
    public PreparationQueueDAO() throws SQLException {
        super(ConnectionUtil.getConnection(), tableName);
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + "\n" +
                "(name)\n" +
                "VALUES(?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + "\n" +
                "SET name=?\n" +
                "WHERE id=?;";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, PreparationQueue entity) throws SQLException {
        stmt.setString(1, entity.getName());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, PreparationQueue entity) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setLong(2, entity.getId());
    }

    @Override
    protected PreparationQueue mapResultSetToEntity(ResultSet rs) throws SQLException {
        var preparationQueue = new PreparationQueue();
        preparationQueue.setId(rs.getLong("id"));
        preparationQueue.setName(rs.getString("name"));

        return preparationQueue;
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }
}
