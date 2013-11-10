<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="servlet.Shared"%>
<%@ include file="checkIfLogged.jsp" %>

<%
	String stato = request.getParameter("state");
	
	if(stato != null){
		boolean state = Boolean.parseBoolean(stato);		
		Shared singleton = Shared.getInstance();
		singleton.setDBWorking(state);
	}
%>
