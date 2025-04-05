package dio.queuechef.entity;

import lombok.Data;

import java.util.List;

@Data
public class Stage {
    private long id;
    private String name;
    private short stageOrder;
    private long preparationQueueId;
    List<Order> orders;
}
