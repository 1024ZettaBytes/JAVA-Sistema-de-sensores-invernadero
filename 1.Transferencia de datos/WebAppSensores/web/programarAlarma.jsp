<%-- 
    Document   : agregarVideojuego
    Created on : 3/12/2018, 07:39:18 PM
    Author     : mario
--%>

<%@page import="objetos.Usuario"%>
<%@page import="objetos.Invernadero"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="Conexion.ConexionBD"%>
<%@page import="Interfaces.IConexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    int nnn;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <script src="scripts/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="scripts/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="scripts/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

        <link href="css/Estilos.css" rel="stylesheet" type="text/css"/>
        <title>Programar alarma</title>
        <script type="text/javascript">
            xmlhttp = new XMLHttpRequest();
            var url = "webresources/wsSensores/usuario/alarma/programar";
            xmlhttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    if ("ERROR_BD" !== this.responseText) {
                        if ("ERROR_FORMATO_CORREO" !== this.responseText) {
                          // Abrir modal mostrando proceso correcto
                            document.getElementById('exampleModalLabel').innerHTML = "Hecho";
                            document.getElementById('mensaje').innerHTML = "Se actualizaron los datos.";
                            $('#myModal').modal('show');
                        } else {
                            // Abrir modal informando de error en el formato de correo
                            document.getElementById('exampleModalLabel').innerHTML = "ERROR!";
                            document.getElementById('mensaje').innerHTML = "Ingrese un correo válido.";
                            $('#myModal').modal('show');
                        }
                    } else {
                        // Abrir modal informando de error en la bd
                        document.getElementById('exampleModalLabel').innerHTML = "ERROR!";
                        document.getElementById('mensaje').innerHTML = "Ocurrió un error al acceder a la base de datos. Intente nuevamente.";
                        $('#myModal').modal('show');
                    }
                }
            }


            function validateEmail(email) {
                var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }
            function enviar() {
                xmlhttp.open("POST", url, true);
                xmlhttp.setRequestHeader("Content-type", "application/json");
                xmlhttp.setRequestHeader("X-Parse-Application-Id", "VnxVYV8ndyp6hE7FlPxBdXdhxTCmxX1111111");
                xmlhttp.setRequestHeader("X-Parse-REST-API-Key", "6QzJ0FRSPIhXbEziFFPs7JvH1l11111111");
                var notif = document.getElementById("checkNotificacion").checked;
                var temp = document.getElementById("numTemp").value;
                var hum = document.getElementById("numHum").value;
                var correo = document.getElementById("txtCorreo").value;
                var parametros = {
                    "notificacion": notif,
                    "correo": correo,
                    "temperaturaCritica": parseFloat(temp),
                    "humedadCritica": parseFloat(hum)
                };
                xmlhttp.send(JSON.stringify(parametros));
            }
            function guardarDatos() {
                var correo = document.getElementById("txtCorreo").value;
                var avisoCorreo = document.getElementById("avisoCorreo");
                    if (validateEmail(correo)) {
                        enviar();
                        avisoCorreo.hidden = true;
                    } else {
                        avisoCorreo.hidden = false;
                    }
            }
            function validaEntradanum() {
                var entradaTemp = document.getElementById("numTemp");
                var entradaHum = document.getElementById("numHum");
                if ("" === entradaTemp.value) {
                    entradaTemp.value = "0";
                }
                if ("" === entradaHum.value) {
                    entradaHum.value = "0";
                }

            }


        </script>
    </head>
    <body>
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div id="mensaje" class="modal-body">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>

                    </div>
                </div>
            </div>
        </div>
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
               <!-- <li><a href="controlPrincipal?tarea=logout">Logout</a></li>-->
            </ul>
        </nav>
        <div class="contenido3">
            <h1>Programar alarma</h1>
            <div id="formulario" class="form-style-8">
                <h2>Ingrese los datos</h2>
                <form>
                    <%
                        IConexion conexion = new ConexionBD();
                        if (conexion.conectar()) {
                            Usuario u = conexion.consultarUsuario();
                            boolean nActiva = u.isNotificacionActivada();
                            String correo = u.getCorreo();
                            float tCritica = u.getTemperaturaCritica();
                            float hCritica = u.getHumedadCritica();
                            if (nActiva) {
                                out.print("<input id='checkNotificacion' type='checkbox' name='checkNotificacion' value='notificacion' checked><b>Alarma activa</b><br><br>");
                            } else {
                                out.print("<input id='checkNotificacion' type='checkbox' name='checkNotificacion' value='notificacion' ><b>Alarma activa</b><br><br>");
                            }
                            out.print("<b>Correo</b><a class='aviso'> *</a>");
                            out.print("<input id='txtCorreo'  name='txtCorreo' type='email' value='" + correo + "'  />");
                            out.print("<div id='avisoCorreo' hidden='true'><a  class='aviso' >Ingrese un correo válido</a></div><br>");
                            out.print("<b>Temperatura crítica (°C)</b>");
                            out.print("<input id='numTemp'  name='numHum' onkeyup='validaEntradanum()'  type='number' value='" + tCritica + "' required/><br>");
                            out.print("<b>Humedad crítica (HR)</b>");
                            out.print("<input id='numHum'  name='numHum'  onkeyup='validaEntradanum()' type='number' value='" + hCritica + "' required/><br>");
                            conexion.desconectar();
                        } else {
                            mostrarModal(out, false, "No se pudo conectar a la base de datos, itente nuevamente recargando la página.");
                        }
                    %>
                    <button id="btnRegistrar" type="button" onclick="guardarDatos()">Guardar</button>
                </form>
            </div>


        </div>
    </body>
    <%!
        public String mostrarModal(JspWriter out, boolean resultado, String mensaje) {
            String titulo = (resultado) ? "EXITO" : "ERROR";
            try {
                out.print("<script>$('#myModal').modal('show') "
                        + "</script>");
                out.print("<script>"
                        + "document.getElementById('exampleModalLabel').innerHTML = '" + titulo + "';"
                        + "document.getElementById('mensaje').innerHTML = '" + mensaje + "';"
                        + "</script>");
            } catch (IOException e) {
            }
            return "";
        }
    %>
</html>
