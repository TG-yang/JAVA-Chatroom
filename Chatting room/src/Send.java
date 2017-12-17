

import java.net.*;

import java.io.*;

public class Send implements Runnable{
	private BufferedReader console; //控制台输入流
	private DataOutputStream dos;   //管道输出流
	private boolean isRunning = true; //线程控制
	private String id;
	private String password;
	private String msg;
	//初始化
	
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
	
	//输入
	public String getMsgFromClient(){
		//Client c = new Client();
		msg= Client.getMsgFromClient();
		//System.out.println(msg+"sssssssssssssssssssssss");
		if(msg!=null){
			return msg;
		}
		return "没人说话";
	}
	
	//发送
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
				isRunning = false;
				CloseUtil.closeAll(dos,console);
			}
		}
	}
	
	public void run(){
		//线程创建
		while(isRunning){
			if(getMsgFromClient()!=null){
				send(getMsgFromClient());
				msg = null;
			}
		}
	}
}
