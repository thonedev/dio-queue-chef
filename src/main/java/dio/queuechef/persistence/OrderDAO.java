package dio.queuechef.persistence;

import dio.queuechef.entity.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends GenericDAO<Order, Long>{
    public final static String tableName = "order";
    public OrderDAO() throws SQLException {
        super(ConnectionUtil.getConnection(), tableName);
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO queuechef_db.`order`\n" +
                "(order_number, items, create_date, is_on_hold, stage_id)\n" +
                "VALUES(?, ?, ?, ?, ?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE queuechef_db.`order`\n" +
                "SET order_number=?, items=?, create_date=?, is_on_hold=?, stage_id=?\n" +
                "WHERE id=?;";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Order entity) throws SQLException {
        stmt.setInt(1, entity.getOrderNumber());
        stmt.setString(2, entity.getItems());
        stmt.setString(3, formatLocalDateTime(entity.getCreateDate()));
        stmt.setBoolean(4, entity.isOnHold());
        stmt.setLong(5, entity.getStageId());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Order entity) throws SQLException {
        stmt.setInt(1, entity.getOrderNumber());
        stmt.setString(2, entity.getItems());
        stmt.setString(3, formatLocalDateTime(entity.getCreateDate()));
        stmt.setBoolean(4, entity.isOnHold());
        stmt.setLong(5, entity.getStageId());
        stmt.setLong(6, entity.getId());
    }

    @Override
    protected Order mapResultSetToEntity(ResultSet rs) throws SQLException {
        Order order = new Order();

        order.setId(rs.getLong("id"));
        order.setOrderNumber(rs.getShort("order_number"));
        order.setItems(rs.getString("items"));
        var createDate = rs.getTimestamp("create_date");
        if(createDate != null)
            order.setCreateDate(createDate.toLocalDateTime());
        order.setOnHold(rs.getBoolean("is_on_hold"));
        order.setStageId(rs.getLong("stage_id"));

        return order;
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    public List<Order> listByStageId(long stageId) throws SQLException {
        String query = "SELECT * FROM " + "`" + tableName + "`" + " WHERE stage_id  = ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, stageId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToEntity(rs));
                }
            }
        }
        return orders;
    }
}
