<%-- 
    Document   : verSesion
    Created on : 21/05/2019, 08:31:27 AM
    Author     : ed000
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sesión</title>
    </head>
    <body>
        <%
            HttpSession httpSesion = request.getSession();
            out.println("ID de sesión: "+httpSesion.getId()+"<br />");
            DateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
            String creada = formato.format(httpSesion.getCreationTime());
            out.println("Sesión creada: "+creada+"<br />");
             String ultimoAcceso = formato.format(httpSesion.getLastAccessedTime());
            out.println("Último acceso de la sesión: "+ultimoAcceso+"<br />");
            out.println("Solicitud de servidor: "+request.getServerName()+"<br />");
            out.println("Instancia de servidor: "+System.getProperty("com.sun.aas.instanceName")+"("+java.net.InetAddress.getLocalHost().getHostName()+")" +"<br />");
        %>
    </body>
</html>
