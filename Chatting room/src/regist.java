

import java.net.*;
import java.io.*;

public class regist {
	private DataOutputStream dos;
	private String id;
	private String password;
	
	public regist(Socket client,String id,String password){
		try{
			dos = new DataOutputStream(client.getOutputStream());
			this.id = id;
			this.password = password;
			send("@gisid:"+this.id+"%ter#word;"+this.password);
			
			//send(getMsgFromClient());
		}catch (IOException e) {
			//e.printStackTrace();
			CloseUtil.closeAll(dos);
		}
	}
		
		public void send(String msg){
			//String msg = getMsgFromConsole();
			if(msg != null && !msg.equals("")){
				try {
					dos.writeUTF(msg);
					dos.flush();
					System.out.println(msg+"这是send线程发出");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					
					CloseUtil.closeAll(dos);
				}
			}
		}
}
