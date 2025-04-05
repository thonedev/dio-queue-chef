package dio.queuechef.entity;

import lombok.Data;

import java.util.List;

@Data
public class PreparationQueue {
    private long id;
    private String name;
    private List<Stage> stages;
}
