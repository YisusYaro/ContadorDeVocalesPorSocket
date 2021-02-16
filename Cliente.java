import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Cliente {

	// Indicar la direcci�n y el n�mero de puerto donde escuchar� el proceso servidor
	static private String SERVER_ADDRESS = "localhost";
	static private int SERVER_PORT = 1024;

	// Creaci�n del socket con el que se llevar� a cabo la comunicaci�n con el servidor.
	static private Socket socketAlServidor = null;

   static private boolean conectarServidor(int maxIntentos){
		//hasta maxIntentos intentos de conexi�n, para darle tiempo al servidor a arrancar
		boolean exito = false;     //�hay servidor?
		int van = 0;

		while((van<maxIntentos) && !exito){
			try {
				socketAlServidor = new Socket(SERVER_ADDRESS, SERVER_PORT);
				exito = true;
			} catch (Exception e) {
				van++;
				System.err.println("Failures:" + van);
				try {    //esperar 1 seg
    				Thread.sleep(1000);
				} catch (InterruptedException e2) {
    				e2.printStackTrace();
				}
			}
		}
		return exito;
		}


	public static void main(String[] args) {

		SERVER_ADDRESS = args[0];

		SERVER_PORT = Integer.parseInt(args[1]);

		boolean exito;                //�conectado?

		exito = conectarServidor(10); //10 intentos

		if(!exito){
			System.err.println("Don't know about host:" + SERVER_ADDRESS);
			System.exit(1);           //abortar si hay problemas
		}

		// ya hay conex��n
		// Inicializar los flujos de datos del socket para la comunicaci�n con el servidor

		PrintWriter canalSalidaAlServidor = null;
		BufferedReader canalEntradaDelServidor = null;
		try {
			canalSalidaAlServidor = new PrintWriter(socketAlServidor.getOutputStream(),true);
			canalEntradaDelServidor = new BufferedReader(new InputStreamReader(socketAlServidor.getInputStream()));
		} catch (IOException e) {      //abortar si hay problemas
			System.err.println("I/O problem:" + SERVER_ADDRESS);
			System.exit(1);
		}
		// Un buffer de entrada para leer de la entrada standard.
		BufferedReader entradaStandard = new BufferedReader(new InputStreamReader(System.in));
		String userInput = "";

		// Protocolo de comunicaci�n con el Servidor.
		// Mientras no se reciba la secuencia "END OF SERVICE"
		// el servidor contar� las vocales que aparecen en las frases
		// que le env�ar� el cliente.
		// El cliente obtiene las frases
		// que le pasa al servidor del usuario que lo est� ejecutando.
		try{
			while (!(userInput.equals("END OF SERVICE"))) {
				System.out.print("Text: ");
				userInput = entradaStandard.readLine();
				if (userInput != null) {
					canalSalidaAlServidor.println(userInput);
					String respuesta = canalEntradaDelServidor.readLine();
					if (respuesta != null) {
						System.out.println("Server answer: " + respuesta);
					} else {
						System.out.println("Comm. is closed!");
					}
				} else {
					System.err.println("Wrong input!");
				}
			}

			// Al cerrar cualquiera de los canales de comunicaci�n utilizados por un socket,�ste se cierra.
			// cerrar el canal de entrada.
			canalEntradaDelServidor.close();

			// cerrar el Socket de comunicaci�n con el servidor.
			socketAlServidor.close();
		} catch (Exception e){
			System.err.println(e);
		}

	}
}