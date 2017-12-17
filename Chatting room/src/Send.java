

import java.net.*;

import java.io.*;

public class Send implements Runnable{
	private BufferedReader console; //����̨������
	private DataOutputStream dos;   //�ܵ������
	private boolean isRunning = true; //�߳̿���
	private String id;
	private String password;
	private String msg;
	//��ʼ��
	
	public Send(){
		console = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public Send(Socket client,String id,String password){
		this();
		try{
			dos = new DataOutputStream(client.getOutputStream());
			this.id = id;
			this.password = password;
			send("@id:"+this.id+"#word;"+this.password);
			
			//send(getMsgFromClient());
		}catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dos,console);
		}
	}
	
	//����
	public String getMsgFromClient(){
		//Client c = new Client();
		msg= Client.getMsgFromClient();
		//System.out.println(msg+"sssssssssssssssssssssss");
		if(msg!=null){
			return msg;
		}
		return "û��˵��";
	}
	
	//����
	public void send(String msg){
		//String msg = getMsgFromConsole();
		if(msg != null && !msg.equals("")){
			try {
				dos.writeUTF(msg);
				dos.flush();
				System.out.println(msg+"����send�̷߳���");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				isRunning = false;
				CloseUtil.closeAll(dos,console);
			}
		}
	}
	
	public void run(){
		//�̴߳���
		while(isRunning){
			if(getMsgFromClient()!=null){
				send(getMsgFromClient());
				msg = null;
			}
		}
	}
}
