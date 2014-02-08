package jwxt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignIn{
	public String sno;
	public String JSESSIONID;
	public String session;
	public final static String _ga="_ga=GA1.2.377681273.1386764424";
	public String url="jdbc:odbc:jwxt";
	public Connection conn;
	public Statement stm;
	public Properties prop;
	CloseableHttpClient httpclient = HttpClients.createDefault();
	
	public SignIn() throws ClassNotFoundException, SQLException{
		//初始化时连接数据库
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		
		prop = new Properties();//插入数据中有中文，则会是乱码且插入失败，此法解决。   
	    prop.put("charSet", "gbk");  
		conn=DriverManager.getConnection(url,prop);
		stm=conn.createStatement();
	}
	public void setsno(String sno){
		this.sno=sno;
	}
	public void setJSESSIONID(String jsessionid){
		this.JSESSIONID=jsessionid;
	}
	public void setsession(String session){
		this.session=session;
	}
	
	public int firstPost(List<NameValuePair> formparams) throws ClientProtocolException, IOException{
		String[] temstring=new String[3];//zhu yi
		String regex1=";",regex2="=";
		int i;
		HttpPost httppost=new HttpPost("http://insysu.com/sign_in");
		httppost.addHeader("Cookie", _ga);
		httppost.setEntity(new UrlEncodedFormEntity(formparams));
		CloseableHttpResponse response=httpclient.execute(httppost);
		int statuscode=response.getStatusLine().getStatusCode();
		System.out.println("First Post:"+response.getStatusLine().getStatusCode());
		HeaderIterator it=response.headerIterator("Set-Cookie");
		
		
		for(i=0;it.hasNext();i++){
			temstring[i]=it.next().toString();
			Pattern pattern1=Pattern.compile(regex1);
			String[] tem1=pattern1.split(temstring[i],2);
			String firsttem=tem1[0];
			Pattern pattern2=Pattern.compile(regex2);
			String[] tem2=pattern2.split(firsttem,2);
			temstring[i]=tem2[1];
						
		}
		this.sno=temstring[0];
		this.JSESSIONID=temstring[1];
		this.session=temstring[2];
		System.out.println("sno="+sno+";\njsessionid="+JSESSIONID+";\nsession="+session);
		response.close();
		return statuscode;
		
	}
	public int firstGet() throws ClientProtocolException, IOException{
		int statuscode;
		HttpGet httpget=new HttpGet("http://insysu.com/sign_in");
		String cookie="_ga="+_ga+";sno="+sno+";JSESSIONID="+JSESSIONID+";session="+session;
		httpget.addHeader("Cookie",cookie);
		CloseableHttpResponse response=httpclient.execute(httpget);
		statuscode=response.getStatusLine().getStatusCode();
		//System.out.println("First Get:"+response.getStatusLine().getStatusCode());
		//String html=buffertostring(response);
		//System.out.println(html);
		response.close();
		//httpclient.close();
		return statuscode;
	}
	private String buffertostring(CloseableHttpResponse response) throws IllegalStateException, IOException{
		HttpEntity entity=response.getEntity();
		InputStream instream=entity.getContent();
		//下面一行很关键，编码避免汉字为乱码，中文乱码会影响json解析
		InputStreamReader instreamreader=new InputStreamReader(instream,"utf-8");
		BufferedReader in=new BufferedReader(instreamreader);
		StringBuffer buffer=new StringBuffer();
		String line="";
		while((line=in.readLine())!=null){
			buffer.append(line);
		}
		return buffer.toString();
	}
	
	public  JSONObject getscorejson(String year,String term) throws ClientProtocolException, IOException, JSONException{
		
		HttpGet httpget=new HttpGet("http://insysu.com/score?year="+year+"&term="+term);
		String cookie="sno="+sno+";JSESSIONID="+JSESSIONID+";_ga="+_ga;
		httpget.addHeader("Cookie",cookie);
		CloseableHttpResponse response=httpclient.execute(httpget);
		String string=buffertostring(response);
		JSONObject json=new JSONObject(string);
		return json;		
		
	}
	/*
	 @kcmc 课程名称
	 @jsxm 教师姓名
	 @xf 学分
	 @xnd 学年
	 @xq 学期
	 @kclb 课程类别
	 @zpcj 总评成绩
	 @jd 绩点
	 @jxbpm 教学班排名
	 */
	public  void getscore(String year,String term,Mainframe mainframe) throws JSONException, ClientProtocolException, IOException, ClassNotFoundException, SQLException{
		JSONObject json=getscorejson(year,term);
		JSONArray jsonarray=json.getJSONObject("body").getJSONObject("dataStores").getJSONObject("kccjStore").getJSONObject("rowSet").getJSONArray("primary");
		System.out.println(jsonarray);
		String kcmc,jsxm,xf,xnd,xq,kclb,zpcj,jd,jxbpm;
		
		//conn=DriverManager.getConnection(url,prop);
		//stm=conn.createStatement();
	
		/*
		String url="jdbc:odbc:jwxt";
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Properties prop = new Properties();//插入数据中有中文，则会是乱码且插入失败，此法解决。   
	    prop.put("charSet", "gbk");  
		Connection conn=DriverManager.getConnection(url,prop);
		Statement stm=conn.createStatement();
		*/
		/*
		String sql="select * from score";
		ResultSet rs=stm.executeQuery(sql);
		while(rs.next())
		   {
			    String string=new String(rs.getBytes("kcmc"),"gbk");//取中文乱码解决
			    System.out.println(string);
		   }
		*/
		for (int i = 0; i < jsonarray.length(); i++) {  
            JSONObject everyJsonObject=jsonarray.getJSONObject(i);
                       
            kcmc=everyJsonObject.getString("kcmc"); 
            //教师姓名这一项有时不会返回，所以要处理这种异常
            try{
            	jsxm=everyJsonObject.getString("jsxm");
            }
            catch(Exception e){
            	jsxm="";
            }
            //当教师姓名为空时，不能对其进行toCharArray()操作，否则会导致数组越界异常。
            if(!jsxm.equals("")){
            	if(jsxm.toCharArray()[0]<59){jsxm="";}
                else if(jsxm.length()>3){
                	Pattern pattern1=Pattern.compile(",");
                    String[] tem1=pattern1.split(jsxm,2);
                    jsxm=tem1[0];//个别名字重复，利用正则取一个即可
                }
            }          
            
            
            xf=everyJsonObject.getString("xf");  
            xnd=everyJsonObject.getString("xnd");
            xq=everyJsonObject.getString("xq");
            
            kclb=everyJsonObject.getString("kclb");
            //注意，==为真返回0，左边大则返回1，右边大返回-1；所以用equals
            if(kclb.equals("10")) kclb="公必";
            else if(kclb.equals("11")) kclb="专必";
            else if(kclb.equals("21")) kclb="专选";
            else if(kclb.equals("30")) kclb="公选";
            
            zpcj=everyJsonObject.getString("zpcj");
            jd=everyJsonObject.getString("jd");
            jxbpm=everyJsonObject.getString("jxbpm");
            
            
            String sql="insert into score values('"+kcmc+"','"+jsxm+"','"+
            		xf+"','"+xnd+"','"+xq+"','"+kclb+"','"+zpcj+"','"+jd+"','"+jxbpm+"','11******')";
            stm.executeUpdate(sql);
        }
		//stm.close();
		//conn.close();
		//创建成绩单
		Score score=new Score(stm,mainframe);
		
	}
	
	public void schedule_post(String year,String term,Mainframe mainframe) throws ClientProtocolException, IOException, SQLException{
		HttpPost httppost=new HttpPost("http://insysu.com/course_schedule");
		String cookie="sno="+sno+";JSESSIONID="+JSESSIONID+";_ga="+_ga;
		httppost.addHeader("Cookie", cookie);
		List<NameValuePair> formparams=new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("class-year",year));
		formparams.add(new BasicNameValuePair("class-term",term));
		httppost.setEntity(new UrlEncodedFormEntity(formparams));
		CloseableHttpResponse response=httpclient.execute(httppost);
		//int statuscode=response.getStatusLine().getStatusCode();
		//System.out.println("schedule Post:"+response.getStatusLine().getStatusCode());
		String html=buffertostring(response);
		
		//利用正则取课程表数据,自然也就用不到matcher.groupcount()来计数了
		String regex1="jc='\\d{1,2}-\\d{1,2}节'.{44}kcmc='[\\u4e00-\\u9fa5]*';dd='[^;,]*';zfw='\\d{1,2}-\\d{1,2}'.{314}weekpos=\\d";
		System.out.println(regex1);
		Pattern pattern=Pattern.compile(regex1);
		Matcher matcher=pattern.matcher(html);
		
		//分批提取每一节课
		//jc 第几节课
		//kcmc 课程名称
		//dd 课室
		//zfw 教学周
		//weekpos 星期几
		String jc="",kcmc="",dd="",zfw="",weekpos="";
		String jc_regex="jc='\\d{1,2}-\\d{1,2}节'",
				kcmc_regex="kcmc='[\\u4e00-\\u9fa5]*'",
				zfw_regex="zfw='\\d{1,2}-\\d{1,2}'",
				weekpos_regex="weekpos=\\d",
				dd_regex="dd='[^;,]*'",
		        re_regex1="'",
		        re_regex2="=";
		Pattern jc_pattern=Pattern.compile(jc_regex),
				kcmc_pattern=Pattern.compile(kcmc_regex),
				zfw_pattern=Pattern.compile(zfw_regex),
				weekpos_pattern=Pattern.compile(weekpos_regex),
				dd_pattern=Pattern.compile(dd_regex),
		        re_pattern1=Pattern.compile(re_regex1),
		        re_pattern2=Pattern.compile(re_regex2);
		Matcher tem_matcher;
		//matcher.find()像游标，可以循环使用
		while(matcher.find()){
			//System.out.println(matcher.group());
		  
			String string=matcher.group();
			//System.out.println(string);
			tem_matcher=jc_pattern.matcher(string);
			if(tem_matcher.find()){
				jc=tem_matcher.group();
				String[] temstring=re_pattern1.split(jc);
				jc=temstring[1];
				
			}
				
			
			tem_matcher=kcmc_pattern.matcher(string);
			if(tem_matcher.find()){
				kcmc=tem_matcher.group();
				String[] temstring=re_pattern1.split(kcmc);
				kcmc=temstring[1];
			}
			
			tem_matcher=zfw_pattern.matcher(string);
			if(tem_matcher.find()){
				zfw=tem_matcher.group();
				String[] temstring=re_pattern1.split(zfw);
				zfw=temstring[1];
			}
			
			tem_matcher=dd_pattern.matcher(string);
			if(tem_matcher.find()){
				dd=tem_matcher.group();
				String[] temstring=re_pattern1.split(dd);
				dd=temstring[1];
			}
			
			tem_matcher=weekpos_pattern.matcher(string);
			if(tem_matcher.find()){
				weekpos=tem_matcher.group();
				String[] temstring=re_pattern2.split(weekpos);
				weekpos=temstring[1];
			}
			
			
			String sql="insert into schedule values('"+kcmc+"','"+dd+"','"+weekpos+"','"+jc+"','"+zfw+"','"+year+"','"+term+"')";
			//System.out.println(sql);
			stm.executeUpdate(sql);		
		}
		Schedule schedule=new Schedule(stm,mainframe);
	}
	
	/*
	public static void main(String args[]) throws ClientProtocolException, IOException, JSONException, ClassNotFoundException, SQLException{
		SignIn signin=new SignIn();
		
		List<NameValuePair> formparams=new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("username","11*****"));
		formparams.add(new BasicNameValuePair("password","*********"));
		signin.firstPost(formparams);
		//signin.firstGet();
		String year="2012-2013",term="1";
		//signin.getscore(year,term);
		signin.schedule_post(year, term);
	}
	*/
}

