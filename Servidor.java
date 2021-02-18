import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Servidor {
    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        serverSocket = creaListenSocket(Integer.parseInt(args[0]));

        while(true){
            clientSocket = creaClientSocket(serverSocket);
            Runnable servidor = new ContadorDeVocales(serverSocket, clientSocket);
            new Thread(servidor).start();
        }
        // se cierra el socket
        //serverSocket.close();
    }

    private static ServerSocket creaListenSocket(int serverSockNum) {
		ServerSocket server = null;

		try {
			server = new ServerSocket(serverSockNum);
		} catch (IOException e) {
			System.err.println("Problems in port: " + serverSockNum);
			System.exit(-1);
		}
		return server;
	}

	// Establecer conexi�n con el servidor y devolver socket
	// si no lo logra abortar programa
	private static Socket creaClientSocket(ServerSocket server) {
		Socket res = null;

		try {
			res = server.accept();
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}
		return res;
	}
}
