

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class regreceive {
	private DataInputStream dis;
	String msg;
	String t;
	static int z;
	

	public int regreceive(Socket client){
	try {
		dis = new DataInputStream(client.getInputStream());
		msg = dis.readUTF();
		if(msg.startsWith("@register:"))
		{
			t= msg.substring(msg.indexOf(":")+1);
			
			System.out.println(t+"服务器发送给客户端login:后续信息");
			if(t.equals("1"))
			{
				System.out.println("yes");
				z=1;
			}
			else if(t.equals("0")){
				System.out.println("no");
				z=0;
			}
		}
		else{
			System.out.println(msg);
		}
		
		
		
		
	} catch (IOException e) {
		//e.printStackTrace();
		
	}
	return z;
	
	
	}
}
