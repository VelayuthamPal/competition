<%@page import="com.test.ConnectionUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% Connection con;
String cpu=null;
double ram=0;
double hdd=0;

con=ConnectionUtil.getConnection();


PreparedStatement ps=con.prepareStatement("SELECT * FROM user_details WHERE user_id=1001");
ResultSet rs=ps.executeQuery();
while(rs.next()){
	cpu=rs.getString(2);
	ram=rs.getDouble(3);
	hdd=rs.getDouble(4);
	
	break;
}
%>
<h4><b>CPU</b></h1>
<%
out.println(cpu);%>
<h4><b>RAM</b></h4>
<%
out.println(ram);%>GB
<h4><b>HDD</b></h4>
<%out.println(hdd); %>GB
</body>
</html>