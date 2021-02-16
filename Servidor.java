import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Servidor implements Runnable{

	private static int SERVER_PORT;
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private PrintWriter salHaciaCliente = null;
	private BufferedReader entDesdeCliente = null;

	public Servidor(ServerSocket serverSocket, Socket clientSocket) {
		this.serverSocket = serverSocket;
		this.clientSocket = clientSocket;
		
	}

	// Crear un socket servidor
	// si no lo logra abortar programa
	

	// Devuelve la cantidad de vocales de la frase
	private static int numeroDeVocales(String frase) {
		int res = 0;
		String fraseMin = frase.toLowerCase();

		for (int i = 0; i < fraseMin.length(); ++i) {
			switch (fraseMin.charAt(i)) {
				case 'a':
				case 'á':
				case 'e':
				case 'é':
				case 'i':
				case 'í':
				case 'o':
				case 'ó':
				case 'u':
				case 'ú':
					res++;
					break;
				default:
					// se ignoran las dem�s letras
			}
		}
		return res;
	}

	public void run(){

		
		try{
			salHaciaCliente = new PrintWriter(clientSocket.getOutputStream(), true);
			entDesdeCliente = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		}catch (IOException e) {
   			System.err.println(e);
   			System.exit(-1);
   		}

		String inputLine = "";
		try{
			inputLine = entDesdeCliente.readLine();

			while ((inputLine != null) && (!inputLine.equals("END OF SERVICE"))) {
				// Contar la cantidad de vocales que

				String respuesta = "'" + inputLine + "' has " + + numeroDeVocales(inputLine) + " vowels";

				// Enviar la respuesta al cliente
				salHaciaCliente.println(respuesta);

				// Recibir nueva petici�n del cliente
				inputLine = entDesdeCliente.readLine();
			}
			// Al cerrar cualquier canal de comunicaci�n
			// utilizado por un socket, �ste se cierra.
			// Para asegurarse que se env�en las respuestas que
			// est�n en el buffer se cierra el OutputStream.
			entDesdeCliente.close();
			salHaciaCliente.close();
			

		}catch (IOException e) {
   			System.err.println(e);
   			System.exit(-1);
   		}

		System.out.println("Bye, bye.");
	}

}