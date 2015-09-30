package com.test;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

@WebServlet("/DailyUsage")
public class DailyUsage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Connection con;
	int i=0;
	double cpuDaily=0;
	double ramDaily=0;
	double hddDaily=0;
       
    public DailyUsage() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		con=ConnectionUtil.getConnection();
		response.setContentType("text/html");
		
		try {
			PreparedStatement ps=con.prepareStatement("SELECT * FROM coll_usage WHERE date_of_usage='29-SEP-15'");
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				i++;
				cpuDaily+=rs.getDouble(1);
				ramDaily+=rs.getDouble(2);
				hddDaily+=rs.getDouble(3);
			}
			
			cpuDaily=cpuDaily/i;
			ramDaily=ramDaily/i;
			hddDaily=hddDaily/i;
			
			System.out.println(cpuDaily);
			System.out.println(ramDaily);
			System.out.println(hddDaily);
			
			rs.close();
			ps.close();
			
			ps=con.prepareStatement("INSERT INTO daily_usage VALUES(?,?,?,?,?)");
			
			ps.setDouble(1, cpuDaily);
			ps.setDouble(2, ramDaily);
			ps.setDouble(3, hddDaily);
			ps.setDouble(4, 1001);
			ps.setString(5, "29-SEP-15");
			
			ps.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
