
package jge3.test;

import jge3.common.networking.Client;
import jge3.common.networking.IPAddress;
import jge3.common.networking.Server;

public class NetworkTest extends Thread {
	
	public static void main(String[] args) {
		
		Server server = new Server();
		if (!server.create()) {
			throw new RuntimeException("Couldn't create server");
		}
		
		NetworkTest test = new NetworkTest(server.getIPAddress(), server.getPort());
		test.start();
		
		server.waitForClient();
		
		while(true) {
			Client[] clients = server.getClients();
			
			for (int i=0;i<clients.length;i++) {
				String[] data = clients[i].getInputData();
				for (String str : data) {
					System.out.println(str);
				}
			}
		}
	}
	
	private IPAddress address;
	private int port;
	
	public NetworkTest(IPAddress address, int port) {
		System.out.println(address.getAddressString()+" "+port);
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void run() {
		Client client = new Client();
		client.connect(address, port);
		
		client.sendData("asdf");
	}
	
}
