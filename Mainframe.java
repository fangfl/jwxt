package jwxt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

public class Mainframe extends JFrame implements ActionListener,WindowListener{
	private String score_year,score_term,schedule_year,schedule_term;
	private JPanel score,schedule;
	private JComboBox yearbox1,yearbox2;
	private JComboBox termbox1,termbox2;
	private JButton button1,button2;
	private SignIn signin;
	public Mainframe(String userid,SignIn signin){
		
		this.signin=signin;
		setTitle("�д����ϵͳ");
		setSize(300,260);
		setLocationRelativeTo(null);//��������
		setVisible(true);
		setResizable(false);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//���������ͼ��
		Toolkit tk=Toolkit.getDefaultToolkit(); 
		Image image=tk.getImage("image\\signin.png"); //ע��image�ļ���Ҫ��srcͬĿ¼
		setIconImage(image);
		
		//�����������ʽ
		Container maincontainer=getContentPane();
		maincontainer.setLayout(new FlowLayout());
		
		JPanel userinfo=new JPanel(new GridLayout(2,1,8,8));
		//Container usercontainer=userinfo.getContentPane();�˾��Ǵ�ģ���ΪJPanelû��getContentPane����
		JLabel server=new JLabel("��ǰʹ�õ�������������",SwingConstants.LEFT);
		server.setFont(new Font("΢���ź�",Font.PLAIN,14));
		JLabel username=new JLabel("ѧ�ţ�"+userid,SwingConstants.LEFT);
		username.setFont(new Font("΢���ź�",Font.PLAIN,14));
		userinfo.add(server);
		userinfo.add(username);
		userinfo.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
		userinfo.setPreferredSize(new Dimension(300, 50));
		maincontainer.add(userinfo);
		
		//������ǩ
		score=createpanel("�ɼ�",1);
		score.setFont(new Font("΢���ź�",Font.PLAIN,14));
		schedule=createpanel("�γ�",2);
		schedule.setFont(new Font("΢���ź�",Font.PLAIN,14));
		JTabbedPane stuinfo=new JTabbedPane();
		
		stuinfo.setFont(new Font("΢���ź�",Font.PLAIN,14));
		//���ñ�ǩ��ɫ
		stuinfo.setBackground(Color.LIGHT_GRAY);
		//Ϊ��ǩ���ͼ��
		
		stuinfo.addTab("��ѯ�ɼ�",score);
		stuinfo.addTab(" �γ̱�", schedule);
		stuinfo.setPreferredSize(new Dimension(300, 210));
		maincontainer.add(stuinfo);
		//���ñ���ͼƬ	
		ImageIcon  background = new ImageIcon("image\\sky1.jpg");
		JLabel label = new JLabel(background);
		getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));//
		label.setBounds(0,0,background.getIconWidth(), background.getIconHeight());//
		//����͸��
		((JPanel)maincontainer).setOpaque(false);
		userinfo.setOpaque(false);
		stuinfo.setOpaque(false);
	}
	public JPanel createpanel(String name,int num){
		JPanel panel=new JPanel();
		panel.setLayout(null);
		
		JPanel tempanel=new JPanel(new GridLayout(3,1,5,0));
		tempanel.setSize(250,130);
		tempanel.setLocation(25, 10);
		
		JPanel panel1=new JPanel();
		JLabel year=new JLabel("ѧ��");
		year.setFont(new Font("΢���ź�",Font.PLAIN,14));
		final String[] yearlist={"2011-2012","2012-2013","2013-2014"};
		if(num==1){
			yearbox1=new JComboBox(yearlist);
			yearbox1.setEditable(false);
			yearbox1.setFont(new Font("΢���ź�",Font.PLAIN,14));
			panel1.add(year);
			panel1.add(yearbox1);
		}
		else if(num==2){
			yearbox2=new JComboBox(yearlist);
			yearbox2.setEditable(false);
			yearbox2.setFont(new Font("΢���ź�",Font.PLAIN,14));
			panel1.add(year);
			panel1.add(yearbox2);
		}
						
		JPanel panel2=new JPanel();
		JLabel term=new JLabel("ѧ��");
		term.setFont(new Font("΢���ź�",Font.PLAIN,14));
		final String[] termlist={"  ��ѧ��     ","  ��ѧ��     ","  Сѧ��     "};
		//termlist.setFont(new Font("΢���ź�",Font.PLAIN,14));
		if(num==1){
			termbox1=new JComboBox(termlist);
			termbox1.setEditable(false);
			termbox1.setFont(new Font("΢���ź�",Font.PLAIN,14));
			panel2.add(term);
			panel2.add(termbox1);
		}
		else if(num==2){
			termbox2=new JComboBox(termlist);
			termbox2.setEditable(false);
			termbox2.setFont(new Font("΢���ź�",Font.PLAIN,14));
			panel2.add(term);
			panel2.add(termbox2);
		}
						
		JPanel panel3=new JPanel();
		if(num==1){
			button1=new JButton("��ѯ�ɼ�");
			button1.setFont(new Font("΢���ź�",Font.PLAIN,14));
			button1.addActionListener(this);
			panel3.add(button1);
		}
		else if(num==2){
			button2=new JButton("��ѯ�γ�");
			button2.setFont(new Font("΢���ź�",Font.PLAIN,14));
			button2.addActionListener(this);
			panel3.add(button2);
		}
		
		
		tempanel.add(panel1);
		tempanel.add(panel2);
		tempanel.add(panel3);
		
		panel.add(tempanel);
						
		panel1.setOpaque(false);
		panel2.setOpaque(false);
		panel3.setOpaque(false);
		tempanel.setOpaque(false);
		panel.setOpaque(false);
		return panel;
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==button1){
			score_year=yearbox1.getSelectedItem().toString();
			score_term=termbox1.getSelectedItem().toString();
			
			if(score_term.equals("  ��ѧ��     "))score_term="1";
			else if(score_term.equals("  ��ѧ��     "))score_term="2";
			else if(score_term.equals("  Сѧ��     "))score_term="3";
			//System.out.println(score_year);
			try {
				signin.getscore(score_year, score_term,this);
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getSource()==button2){
			schedule_year=yearbox2.getSelectedItem().toString();
			schedule_term=termbox2.getSelectedItem().toString();
			
			if(schedule_term.equals("  ��ѧ��     "))schedule_term="1";
			else if(schedule_term.equals("  ��ѧ��     "))schedule_term="2";
			else if(schedule_term.equals("  Сѧ��     "))schedule_term="3";
			//System.out.println(score_year);
			try {
				signin.schedule_post(schedule_year, schedule_term,this);
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	/*
	public static void main(String[] args){
		Mainframe mainframe=new Mainframe("11*****");
	}
	*/
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		JOptionPane.showMessageDialog(null,"������������и������ݣ�");
		System.exit(0);
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
