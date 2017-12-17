

import java.net.*;
import java.io.*;

public class Receive implements Runnable{
	private DataInputStream dis;   //������
	private boolean isRunning = true;  //�̱߳�ʶ
	static String m="0";
	//��ʼ��
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
	//��������
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
				
				System.out.println(m+"���������͸��ͻ���login:������Ϣ");
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
		//�߳���
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
			System.out.println(j+"�ͻ��˽��ܷ�������Ϣ");
			//}
		}
	}
	
	public static String gett(){
		return m;
	}
}
