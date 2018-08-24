
import java.io.PrintWriter;
import java.sql.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




@WebServlet("/ConversationDetails")
public class ConversationDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public ConversationDetails() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request,response);
		
	}
		
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//doGet(request, response);
PrintWriter out = response.getWriter();
		
		out.println("<html><center> <h3>Say something :) </h3> </center> </html>");
		out.println("<html><center><form action=\"ConversationDetails \" method=\"post\"> Message: <input type="
				+ "\"text\" name = \"message\"> <br>  <br> <input type=\"submit\" value = \"Send\"> </form> </center></html>");
		String message = request.getParameter("message");
		HttpSession session =request.getSession();
		if(session.isNew())
		{
			response.sendRedirect("Home");
		}
		
		String name_client = (String) session.getAttribute("name_client");
		String uid = (String) session.getAttribute("uid");
		String uid_client = (String) session.getAttribute("uid_client");
		try (
			    Connection conn = DriverManager.getConnection(
			    		"jdbc:postgresql://localhost:5390/postgres", "deepak", "");
			    Statement stmt = conn.createStatement();
			){
			
//				HttpSession session =request.getSession();
//				String name_client = (String) session.getAttribute("name_client");
//				String uid = (String) session.getAttribute("uid");
//				String uid_client = (String) session.getAttribute("uid_client");
				out.println("<html><center> <h1>You are talking to " + name_client + " </h1><center></html>");
				//out.println("<html><center> <h3>Say something :) </h3> </center> </html>");
				
//				out.println("<html><form action=\"Chat \" method=\"post\"> Message: <input type"
//						+ "=\"text\" name = \"message\"> <br>  <br> <input type=\"submit\" value = \"Send\"> </form> </html>");
//				String message = request.getParameter("message");
				//out.println(name);
				
//				int uid1=Integer.parseInt(uid);
//				int sender=Integer.parseInt(uid);
//				int uid2=Integer.parseInt(uid_client);
				//int receiver =
				
				String uid_dup = uid;
				String uid_client_dup = uid_client;
				
				if(uid.compareTo(uid_client)>0)
				{
				    String temp =uid_client;
					uid_client=uid;
					uid=temp;
					
				}
				
				
				PreparedStatement ps = conn.prepareStatement("select *  from conversations where uid1=? and uid2=?");
				ps.setString(1,uid);
				ps.setString(2,uid_client);
				ResultSet check=ps.executeQuery();
				
				if(check.next()==false)
				{
					
					PreparedStatement ps1 = conn.prepareStatement("insert into conversations(uid1,uid2) values (?,? )");
					ps1.setString(1,uid);
					ps1.setString(2,uid_client);
					ps1.executeQuery();
				}
				
				//out.println("<html>  <a href = \"http://localhost:8080/Assignment5B/Messages \"> View Messages </a> </html>");
				
				PreparedStatement ps2 = conn.prepareStatement("select thread_id from conversations where uid1=? and uid2=? ");
				ps2.setString(1,uid);
				ps2.setString(2,uid_client);
				ResultSet rs= ps2.executeQuery();
				
				int thread_id=0;
				//out.println("hello");
				while(rs.next())
				{
					 thread_id=rs.getInt(1);
					 
				}
				session.setAttribute("thread_id", thread_id);
				
				out.println("<html>  <a href = \"http://localhost:8080/Assignment5B/Messages \"> View Messages </a> </html>");
				
				out.println("<html> or <a href = \"http://localhost:8080/Assignment5B/Logout \"> Logout </a> </html>");
				
				
				
				
				
				
				if(message!=null && message.length()>0)
				{
					
					try(	
						PreparedStatement ps3 = conn.prepareStatement("insert into posts(uid,thread_id,timestamp,text) values (?,?,now(),? )"))
					{	
						ps3.setString(1,uid_dup);
						ps3.setInt(2,thread_id);
						ps3.setString(3,message);
						ps3.executeQuery();
					}
					
					catch (SQLException e) {
				        e.printStackTrace();
				    }
					
					
				}
				
				
				
				PreparedStatement ps4 = conn.prepareStatement("select * from posts where thread_id=? order by timestamp desc");
				ps4.setInt(1,thread_id );
				ResultSet display=ps4.executeQuery();
				String name = (String) session.getAttribute("name");
				
				
				//out.println(name);
				out.println("<table <tr> <th> Sent by </th> <th> Time </th> <th> Message </th>  </tr>");
				while(display.next())
				{
					if(display.getString(3).equals(uid_dup)) {
						out.println("<tr> <td>" + name + "</td>  <td>" + display.getString(4) 
						+ " </td> <td>" +display.getString(5)+  "</tr> ");
					}
					else {
						out.println("<tr> <td>" + name_client + "</td>  <td>" + display.getString(4) 
						+ " </td> <td>" +display.getString(5)+  "</tr> ");
					}
	
				}
		}
		catch (Exception sqle)
		{
			System.out.println("Exception : " + sqle);
		}
		
		
	}

}
