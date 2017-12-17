

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Client {
	static JFrame frame = new JFrame();
	static JFrame frame2 = new JFrame("注册");
	static String id;
	static String password;
	static String msg=null;
	static String ki = "kimna";
	static String ko = "kiman";
	static int t;
	//static int log=0;
	
	static JTextArea text3 = new JTextArea();
	static JTextArea text4 = new JTextArea();
	//static JTextField text6 = new JTextField();
	
	public static String getMsgFromClient() {
		if(null != msg) {
			return msg;
		}
		return null;
	}
	
	public static void texton(String msg){
		text3.append(msg+"\n");
	}
	
	public static void useron(String msg){
		//text4.setText("");
		text4.append(msg+"\n");
	}
	
	public static void usersclear(){
		text4.setText("");
	}
	
	
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		// TODO Auto-generated method stub
		Socket client = new Socket("localhost",5678);
		//System.out.println("the id:");
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//String id = br.readLine();
		//if(id != null && id.length()<6 && id.length()>1){
			/*
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());
			DataInputStream dis = new DataInputStream(client.getInputStream());
			*/
		final String userName = "a";
		final String passwrod = "s";
		
		
		//JFrame jFrame = new JFrame("登陆界面");
		frame.setBounds(700,400,600,500);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel label1 = new JLabel("用户名");
		label1.setBounds(200, 150, 100, 30);
		frame.add(label1);
		
		JLabel label2 = new JLabel("密码");
		label2.setBounds(200, 180, 100, 30);
		frame.add(label2);
		
		final JTextField text1 = new JTextField();
		text1.setBounds(260,155,130,20);
		frame.add(text1);
		
		final JPasswordField text2 = new JPasswordField();
		text2.setBounds(260,185,130,20);
		frame.add(text2);
		
		//text6.setBounds(20,20,20,20);
		//frame.add(text6);
		
		JButton button = new JButton("登录");
		button.setBounds(305,250,130,40);
		
		JButton button2 = new JButton("注册");
		button2.setBounds(165,250,130,40);
		
		frame.add(button);
		frame.add(button2);
		
		frame.setVisible(true);
		
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				id = text1.getText();
				password = text2.getText();
				System.out.println(id+"客户端输入");
				System.out.println(password+"客户端输入");
				
				Send sclient = new Send(client,id,password);
				logreceive lr = new logreceive();
				
				if(lr.logreceive(client)==1){
					JOptionPane.showMessageDialog(null, "登录成功", "提示", JOptionPane.INFORMATION_MESSAGE);
					
					new Thread(new Receive(client)).start();
					
					closeThis();
					
					JMenuBar jb;
			        JFrame frame1 = new JFrame("chatroom");
			        frame1.setBounds(600,300,800,600);
					frame1.setResizable(false);
					frame1.setLayout(null);
					frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					jb = new JMenuBar();
					frame1.setJMenuBar(jb);
					
					JLabel label3 = new JLabel("聊天消息");
					label3.setBounds(300, 0, 100, 30);
					frame1.add(label3);
					
					JLabel label4 = new JLabel("在线用户");
					label4.setBounds(20,0,100,30);
					frame1.add(label4);
					
					text3.setSize(470,430);
					frame1.add(text3);
					JScrollPane jsp1 = new JScrollPane(text3);
					jsp1.setBounds(300,25,470,430);
					text3.setLayout(new BorderLayout());
					frame1.add(jsp1);
					
					text3.setEditable(false);
					
					text4.setBounds(20,25,220,520);
					frame1.add(text4);
					text4.setEditable(false);
					
					JTextField text5 = new JTextField();
					text5.setBounds(300,460,400,80);
					frame1.add(text5);
					text3.setEditable(false);
					
					JButton button3 = new JButton("发送");
					button3.setBounds(700,460,80,80);
					
					frame1.add(button3);
					
					frame1.setVisible(true);
					
					//Send sclient = new Send(client,id,password);
					
					
					button3.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e)
						{
								
								msg = text5.getText();
								
								System.out.println(msg+"文本框输入");
								text3.append(msg+"\n");
								text5.setText("");
								
								sclient.send(msg);
							//text3.setText(msg+"\n");
							//System.out.println(msg);
							
						}
					});
					
			    }
				else if(lr.logreceive(client)==3){
					JOptionPane.showMessageDialog(null, "该用户已登录", "提示", JOptionPane.ERROR_MESSAGE);
					closeThis();
				}
				else{
					if(text1.getText()==null||text2.getText()==null)
				    {
				    	JOptionPane.showMessageDialog(null, "the id and the password", "提示", JOptionPane.ERROR_MESSAGE);
				    	closeThis();
				    }
					else{
						JOptionPane.showMessageDialog(null, "错误", "提示", JOptionPane.ERROR_MESSAGE);
						text1.setText("");
						text2.setText("");
						closeThis();
					}
				}
			}
		});
		
		button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e){
				closeThis();
				
				
				frame2.setBounds(700,400,600,500);
				frame2.setResizable(false);
				frame2.setLayout(null);
				frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				JLabel label5 = new JLabel("用户名");
				label5.setBounds(200, 150, 100, 30);
				frame2.add(label5);
				
				JLabel label6 = new JLabel("密码");
				label6.setBounds(200, 180, 100, 30);
				frame2.add(label6);
				
				final JTextField text1 = new JTextField();
				text1.setBounds(260,155,130,20);
				frame2.add(text1);
				
				final JPasswordField text2 = new JPasswordField();
				text2.setBounds(260,185,130,20);
				frame2.add(text2);
				
				JButton button4 = new JButton("注册账号");
				button4.setBounds(235,250,150,40);
				
				frame2.add(button4);
				
				frame2.setVisible(true);
				
				button4.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						id = text1.getText();
						password = text2.getText();
						System.out.println(id+"客户端输入");
						System.out.println(password+"客户端输入");
						
						/*
						if(id == null || id.equals("") || password == null || password.equals(""))
						{
							JOptionPane.showMessageDialog(null, "用户名和密码均不能为空", "提示", JOptionPane.ERROR_MESSAGE);
							closeThre();
							return ;
						}
						else
						{
							regist regi = new regist(client,id,password);
							regreceive regr = new regreceive();
							
							if(regr.regreceive(client)==1)
							{
								JOptionPane.showMessageDialog(null, "注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);
								closeThre();
							}
							else
							{
								JOptionPane.showMessageDialog(null, "用户名已存在", "提示", JOptionPane.ERROR_MESSAGE);
									closeThre();
							}
						}
						*/
						
						if(id != null && !id.equals("") && password != null && !password.equals(""))
						{
							regist regi = new regist(client,id,password);
							regreceive regr = new regreceive();
							
							if(regr.regreceive(client)==1)
							{
								JOptionPane.showMessageDialog(null, "注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);
								closeThre();
							}
							else
							{
								JOptionPane.showMessageDialog(null, "用户名已存在", "提示", JOptionPane.ERROR_MESSAGE);
									closeThre();
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "用户名或密码均不能为空", "提示", JOptionPane.ERROR_MESSAGE);
							id = "asd";
							password = "d";
							regist regi = new regist(client,id,password);
							regreceive regr = new regreceive();
							closeThre();
						}
						
						
					}
				});
			}
		});
	}
	
	public static void closeThis(){
		frame.dispose();
	}
	
	public static void closeThre(){
		frame2.dispose();
	}
	
	public String getid(){
		return id;
	}
	
	
	
}
