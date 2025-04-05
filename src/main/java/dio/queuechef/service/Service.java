package dio.queuechef.service;

import dio.queuechef.entity.HoldRecord;
import dio.queuechef.entity.Order;
import dio.queuechef.entity.PreparationQueue;
import dio.queuechef.entity.Stage;
import dio.queuechef.persistence.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class Service {
    private final PreparationQueueDAO preparationQueueDAO = new PreparationQueueDAO();
    private final StageDAO stageDAO = new StageDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final HoldRecordDAO holdRecordDAO = new HoldRecordDAO();
    private final Scanner sc = new Scanner(System.in);

    public Service() throws SQLException {
    }

    public void printPreparationQueues() throws SQLException {
        var queues = preparationQueueDAO.findAll();
        for (var queue : queues) {
            var stages = stageDAO.listByPreparationQueueId(queue.getId());
            for (var stage : stages) {
                var orders = orderDAO.listByStageId(stage.getId());
                for (var order : orders) {
                    var holders = holdRecordDAO.listByOrderId(order.getId());
                    order.setHoldRecords(holders);
                }
                stage.setOrders(orders);
            }
            queue.setStages(stages);
        }
        System.out.println("-------++==Queues==++-------");
        for (var queue : queues) {
            System.out.println(queue);
        }
        System.out.println("----------------------------");

    }

    public PreparationQueue getQueueById(long queueId) throws SQLException {
        var queue = preparationQueueDAO.findById(queueId);
        var stages = stageDAO.listByPreparationQueueId(queue.getId());
        for (var stage : stages) {
            var orders = orderDAO.listByStageId(stage.getId());
            for (var order : orders) {
                var holders = holdRecordDAO.listByOrderId(order.getId());
                order.setHoldRecords(holders);
            }
            stage.setOrders(orders);
        }
        queue.setStages(stages);
        return queue;
    }

    public void createStage() throws SQLException {
        System.out.print("Entre como o nome do estágio: ");
        String name = sc.nextLine();
        System.out.print("\nQual a ordem? ");
        short stageOrder = sc.nextShort();
        sc.nextLine();
        printPreparationQueues();
        System.out.print("\nQual o ID da Fila? ");
        long preparationQueueId = sc.nextLong();
        sc.nextLine();
        var stage = new Stage();
        stage.setName(name);
        stage.setStageOrder(stageOrder);
        stage.setPreparationQueueId(preparationQueueId);

        stageDAO.create(stage);
    }

    public void printStages(long queueId) throws SQLException {
        var stages = stageDAO.listByPreparationQueueId(queueId);
        for (var stage : stages) {
            var orders = orderDAO.listByStageId(stage.getId());
            for (var order : orders) {
                var holders = holdRecordDAO.listByOrderId(order.getId());
                order.setHoldRecords(holders);
            }
            stage.setOrders(orders);
        }
        System.out.println("-------++==Stages==++-------");
        for (var stage : stages) {
            System.out.println(stage);
        }
        System.out.println("----------------------------");
    }

    public void createOrder() throws SQLException {
        System.out.print("Entre os Itens: ");
        String itens = sc.nextLine();
        System.out.print("\nQual o estágio? ");
        long stageid = sc.nextLong();
        sc.nextLine();

        var order = new Order();
        order.setOrderNumber((short) new Random().nextInt(Short.MAX_VALUE));
        order.setItems(itens);
        order.setStageId(stageid);
        order.setCreateDate(LocalDateTime.now());
        order.setOnHold(false);
        orderDAO.create(order);
    }

    public void moveStage() throws SQLException {
        System.out.print("Qual ordem deseja mover? ");
        var ordermId = sc.nextLong();
        var order = orderDAO.findById(ordermId);
        var stage = stageDAO.findById(order.getStageId());
        var stages = stageDAO.findAll();
        if (stage.getStageOrder() != 3 && stage.getStageOrder() != 4) {
            var nextStage = stages.stream().sorted(Comparator.comparing(Stage::getStageOrder))
                    .filter(c -> stage.getStageOrder() + 1 == c.getStageOrder())
                    .findFirst();
            order.setStageId(nextStage.orElseThrow().getId());
            orderDAO.update(order);
        }
    }

    public void cancelOrder() throws SQLException {
        System.out.print("Qual ordem deseja cancelar? ");
        var ordermId = sc.nextLong();
        sc.nextLine();
        var order = orderDAO.findById(ordermId);
        var stage = stageDAO.findById(order.getStageId());
        var stages = stageDAO.findAll();
        var nextStage = stages.stream().sorted(Comparator.comparing(Stage::getStageOrder))
                .filter(c -> c.getName().equals("Cancelado"))
                .findFirst();
        order.setStageId(nextStage.orElseThrow().getId());
        orderDAO.update(order);
    }

    public void addHold() throws SQLException {
        System.out.print("Qual ordem deseja bloquear?: ");
        long orderId = sc.nextLong();
        sc.nextLine();
        var order = orderDAO.findById(orderId);
        if(order.isOnHold()){
            System.out.println("Pedido já está bloqueado!");
            return;
        }

        System.out.print("Qual o motivo? ");
        String cause = sc.nextLine();
        order.setOnHold(true);
        orderDAO.update(order);


        var hold = new HoldRecord();

        hold.setHoldDate(LocalDateTime.now());
        hold.setHoldReason(cause);
        hold.setOrderId(orderId);
        hold.setOrderId(order.getId());
        holdRecordDAO.create(hold);
    }

    public void removeHold() throws SQLException {
        System.out.print("Qual bloqueio deseja remover?: ");
        long holdId = sc.nextLong();
        sc.nextLine();
        System.out.print("\nQual o motivo? ");
        String cause = sc.nextLine();

        var hold = holdRecordDAO.findById(holdId);

        hold.setReleaseDate(LocalDateTime.now());
        hold.setReleaseReason(cause);

        var order = orderDAO.findById(hold.getOrderId());

        order.setOnHold(false);
        orderDAO.update(order);

        holdRecordDAO.update(hold);
    }

    public void close() throws SQLException {
        preparationQueueDAO.closeConnection();
        stageDAO.closeConnection();
        orderDAO.closeConnection();
        holdRecordDAO.closeConnection();
    }

    public void createPreparationQueue() throws SQLException {
        System.out.print("Digite o nome da nova fila: ");
        String queueName = sc.nextLine();
        var newQueue = new PreparationQueue();
        newQueue.setName(queueName);
        preparationQueueDAO.create(newQueue);

    }
}
