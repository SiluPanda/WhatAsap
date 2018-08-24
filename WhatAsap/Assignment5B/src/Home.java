
import java.io.PrintWriter;
import java.sql.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Assignment5a
 */
@WebServlet("/Home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public Home() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
		PrintWriter out = response.getWriter();
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//doGet(request, response);
		try (
			    Connection conn = DriverManager.getConnection(
			    		"jdbc:postgresql://localhost:5390/postgres", "deepak", "");
			    Statement stmt = conn.createStatement();
			){
			
			PrintWriter out = response.getWriter();
			out.println("<html> <center><h1> Welcome to WhatAsap</h1> </center></html>");
			out.println("<html><center> Designed by: Silu (150050024), Deepak (150050039) <br><br> <h3> Login here to start a chat :) </h3></center></html>");
			out.println("<html><center><form action=\"Home \" method=\"post\"> Enter your name: <input type=\"text\" name = \"name\"> <br> Enter your password: <input type=\"password\" name = \"password\"> <br> <input type=\"submit\" value = \"Submit\"> </form> </center></html>");
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			
			PreparedStatement ps = conn.prepareStatement("select password from password where id = ?");
			ps.setString(1,name);
			ResultSet rset = ps.executeQuery();
			String pass_original = "";
			while(rset.next()) {
				 pass_original = rset.getString(1);
				 //out.println("loop running");
			}
			if(password.isEmpty() || name.isEmpty()) {
				out.println("ID or password can not be blank");
			}
			else {
			if(password.equals(pass_original)) {
				out.println("Congrats, You are logged in!");
				HttpSession session =request.getSession();
				
				session.setAttribute("uid", name);
				session.setAttribute("phone", password);
				response.sendRedirect("createConversation");
			}
			else {
				out.println("Sorry, try again");
			}
			}
			
			
			
		}
		catch (Exception sqle)
		{
		System.out.println("Exception : " + sqle);
		}
		
	}

}
