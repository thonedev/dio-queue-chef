package dio.queuechef.persistence;

import dio.queuechef.entity.HoldRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoldRecordDAO extends GenericDAO<HoldRecord, Long> {

    public final static String tableName = "hold_record";
    public HoldRecordDAO() throws SQLException {
        super(ConnectionUtil.getConnection(), tableName);
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (hold_reason, hold_date, order_id) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET hold_reason = ?, release_reason = ?, hold_date = ?, release_date = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, HoldRecord entity) throws SQLException {
        stmt.setString(1, entity.getHoldReason());
        stmt.setString(2, formatLocalDateTime(entity.getHoldDate()));
        stmt.setLong(3, entity.getOrderId());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, HoldRecord entity) throws SQLException {
        stmt.setString(1, entity.getHoldReason());
        stmt.setString(2, entity.getReleaseReason());
        stmt.setString(3, formatLocalDateTime(entity.getHoldDate()));
        if(entity.getReleaseDate() != null)
            stmt.setString(4, formatLocalDateTime(entity.getReleaseDate()));
        stmt.setLong(5, entity.getId());
    }

    @Override
    protected HoldRecord mapResultSetToEntity(ResultSet rs) throws SQLException {
        var hold = new HoldRecord();
        hold.setId(rs.getInt("id"));
        hold.setHoldReason(rs.getString("hold_reason"));
        hold.setReleaseReason(rs.getString("release_reason"));
        hold.setOrderId(rs.getLong("order_id"));
        var holdDate = rs.getTimestamp("hold_date");
        if(holdDate != null)
            hold.setHoldDate(holdDate.toLocalDateTime());
        var releaseDate = rs.getTimestamp("release_date");
        if(releaseDate != null)
            hold.setReleaseDate(releaseDate.toLocalDateTime());

        return hold;
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    public List<HoldRecord> listByOrderId(long orderId) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE order_id  = ?";
        List<HoldRecord> holdRecords = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    holdRecords.add(mapResultSetToEntity(rs));
                }
            }
        }
        return holdRecords;
    }
}
