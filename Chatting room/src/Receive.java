

import java.net.*;
import java.io.*;

public class Receive implements Runnable{
	private DataInputStream dis;   //输入流
	private boolean isRunning = true;  //线程标识
	static String m="0";
	//初始化
	public Receive(){
		
	}
	public Receive(Socket client){
		try{
			dis = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
	}
	//接收数据
	public String receive(){
		String msg = "";
		try{
			msg = dis.readUTF();
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
		return msg;
	}
	
	/*
	public int loginre(int z){
		String lg;
		try {
			lg = dis.readUTF();
			if(lg.startsWith("@login:"))
			{
				m= lg.substring(lg.indexOf(":")+1);
				
				System.out.println(m+"服务器发送给客户端login:后续信息");
				if(m.equals("1"))
				{
					System.out.println("yes");
					z = 1;
				}
				else{
					System.out.println("no");
					z = 0;
				}
			}
			} catch (IOException e) {
			
			//e.printStackTrace();
		}
		return 0;
	}
	*/

	public void run(){
		//线程体
		while(isRunning){
			//if(receive()!=null){
			String j;
			String t;
			j=receive();
			
			if(j.startsWith("0@1users:"))
			{
				String user = j.substring(j.indexOf("0@1")+9);
				Client.useron(user);
			}
			else if(j.equals("0@2users:"))
			{
				Client.usersclear();
			}
			else
			{
				Client.texton(j);
			}
			System.out.println(j+"客户端接受服务器消息");
			//}
		}
	}
	
	public static String gett(){
		return m;
	}
}
