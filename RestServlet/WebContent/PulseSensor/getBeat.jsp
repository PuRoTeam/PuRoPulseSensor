<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.Math"%>
<%@ page import="it.uniroma2.pulsesensor.servlet.Shared"%>
<%@ page import="org.json.JSONObject"%>

<%			
	String selectedUid = request.getParameter("uid");

	Long uid = null;	
	
	try
	{		
		if(selectedUid != null)
			uid = Long.parseLong(selectedUid);
		
		Shared singleton = Shared.getInstance();
		//Integer BPM = new Integer((int)(Math.random()*100));
		Integer BPM = singleton.getBPM(uid);
		JSONObject json = new JSONObject();
		json.put("BPM", BPM);
		out.println(json.toString());
		//System.out.println("BPM: " + json.toString() + " uid: " + uid);
	}
	catch(NumberFormatException e)
	{
		out.println("{}"); //se un valore non valido, invio json vuota
		return;
	}
	

%>