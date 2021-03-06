package jwxt;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Score extends JDialog implements ActionListener,WindowListener {
	private String sno;
	private Statement stm;
	public Score(Statement stm,Mainframe mainframe) throws SQLException{
		//super必须是构造函数第一行
		super(mainframe,true);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(true);
		menuBar.setFont(new Font("微软雅黑",Font.PLAIN,14));
		
		JMenu editMenu = new JMenu("帮助");
		menuBar.add(editMenu);
        this.setJMenuBar(menuBar);
		
		
		this.stm=stm;
		String[] col = { "课程名称", "教师姓名", "学分","学年度","学期","课程类别","总评成绩","绩点","教学班排名" };
		DefaultTableModel mm = new DefaultTableModel(col, 0);
		String sql = "select * from score";
		ResultSet rs = stm.executeQuery(sql);
		while(rs.next()){
			String s1=rs.getString(1);
			String s2=rs.getString(2);
			String s3=rs.getString(3);
			String s4=rs.getString(4);
			String s5=rs.getString(5);
			String s6=rs.getString(6);
			String s7=rs.getString(7);
			String s8=rs.getString(8);
			String s9=rs.getString(9);
			String s10=rs.getString(10);
			sno=s10;
			String[] ss=new String[]{s1,s2,s3,s4,s5,s6,s7,s8,s9,s10};
			mm.addRow(ss);
		}
		
	    JTable table = new JTable();
	    table.setModel(mm);
	    
        //JScrollPane scrollpane = new JScrollPane(table);
        table.getTableHeader().setReorderingAllowed(false);//拒绝列的位置变化
        table.getTableHeader().setFont(new Font("微软雅黑",Font.PLAIN,14));//设置表头字体
        table.setColumnSelectionAllowed(true);//可以选中列
        table.setCellSelectionEnabled(true);//可以选中某个单元格
        //table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);//自动筛选功能
        table.setFont(new Font("微软雅黑",Font.PLAIN,14));//设置表格内容字体
        
        //添加表头
        JPanel panel1=new JPanel(new BorderLayout());
        panel1.add(table.getTableHeader(), BorderLayout.PAGE_START);
        panel1.add(table, BorderLayout.CENTER);
        
        //添加按钮
        JPanel panel2=new JPanel(new FlowLayout());
        JButton button1=new JButton("打印");
        JButton button2=new JButton("退出");
        button1.setFont(new Font("微软雅黑",Font.PLAIN,14));
        button2.setFont(new Font("微软雅黑",Font.PLAIN,14));
        button1.setToolTipText("导出成绩表到excel");
        button2.setToolTipText("清空成绩表并退出");
        button1.addActionListener(this);
        button2.addActionListener(this);
        panel2.add(button1);
        panel2.add(button2);
        
        //getContentPane().setLayout(new FlowLayout());
        getContentPane().add(panel1);
        getContentPane().add(panel2,BorderLayout.SOUTH);
        
    
        setLocationRelativeTo(null);
        pack();
        
        //改变标题栏的图标
      	Toolkit tk=Toolkit.getDefaultToolkit(); 
      	Image image=tk.getImage("image\\signin.png"); //注意image文件夹要和src同目录
      	setIconImage(image);
      	
      	//设置frame
        setTitle("成绩单");
        setResizable(false);
        setVisible(true);
        //添加窗口监听器
        this.addWindowListener(this);
                
  }
	
	
	public void actionPerformed(ActionEvent e){
		
		if(e.getActionCommand().equals("退出")){
			//清除数据库数据
			String sql="delete from score";
			try {
				stm.executeUpdate(sql);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//释放当前panel的资源，关闭panel
			dispose();
		}
		else {
			String sql="select * from score";
			try {
				ResultSet rs = stm.executeQuery(sql);
				toexcel(rs,"score.xls","score");
				Runtime.getRuntime().exec("cmd  /c  start  score.xls");  

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
		}
	}
	//添加打印成绩单功能
	public void toexcel(ResultSet rs,String xlsName,String sheetName) throws SQLException, IOException{
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0,sheetName);
		HSSFRow row= sheet.createRow((short)0);
		HSSFCell cell;
		ResultSetMetaData md=rs.getMetaData();
		int nColumn=md.getColumnCount();
		//写入各个字段的名称
		for(int i=1;i<=nColumn;i++)
		{
		cell=row.createCell(i-1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(md.getColumnLabel(i));
		}

		int iRow=1;
		//写入各条记录，每条记录对应Excel中的一行
		while(rs.next())
		{row= sheet.createRow((short)iRow);;
		for(int j=1;j<=nColumn;j++)
		{ 
		cell = row.createCell(j-1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(rs.getObject(j).toString());
		}
		iRow++;
		}
		FileOutputStream fOut = new FileOutputStream(xlsName);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();
		JOptionPane.showMessageDialog(null,"导出数据成功！");
	}
	
//-----

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
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
		JOptionPane.showMessageDialog(null,"已清空当前成绩表！");
		String sql="delete from score";
		try {
			stm.executeUpdate(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}

