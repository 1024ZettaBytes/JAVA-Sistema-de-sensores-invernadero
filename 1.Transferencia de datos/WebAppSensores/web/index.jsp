<%-- 
    Document   : agregarVideojuego
    Created on : 3/12/2018, 07:39:18 PM
    Author     : mario
--%>

<%@page import="objetos.Invernadero"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="Conexion.ConexionBD"%>
<%@page import="Interfaces.IConexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <script src="scripts/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="scripts/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
        <link href="css/Estilos.css" rel="stylesheet" type="text/css"/>
        <script src="https://apis.google.com/js/platform.js?onload=onLoad" async defer></script>
        <meta name="google-signin-client_id" content="260022130926-tqm36lc9g0ggv0dqa4sqbff21el22tdk.apps.googleusercontent.com">
        <title>Registrar sensor</title>
        <script>
            var ses = false;
            function onLoad() {
                gapi.load('auth2', function () {
                    /**
                     * Retrieve the singleton for the GoogleAuth library and set up the
                     * client.
                     */
                    auth2 = gapi.auth2.init({
                        client_id: '260022130926-tqm36lc9g0ggv0dqa4sqbff21el22tdk.apps.googleusercontent.com'
                    });

                    auth2.then(function () {
                        var isSignedIn = auth2.isSignedIn.get();
                        var currentUser = auth2.currentUser.get();
                        if (isSignedIn) {
                            console.log("SI");
                        } else {
                           window.location.replace("login.jsp");
                        }
                    });

                });
            }
             function signOut() {
                            auth2.signOut().then(function () {
                        window.location.replace("login.jsp");
                    });
                }

        </script>


    </head>
    <body>
        <nav>
            <ul>
                <li><a href="index.jsp"><img  src="imgs/home.png" alt=""/></a></li>                
                <li class="dropdown">
                    <a href="javascript:void(0)" class="dropbtn">Sensores</a>
                    <div class="dropdown-content">
                        <a href="resgistrarSensor.jsp">Registrar Sensor</a>                        
                    </div>
                </li>

                <li class="dropdown">
                    <a href="javascript:void(0)" class="dropbtn">Lecturas</a>
                    <div class="dropdown-content">
                        <a href="consultarEstadisticas.jsp">Estadisticas</a>                                           

                    </div>

                </li>
                <li class="dropdown">
                    <a href="javascript:void(0)" class="dropbtn">Usuario</a>
                    <div class="dropdown-content">                       
                        <a href="programarAlarma.jsp">Programar Alarma</a>                                                                   
                    </div>
                </li>
                <li><a href="javascript:signOut()">Logout</a></li>
            </ul>
        </nav>
        <div class="contenido1">
            <h1>¡Bienvenido!</h1>

        </div>
    </body>
</html>
