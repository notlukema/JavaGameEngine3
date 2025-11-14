
package jge3.common.networking;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPAddress {
	
	private InetAddress address;
	
	public IPAddress(String name) {
		try {
			address = InetAddress.getByName(name);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			address = null;
		}
	}
	
	public IPAddress(int p1, int p2, int p3, int p4) {
		try {
			address = InetAddress.getByAddress(new byte[] {(byte)p1, (byte)p2, (byte)p3, (byte)p4});
		} catch (UnknownHostException e) {
			e.printStackTrace();
			address = null;
		}
	}
	
	public IPAddress(InetAddress address) {
		this.address = address;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public String getAddressString() {
		return address.getHostName();
	}
	
	public byte[] getAddressByte() {
		return address.getAddress();
	}
	
}
