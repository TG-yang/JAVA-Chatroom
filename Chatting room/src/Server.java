

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
		System.out.println("服务器开始运行");
		while(true){
			Socket client = server.accept();
			System.out.println("接收到一个客户端连接");
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
			System.out.println("接收客户端的线程开始运行");
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
				System.out.println(t+"验证t1");
				String r =dis.readUTF();        //"@id:"+this.id+"#word;"+this.password
				System.out.println(r+"服务器接收到输入流");
				if(r.startsWith("@id:")&&r.indexOf("#word;")!=0){
					this.id = r.substring(r.indexOf(":")+1,r.indexOf("#"));
					this.password = r.substring(r.indexOf(";")+1);
					for(Mychannel c:all){
						System.out.println("zuichu"+c.id);
					}
					System.out.println("this"+this);
					for(String user:users){
						System.out.println(user+"判断");
						x++;
						if(user.equals(this.id)){
							send("@login:3");
							System.out.println("出现重复登录");
							//all.remove(this);
							n=1;
							/*
							for(Mychannel c:all){
								System.out.println("remove前"+c.id);
							}
							all.remove(this);
							for(Mychannel c:all){
								System.out.println("remove后"+c.id);
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
						System.out.println("remove前"+c);
					}
					
					
					for(Mychannel c:all){
						System.out.println("remove后"+c);
					}
					System.out.println(t+"验证t2");
					System.out.println("发送完3"+x+"and"+y);//
					if(x==y){
						login();
					}
					else{
						all.remove(this);
					}
					//System.out.println(t+"验证t2");
				}                             //"@gisid:"+this.id+"%ter#word;"+this.password
				else if(r.startsWith("@gisid:")&&r.indexOf("%ter#word;")!=0){
					this.id = r.substring(r.indexOf(":")+1,r.indexOf("%"));
					this.password = r.substring(r.indexOf(";")+1);
					register();
					System.out.println(t+"验证t3");
				}
				
				//send("@login:"+t);
				//System.out.println("@login:"+t);
				System.out.println(t+"验证t4");
				if(t==1){
					users.add(this.id);
					System.out.println(this.id+" 已登录");
					send("welcome");
					
					for(Mychannel c:all){
						System.out.println("remove前1"+c);
					}
					
					
					all.removeAll(rmlist);
					
					
					for(Mychannel c:all){
						System.out.println("remove后1"+c);
					}
					
					sendothers(this.id+" 已登录");
					for(String user:users){
						System.out.println(user);
					}
					
					
					for(Mychannel c:all){
						c.send("0@2users:");
						send("0@2users:");
					}
					
					for(String user:users){//当前客户端显示用户
						send("0@1users:"+user);
					}
					
					for(Mychannel i:all){
						for(String user:users){
							System.out.println("0@1users:"+user);
							i.send("0@1users:"+user);//之前用户端显示用户
						}
					}
					t=0;
				}
				else{
					send("登录出错");
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
			System.out.println(id+"服务器register接收到id");
			password = this.password;
			System.out.println(password+"服务器register接收到password");
			//连接数据库
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://localhost:3306/test1";
				String username = "root";
				String databasepass = "12315";
				Connection conn = DriverManager.getConnection(url,username,databasepass);
				Statement stmtt = conn.createStatement();
				
				ResultSet resultt = stmtt.executeQuery("select * from idtest1");
				
				while(resultt.next()){//判断是否还有下一行
					count++;
		            String ID = resultt.getString("ID");
		            if(ID.equals(id))
		            {
		            	System.out.println("已存在用户名");
		            	break;
		            }
		            else
		            {
		            	System.out.println("用户名可注册");
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
			System.out.println(id+"服务器login接收到id");
			password = this.password;
			System.out.println(password+"服务器login接收到password");
			//连接数据库
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://localhost:3306/test1";
				String username = "root";
				String databasepass = "12315";
				Connection conn = DriverManager.getConnection(url,username,databasepass);
				Statement stmt = conn.createStatement();
				
				ResultSet result = stmt.executeQuery("select * from idtest1");
				
				while(result.next()){//判断是否还有下一行
					count++;
		            String ID = result.getString("ID");
		            String PASSWORD = result.getString("PASSWORD");
		            if(ID.equals(id)&&PASSWORD.equals(password))
		            {
		            	System.out.println("已查找到");
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
            		System.out.println("未查找到");
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
				System.out.println(this.id+"已登出");//服务器控制台输出
				
				//users.remove(this.id);
				/*
				for(String user:users){
					System.out.println(user+"users中的user记录");
				}
				*/
				
				String dcid = this.id;
				//all.remove(this);
				if(n==0){
					if(users.indexOf(this.id)!=-1)
					{
						for(Mychannel i:all)
						{   //需要由流关闭的用户this.id来调用sendothers发出登出信息
							if(i.id.equals(dcid))
							{
								i.sendothers(dcid+"已登出");//发送给客户端
							}
						}
						
						for(Mychannel c:all){
							System.out.println(c.id+"此时channel中1");
						}
						
						all.remove(this);
						for(Mychannel c:all){
							c.send("0@2users:");
							send("0@2users:");
						}
						
						for(Mychannel c:all){
							System.out.println(c.id+"此时channel中");
						}
						users.remove(this.id);
						for(Mychannel c:all){
							for(String user:users){
								System.out.println("0@1users:"+user);
								c.send("0@1users:"+user);
							}
						}
					}
				//users.remove(this.id);//移除自身表users中的信息
				all.remove(this); //移除自身集合
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
				//离开
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
				sendothers(receive()); //多client转发实现
				//senduseron();
			}
		}
		
	}
	
	
}
