<%@ page language="java" import="java.sql.*" errorPage="" %>
<%

    Connection conn = null;
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database","root", "");
    
    ResultSet rsdoLogin = null;
    PreparedStatement psdoLogin=null;
    
    String sUserID=request.getParameter("sUserName");
    String sPassword=request.getParameter("sPwd");
    String message="User login successfully ";
    
    try{
    String sqlOption="SELECT * FROM usermaster where"
                    +" sUserID=? and sPassword=password(?) and sStatus='A'";
    
    psdoLogin=conn.prepareStatement(sqlOption);
    psdoLogin.setString(1,sUserID);
    psdoLogin.setString(2,sPassword);
    
    rsdoLogin=psdoLogin.executeQuery();
    
    if(rsdoLogin.next())
    {
      String sUserName=rsdoLogin.getString("sFirstName")+" "+rsdoLogin.getString("sLastName");
     
      session.setAttribute("sUserID",rsdoLogin.getString("sUserID"));
      session.setAttribute("iUserType",rsdoLogin.getString("iUserType"));
      session.setAttribute("iUserLevel",rsdoLogin.getString("iUserLevel"));
      session.setAttribute("sUserName",sUserName);
     
      response.sendRedirect("success.jsp?error="+message);
    }
    else
    {
      message="No user or password matched" ;
      response.sendRedirect("login.jsp?error="+message);
    }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    
    
    /// close object and connection
    try{
         if(psdoLogin!=null){
             psdoLogin.close();
         }
         if(rsdoLogin!=null){
             rsdoLogin.close();
         }
         
         if(conn!=null){
          conn.close();
         }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }

%>
