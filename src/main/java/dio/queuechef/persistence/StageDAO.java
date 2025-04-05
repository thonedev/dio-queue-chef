package dio.queuechef.persistence;

import dio.queuechef.entity.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StageDAO extends GenericDAO<Stage, Long> {
    private static final String tableName = "stage";
    public StageDAO() throws SQLException {
        super(ConnectionUtil.getConnection(), tableName);
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO stage\n" +
                "(name, stage_order, preparation_queue_id)\n" +
                "VALUES(?, ?, ?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE stage\n" +
                "SET name=?, stage_order=?, preparation_queue_id=?\n" +
                "WHERE id=?;";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Stage entity) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setShort(2, entity.getStageOrder());
        stmt.setLong(3, entity.getPreparationQueueId());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Stage entity) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setShort(2, entity.getStageOrder());
        stmt.setLong(3, entity.getPreparationQueueId());
        stmt.setLong(4, entity.getId());
    }

    @Override
    protected Stage mapResultSetToEntity(ResultSet rs) throws SQLException {
        var stage = new Stage();

        stage.setId(rs.getLong("id"));
        stage.setName(rs.getString("name"));
        stage.setStageOrder(rs.getShort("stage_order"));
        stage.setPreparationQueueId(rs.getLong("preparation_queue_id"));

        return stage;
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    public List<Stage> listByPreparationQueueId(long queueId) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE preparation_queue_id  = ?";
        List<Stage> stages = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, queueId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stages.add(mapResultSetToEntity(rs));
                }
            }
        }
        return stages;
    }
}
