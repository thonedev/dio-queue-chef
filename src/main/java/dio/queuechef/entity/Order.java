package dio.queuechef.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private long id;
    private short orderNumber;
    private String items;
    private LocalDateTime createDate;
    private boolean isOnHold;
    private long stageId;
    List<HoldRecord> holdRecords;
}
