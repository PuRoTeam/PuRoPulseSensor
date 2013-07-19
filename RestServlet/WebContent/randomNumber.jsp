<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.Math"%>
<%@ page import="org.json.JSONObject"%>

<%
	ServletContext context = getServletContext();
	Double point_var = (Double)context.getAttribute("Random");

	int max = 100;
	double random = Math.random()*max;
	JSONObject jObject = new JSONObject().put("random", point_var);
	out.println(jObject.toString());
%>
