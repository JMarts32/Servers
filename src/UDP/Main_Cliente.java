package UDP;

import java.util.Scanner;

public class Main_Cliente {

    private static Scanner input;
    private static int numClientes;

    public static void main(String[] args) {
        // Pregunta al usuario cuantos clientes quiere crear
        input = new Scanner(System.in);
        System.out.println("¿Cuantos clientes quieres crear?");
        numClientes = input.nextInt();
        // Crea los clientes
        for (int i = 0; i < numClientes; i++) {
            Cliente cliente = new Cliente(i);
            cliente.start();
        }
    }
}

