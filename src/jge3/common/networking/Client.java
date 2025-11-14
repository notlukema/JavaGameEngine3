
package jge3.common.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
	
	private Socket client;
	
	private DataInputStream input;
	private DataOutputStream output;

	private Thread dataGetter;
	private boolean autoGetData;
	private List<String> dataIn;
	
	private int localPort;
	private int serverPort;
	private IPAddress address;
	
	public Client() {
		client = null;
		
		input = null;
		output = null;
		
		dataGetter = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (autoGetData && input != null && dataIn != null) {
						try {
							dataIn.add(input.readUTF());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		dataGetter.start();
		autoGetData = true;
		dataIn = new ArrayList<String>();
		
		localPort = 0;
		serverPort = 0;
		address = null;
	}
	
	public Client(IPAddress address, int port) {
		this();
		connect(address, port);
	}
	
	public Client(Socket socket) {
		this();
		client = socket;
		getData();
	}
	
	private void getData() {
		try {
			input = new DataInputStream(client.getInputStream());
			output = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		localPort = client.getLocalPort();
		serverPort = client.getPort();
		address = new IPAddress(client.getInetAddress());
	}
	
	public boolean connect(IPAddress address, int port) {
		try {
			client = new Socket(address.getAddress(), port);
			getData();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public boolean sendData(String data) {
		try {
			output.writeUTF(data);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public String[] getInputData() {
		return getInputData(true);
	}
	
	public String[] getInputData(boolean clear) {
		String[] data = new String[dataIn.size()];
		for (int i=0;i<data.length;i++) {
			data[i] = dataIn.get(i);
		}
		if (clear) {
			dataIn.clear();
		}
		return data;
	}
	
	public boolean isConnected() {
		return client != null;
	}
	
	public Socket getSocket() {
		return client;
	}
	
	public DataInputStream getInputStream() {
		return input;
	}
	
	public DataOutputStream getOutputStream() {
		return output;
	}
	
	public int getLocalPort() {
		return localPort;
	}
	
	public int getServerPort() {
		return serverPort;
	}
	
	public IPAddress getServerAddress() {
		return address;
	}
	
}
