package jwxt;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;



import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;



public class SignInFrame extends JFrame implements ActionListener{
	private JLabel userLabel,passLabel;
	private JTextField userName;
	private JPasswordField userPassword;
	private JButton loginButton,quitButton;
	public SignIn signin;//ʮ����Ҫ����Ҫ��actionPerformed��������ʱ������Ϊ����Ҫһֱ�á�
	private String username;
	
	public SignInFrame() throws ClassNotFoundException, SQLException{
		
		setTitle("����ϵͳ");
		//�ı��������ͼ��
		Toolkit tk=Toolkit.getDefaultToolkit(); 
		Image image=tk.getImage("image\\signin.png"); //ע��image�ļ���Ҫ��srcͬĿ¼
		setIconImage(image);
		//�½�SignIn����
		signin=new SignIn();
		Container content=getContentPane();
		content.setLayout(new BorderLayout());
		//���ñ���ͼƬ	
		ImageIcon  background = new ImageIcon("image\\sky1.jpg");
	    JLabel label = new JLabel(background);
	    getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));//
	    label.setBounds(0,0,background.getIconWidth(), background.getIconHeight());//
	   
	    JPanel panel=new JPanel(new GridLayout(3,1,0,0));
	    
	    JPanel userPanel = new JPanel();  
	    userLabel = new JLabel("ѧ ��: ");
	    userName = new JTextField(16);
	    userPanel.add(userLabel);
	    userPanel.add(userName);
	      
	    JPanel passPanel = new JPanel();  
	    passLabel = new JLabel("�� ��: ");
	    userPassword = new JPasswordField(16);
	    passPanel.add(passLabel);
	    passPanel.add(userPassword);
	      
	      
	    JPanel buttonPanel = new JPanel();  
	    loginButton = new JButton("��¼");
	    loginButton.setContentAreaFilled(false);
	    loginButton.addActionListener(this);
	    buttonPanel.add(loginButton);
	      
	    quitButton = new JButton("�˳�");
	    quitButton.setContentAreaFilled(false);
	    quitButton.addActionListener(this);
	    buttonPanel.add(quitButton);
	   
	    panel.add(userPanel, 0);
	    panel.add(passPanel, 1);
	    panel.add(buttonPanel, 2);
	    content.add(panel);
	    //���ø�������Ϊ͸��
	    userPanel.setOpaque(false);
	    passPanel.setOpaque(false);
	    buttonPanel.setOpaque(false);
	    panel.setOpaque(false);
	    ((JPanel)content).setOpaque(false);
	      	   	   
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);//��¼���ھ���
		//��������
		userLabel.setFont(new Font("΢���ź�",Font.PLAIN,14));
		passLabel.setFont(new Font("΢���ź�",Font.PLAIN,14));
		loginButton.setFont(new Font("΢���ź�",Font.PLAIN,14));
		quitButton.setFont(new Font("΢���ź�",Font.PLAIN,14));
		
		setSize(250,160);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	public String getUsername(){
		return username;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand()=="��¼")//����getSouce,��Ϊ���loginButton�������ֻ�ǹ��캯���еľֲ�������������������
		{
			//System.out.println("login success");
			//String homepage="";
			username=userName.getText();
			String tempassword=String.valueOf(userPassword.getPassword());//getPassword()�᷵���ַ����飬�������룬��ʱ��Ҫ���ģ�����Ҫת��һ�¡�
			List<NameValuePair> formparams=new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("username",username));
			formparams.add(new BasicNameValuePair("password",tempassword));
			
			try {
				
				int statuscode1=signin.firstPost(formparams);
				if(statuscode1==302){
					Mainframe mainframe=new Mainframe(username,signin);
					setVisible(false);//�Զ����ص�¼���ڣ�ֻ��ʾ�´���
				}
				
				//if(homepage!="") System.out.println(homepage);
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if(e.getActionCommand()=="�˳�")
		{
			System.out.println("login failed");
			System.exit(0);
		}
	}
	
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException{
	    new SignInFrame();
	}
	
}
