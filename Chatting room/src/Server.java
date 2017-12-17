

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class Server {
	private List<Mychannel> all = new ArrayList<Mychannel>();
	private List<String> users = new ArrayList<String>();
	private List<Mychannel> rmlist = new ArrayList<Mychannel>();
	private int t;
	static int n=0;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*
		ServerSocket server = new ServerSocket(5678);
		while(true){
			Socket client = server.accept();
			
			DataInputStream dis = new DataInputStream(client.getInputStream());
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());
			
			while(true){
				String msg = dis.readUTF();
				System.out.println(msg);
				
				dos.writeUTF("fuwuqi>"+msg);
				dos.flush();
			}
			
			Mychannel channel = new Mychannel(client);
			new Thread(channel).start();
		}
		*/
		new Server().start();
	}
	public void start() throws IOException{
		ServerSocket server = new ServerSocket(5678);
		System.out.println("��������ʼ����");
		while(true){
			Socket client = server.accept();
			System.out.println("���յ�һ���ͻ�������");
			/*
			DataInputStream dis = new DataInputStream(client.getInputStream());
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());
			
			while(true){
				String msg = dis.readUTF();
				System.out.println(msg);
				
				dos.writeUTF("fuwuqi>"+msg);
				dos.flush();
			}
			*/
			
			Mychannel channel = new Mychannel(client);
			all.add(channel);
			new Thread(channel).start();
			System.out.println("���տͻ��˵��߳̿�ʼ����");
			/*
			for(Mychannel other:all){
				System.out.println(other);
			}
			*/
			
		}
	}
	
	class Mychannel implements Runnable{
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isRunning = true;
		private String id;
		private String password;
		private int x;
		private int y;
		public Mychannel (Socket client){
			try {
				dis = new DataInputStream(client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());
				System.out.println(t+"��֤t1");
				String r =dis.readUTF();        //"@id:"+this.id+"#word;"+this.password
				System.out.println(r+"���������յ�������");
				if(r.startsWith("@id:")&&r.indexOf("#word;")!=0){
					this.id = r.substring(r.indexOf(":")+1,r.indexOf("#"));
					this.password = r.substring(r.indexOf(";")+1);
					for(Mychannel c:all){
						System.out.println("zuichu"+c.id);
					}
					System.out.println("this"+this);
					for(String user:users){
						System.out.println(user+"�ж�");
						x++;
						if(user.equals(this.id)){
							send("@login:3");
							System.out.println("�����ظ���¼");
							//all.remove(this);
							n=1;
							/*
							for(Mychannel c:all){
								System.out.println("removeǰ"+c.id);
							}
							all.remove(this);
							for(Mychannel c:all){
								System.out.println("remove��"+c.id);
							}
							*/
							System.out.println(user+"1");
							System.out.println(this.id+"2");
							rmlist.add(this);
							t=0;
						}
						else{
							y++;
						}
						t=0;
					}
					for(Mychannel c:rmlist){
						System.out.println("rmlist"+c);
					}
					for(Mychannel c:all){
						System.out.println("removeǰ"+c);
					}
					
					
					for(Mychannel c:all){
						System.out.println("remove��"+c);
					}
					System.out.println(t+"��֤t2");
					System.out.println("������3"+x+"and"+y);//
					if(x==y){
						login();
					}
					else{
						all.remove(this);
					}
					//System.out.println(t+"��֤t2");
				}                             //"@gisid:"+this.id+"%ter#word;"+this.password
				else if(r.startsWith("@gisid:")&&r.indexOf("%ter#word;")!=0){
					this.id = r.substring(r.indexOf(":")+1,r.indexOf("%"));
					this.password = r.substring(r.indexOf(";")+1);
					register();
					System.out.println(t+"��֤t3");
				}
				
				//send("@login:"+t);
				//System.out.println("@login:"+t);
				System.out.println(t+"��֤t4");
				if(t==1){
					users.add(this.id);
					System.out.println(this.id+" �ѵ�¼");
					send("welcome");
					
					for(Mychannel c:all){
						System.out.println("removeǰ1"+c);
					}
					
					
					all.removeAll(rmlist);
					
					
					for(Mychannel c:all){
						System.out.println("remove��1"+c);
					}
					
					sendothers(this.id+" �ѵ�¼");
					for(String user:users){
						System.out.println(user);
					}
					
					
					for(Mychannel c:all){
						c.send("0@2users:");
						send("0@2users:");
					}
					
					for(String user:users){//��ǰ�ͻ�����ʾ�û�
						send("0@1users:"+user);
					}
					
					for(Mychannel i:all){
						for(String user:users){
							System.out.println("0@1users:"+user);
							i.send("0@1users:"+user);//֮ǰ�û�����ʾ�û�
						}
					}
					t=0;
				}
				else{
					send("��¼����");
					t=0;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				CloseUtil.closeAll(dis,dos);
				isRunning = false;
			}
			
		}
		
		private void register(){
			String id;
			String password;
			int noid = 0;
			int count = 0;
			id = this.id;
			System.out.println(id+"������register���յ�id");
			password = this.password;
			System.out.println(password+"������register���յ�password");
			//�������ݿ�
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://localhost:3306/test1";
				String username = "root";
				String databasepass = "12315";
				Connection conn = DriverManager.getConnection(url,username,databasepass);
				Statement stmtt = conn.createStatement();
				
				ResultSet resultt = stmtt.executeQuery("select * from idtest1");
				
				while(resultt.next()){//�ж��Ƿ�����һ��
					count++;
		            String ID = resultt.getString("ID");
		            if(ID.equals(id))
		            {
		            	System.out.println("�Ѵ����û���");
		            	break;
		            }
		            else
		            {
		            	System.out.println("�û�����ע��");
		            	noid++;
		            }
				}
				if(noid==count){
					String regist = "insert into idtest1 (ID,PASSWORD) values ('"+id+"','"+password+"')";
					stmtt.executeUpdate(regist);
					send("@register:1");
					all.remove(this);
				}
				else{
					send("@register:0");
					all.remove(this);
				}
				t=0;
				
			} catch (SQLException e) {
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}
		}
		
		private void login(){
			boolean flag = false;
			String id;
			String password;
			int nofound = 0;
			int count = 0;
			id = this.id;
			System.out.println(id+"������login���յ�id");
			password = this.password;
			System.out.println(password+"������login���յ�password");
			//�������ݿ�
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://localhost:3306/test1";
				String username = "root";
				String databasepass = "12315";
				Connection conn = DriverManager.getConnection(url,username,databasepass);
				Statement stmt = conn.createStatement();
				
				ResultSet result = stmt.executeQuery("select * from idtest1");
				
				while(result.next()){//�ж��Ƿ�����һ��
					count++;
		            String ID = result.getString("ID");
		            String PASSWORD = result.getString("PASSWORD");
		            if(ID.equals(id)&&PASSWORD.equals(password))
		            {
		            	System.out.println("�Ѳ��ҵ�");
		            	t=1;
		            	send("@login:1");
		            	break;
		            }
		            else
		            {
		            	nofound++;
		            }
		        }
				if(nofound == count){
            		System.out.println("δ���ҵ�");
            		send("@login:0");
            		t=0;
            	}
				
			} catch (SQLException e) {
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}
			
		}
		
		
		private String receive(){
			String msg = "";
			try {
				msg = dis.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				CloseUtil.closeAll(dis,dos);
				isRunning = false;
				System.out.println(this.id+"�ѵǳ�");//����������̨���
				
				//users.remove(this.id);
				/*
				for(String user:users){
					System.out.println(user+"users�е�user��¼");
				}
				*/
				
				String dcid = this.id;
				//all.remove(this);
				if(n==0){
					if(users.indexOf(this.id)!=-1)
					{
						for(Mychannel i:all)
						{   //��Ҫ�����رյ��û�this.id������sendothers�����ǳ���Ϣ
							if(i.id.equals(dcid))
							{
								i.sendothers(dcid+"�ѵǳ�");//���͸��ͻ���
							}
						}
						
						for(Mychannel c:all){
							System.out.println(c.id+"��ʱchannel��1");
						}
						
						all.remove(this);
						for(Mychannel c:all){
							c.send("0@2users:");
							send("0@2users:");
						}
						
						for(Mychannel c:all){
							System.out.println(c.id+"��ʱchannel��");
						}
						users.remove(this.id);
						for(Mychannel c:all){
							for(String user:users){
								System.out.println("0@1users:"+user);
								c.send("0@1users:"+user);
							}
						}
					}
				//users.remove(this.id);//�Ƴ������users�е���Ϣ
				all.remove(this); //�Ƴ�������
				//users.remove(this.id);
				
			}
			n=0;	
			}
			return msg;
			
		}
		
		private void send(String msg){
			if(msg == null || "".equals(msg)){
				return ;
			}
			try {
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				CloseUtil.closeAll(dos);
				isRunning = false;
				all.remove(this);
				//�뿪
			}
			
		}
		
		private void sendothers(String msg){
			//String msg = receive();
			//if(!msg.startsWith(this.id)){
			if(msg != null && !msg.equals("")){
				System.out.println(this.id+":"+msg);
				if(msg.startsWith("@") && msg.indexOf(":") != 0 ){
					String id = msg.substring(1,msg.indexOf(":"));
					String word = msg.substring(msg.indexOf(":")+1);
					for(Mychannel other:all){
						if(other.id.equals(id))
						{
							other.send(this.id+"@you:"+word);
						}
					}
				}
				else{
					for(Mychannel other:all){
						if(other == this){
							continue;
						}
						else{
							other.send(this.id+":"+msg);
						}
					}
				}
			}
		}
		
		
		//System.out.println(this.id+":"+dis.readUTF());
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isRunning){
				sendothers(receive()); //��clientת��ʵ��
				//senduseron();
			}
		}
		
	}
	
	
}
