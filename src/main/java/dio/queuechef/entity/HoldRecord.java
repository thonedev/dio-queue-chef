package dio.queuechef.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HoldRecord {
    private long id;
    private String holdReason;
    private String releaseReason;
    private LocalDateTime holdDate;
    private LocalDateTime releaseDate;
    private long orderId;
}
