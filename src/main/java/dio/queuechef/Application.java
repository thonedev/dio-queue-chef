package dio.queuechef;

import dio.queuechef.entity.HoldRecord;
import dio.queuechef.persistence.ConnectionUtil;
import dio.queuechef.persistence.HoldRecordDAO;
import dio.queuechef.service.Service;
import org.flywaydb.core.Flyway;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

//@SpringBootApplication
public class Application {

	static void clearScreen() {
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				System.out.flush();
			} else {
				System.out.print("\033[H\033[2J");
				System.out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		var flyway = Flyway.configure()
				.dataSource("jdbc:mysql://localhost/queuechef_db", "root", "root")
				.load();
		System.out.println("Iniciando migração...");

		flyway.migrate(); // <-- Thread principal espera aqui até terminar

		System.out.println("Migração concluída!");
		System.out.flush();




		try{
			menu();
		}catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	static void menu() throws SQLException {
		var sc = new Scanner(System.in);
		var service = new Service();
		while (true) {
			clearScreen();
			System.out.println("-------+++=== Fila de Preparo Sys ===+++-------");
			System.out.println("1 - Exibir todas as filas de preparação");
			System.out.println("2 - Exibir estágios de uma fila específica");
			System.out.println("3 - Criar nova fila de preparação");
			System.out.println("4 - Criar novo estágio");
			System.out.println("5 - Criar novo pedido");
			System.out.println("6 - Mover pedido para próximo estágio");
			System.out.println("7 - Cancelar pedido");
			System.out.println("8 - Adicionar bloqueio a um pedido");
			System.out.println("9 - Remover bloqueio de um pedido");
			System.out.println("10 - Sair");
			System.out.println("-----------------------------------------------");
			System.out.print("Escolha uma opção: ");
			var opt = sc.nextInt();
			sc.nextLine(); // Consume newline

			switch (opt) {
				case 1:
					clearScreen();
					service.printPreparationQueues();
					System.out.println("\nPressione Enter para continuar...");
					sc.nextLine();
					break;
				case 2:
					System.out.print("Digite o ID da fila: ");
					long queueId = sc.nextLong();
					sc.nextLine();
					clearScreen();
					service.printStages(queueId);
					System.out.println("\nPressione Enter para continuar...");
					sc.nextLine();
					break;
				case 3:
					service.createPreparationQueue();
					System.out.println("Fila criada com sucesso!");
					System.out.println("Pressione Enter para continuar...");
					sc.nextLine();
					break;
				case 4:
					service.createStage();
					System.out.println("Estágio criado com sucesso!");
					System.out.println("Pressione Enter para continuar...");
					sc.nextLine();
					break;
				case 5:
					service.createOrder();
					System.out.println("Pedido criado com sucesso!");
					System.out.println("Pressione Enter para continuar...");
					sc.nextLine();
					break;
				case 6:
					service.moveStage();
					System.out.println("Pedido movido com sucesso!");
					System.out.println("Pressione Enter para continuar...");
					sc.nextLine();
					break;
				case 7:
					service.cancelOrder();
					System.out.println("Pedido cancelado com sucesso!");
					System.out.println("Pressione Enter para continuar...");
					sc.nextLine();
					break;
				case 8:
					service.addHold();
					System.out.println("Bloqueio adicionado com sucesso!");
					System.out.println("Pressione Enter para continuar...");
					sc.nextLine();
					break;
				case 9:
					service.removeHold();
					System.out.println("Bloqueio removido com sucesso!");
					System.out.println("Pressione Enter para continuar...");
					sc.nextLine();
					break;
				case 10:
					System.out.println("Saindo...");
					service.close();
					sc.close();
					return;
				default:
					System.out.println("Opção inválida! Tente novamente.");
					System.out.println("Pressione Enter para continuar...");
					sc.nextLine();
			}
		}
	}

}
