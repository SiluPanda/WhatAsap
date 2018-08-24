

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Messages
 */
@WebServlet("/Messages")
public class Messages extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Messages() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		HttpSession session =request.getSession();
		if(session.isNew())
		{
			response.sendRedirect("Home");
		}
		int thread_id = (int) session.getAttribute("thread_id");
		try (
			    Connection conn = DriverManager.getConnection(
			    		"jdbc:postgresql://localhost:5390/postgres", "deepak", "");
			    Statement stmt = conn.createStatement();
			)
		{	
			PreparedStatement ps4 = conn.prepareStatement("select * from posts where thread_id=?");
			ps4.setInt(1,thread_id );
			ResultSet display=ps4.executeQuery();
			
			
			
			out.println("<table <tr> <th> Messenger </th> <th> Time </th> <th> Message </th>  </tr>");
			while(display.next())
			{
				out.println("<tr> <td>" + display.getString(3) + "</td>  <td>" + display.getString(4) + " </td> <td>" +display.getString(5)+  "</tr> ");
			}
		}
		
		catch (Exception sqle)
		{
		System.out.println("Exception : " + sqle);
		}
		}
		
		
		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
