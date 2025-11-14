
package jge3.common.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	private ServerSocket server;
	private List<Client> clients;

	private int port;
	private IPAddress address;
	
	private Thread clientAccepter;
	
	public Server() {
		server = null;
		clients = new ArrayList<Client>();
		
		port = 0;
		address = null;
	}
	
	public Server(int port) {
		server = null;
		clients = new ArrayList<Client>();
		
		this.port = port;
		address = null;
	}
	
	public void startAccepting() {
		createClientAccepter();
	}
	
	public void stopAccepting() {
		clientAccepter.interrupt();
	}
	
	private void createClientAccepter() {
		clientAccepter = new Thread(new Runnable() {
			@Override
			public void run() {
				while (server == null) {
				}
				
				try {
					System.out.println("accepting");
					server.accept();
				} catch (ClosedByInterruptException e) {
					System.out.println("stopped accepting");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		clientAccepter.start();
	}
	
	private void getData() {
		port = server.getLocalPort();
		address = new IPAddress(server.getInetAddress());
	}
	
	public boolean create() {
		try {
			server = new ServerSocket(port);
			getData();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public boolean create(int backlog) {
		try {
			server = new ServerSocket(port, backlog);
			getData();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public boolean create(int backlog, IPAddress address) {
		try {
			server = new ServerSocket(port, backlog, address.getAddress());
			getData();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public boolean isCreated() {
		return server != null;
	}
	
	public ServerSocket getServerSocket() {
		return server;
	}
	
	public int getPort() {
		return port;
	}
	
	public IPAddress getIPAddress() {
		return address;
	}
	
	public boolean changePort(int port) {
		if (server == null) {
			this.port = port;
		}
		return server == null;
	}
	
	public Client waitForClient() {
		return waitForClient(0);
	}
	
	public Client waitForClient(int timeOut) {
		try {
			server.setSoTimeout(timeOut);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			Socket client = server.accept();
			clients.add(new Client(client));
		} catch (SocketTimeoutException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Client();
	}
	
	public Client[] getClients() {
		Client[] clients2 = new Client[clients.size()];
		for (int i=0;i<clients2.length;i++) {
			clients2[i] = clients.get(i);
		}
		return clients2;
	}
	
}
