<%@page import="com.test.ConnectionUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Highcharts Example</title>

		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
		<style type="text/css">
${demo.css}
		</style>
		<%
		Connection con;
		con=ConnectionUtil.getConnection();
		int i=0,j=0,k=0;
		
		PreparedStatement ps=con.prepareStatement("SELECT * FROM coll_usage WHERE user_id=?");
		ps.setInt(1, 1001);
		ResultSet rs=ps.executeQuery();%>
		
		<script type="text/javascript">
$(function () {
    $('#container').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: 'Time'
        },
        subtitle: {
            text: 'Source: WorldClimate.com'
        },
        xAxis: {
            categories: [
                         <%while(k<20){%>
                         <%=k %>,
                         <%} %>
                     
                         
                         ]
        },
        yAxis: {
            title: {
                text: 'CPU Usage'
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        series: [{
            name: 'System Value',
            data: [
					<%while(rs.next()){%>
					<%=rs.getDouble(1) %>,
					<%i++;} %>
					]
        }, {
            name: 'Ideal Value',
            data: [
                  <% while(j<i){%>
                  <%= 75%>,
                  <%j++;} %>
                  ]
                  }
        ]
    });
});
		</script>
	</head>
	<body>
<script src="../../js/highcharts.js"></script>
<script src="../../js/modules/exporting.js"></script>

<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>

	</body>
</html>
