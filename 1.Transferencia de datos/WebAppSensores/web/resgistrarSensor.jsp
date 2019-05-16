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
        <title>Registrar sensor</title>
        <script type="text/javascript">
            //notar el protocolo.. es 'ws' y no 'http'
            var wsUri = "ws://localhost:8080/WebAppSensores/consultarDisponibilidadSensor";
            var wsUri2 = "ws://localhost:8080/WebAppSensores/agregarSensor";
            var websocket = new WebSocket(wsUri); //creamos el socket
            var socketAgregar = new WebSocket(wsUri2); //creamos el socket
            var estadoDiv = null;
            var sensor;
            var invernadero;
            var marca;
            var valido = false;
            var valorSensor = "";
            var valorInvernadero = null;
            var valorCampo = "";
            var valorMarca = "";

            websocket.onmessage = function (evt) { // cuando se recibe un mensaje

                if (evt.data === "true") {
                    sensor.style.border = "1px solid green";
                    estadoDiv.hidden = true;
                    valido = true;
                } else {
                    valido = false;
                    estadoDiv.hidden = false;
                    sensor.style.border = "1px solid red";
                    estadoDiv.innerHTML = "El ID ingresado ya está en uso!.";
                    estadoDiv.style.color = "red";

                }
            };
            socketAgregar.onmessage = function (evt) { // cuando se recibe un mensaje
                var titulo;
                var mensaje;
                if (evt.data === "true") {
                    titulo = "HECHO!";
                    mensaje = "Sensor registrado."
                    invernadero.style.border = "1px solid red";
                    invernadero.value = "";
                    sensor.value = "";
                    sensor.style.border = "1px solid red";
                    marca.value = "";
                    marca.style.border = "1px solid red";
                    aviso = document.getElementById("aviso");
                    aviso.hidden = true;
                } else {
                    titulo = "ERROR";
                    mensaje = "Ocurrió un problema. Intente de nuevo."
                }
                document.getElementById('exampleModalLabel').innerHTML = titulo;
                document.getElementById('mensaje').innerHTML = mensaje;
                $('#myModal').modal('show');
            };
            function validaId() {
                sensor = document.getElementById("sensor");
                estadoDiv = document.getElementById("estado");
                valorSensor = sensor.value;
                if ("" !== valorSensor) {
                    if (valorCampo !== valorSensor) {
                        valorCampo = valorSensor;
                        websocket.send(valorSensor);
                    }
                } else {
                    valorCampo = "";
                    sensor.style.border = "1px solid red"
                    estadoDiv.hidden = true;
                }
            }
            function validaInvernadero(){
                invernadero = document.getElementById("invernaderoOption");
                invernadero.style.border = "1px solid green";
            }
            function validaMarca() {
                marca = document.getElementById("marca");
                valorMarca = marca.value;
                if ("" !== valorMarca) {
                    marca.style.border = "1px solid green";
                } else {
                    marca.style.border = "1px solid red";
                }
            }
            function registrar() {
                invernadero = document.getElementById("invernaderoOption");
                marca = document.getElementById("marca");
                valorInvernadero = invernadero.value;
                valorMarca = marca.value;
                if ("" !== valorInvernadero && "" !== valorSensor && "" !== valorMarca && valido) {
                    msg = "{\n"
                            + "\t'idInvernadero':'" + valorInvernadero + "',\n"
                            + "\t'idSensor':'" + valorSensor + "',\n"
                            + "\t'marca':'" + valorMarca + "'\n"
                            + "}";
                    socketAgregar.send(msg);
                } else {
                    aviso = document.getElementById("aviso");
                    aviso.innerHTML = "Asegurese de completar todos los campos."
                    aviso.style.color = "red";
                    aviso.hidden = false;
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
        <div class="contenido1">
            <h1>Registrar nuevo sensor</h1>
            <div class="form-style-8">
                <h2>Ingresa los datos del sensor a registrar</h2>
                <form id="formulario">
                    <select id="invernaderoOption" class="obligatorio" onchange="validaInvernadero()" required>
                        <option value="" disabled selected>Seleccione un invernadero...</option>
                        <%
                            IConexion conexion = new ConexionBD();
                            if (conexion.conectar()) {
                                ArrayList<Invernadero> invernaderos = conexion.consultarInvernaderos();
                                for (Invernadero i : invernaderos) {
                                    int id = i.getIdInvernadero();
                                    out.print("<option value='" + id + "'>Invernadero " + id + "</option>");
                                }
                                conexion.desconectar();
                            } else {
                                mostrarModal(out, false, "No se pudo conectar a la base de datos, itente nuevamente recargando la página.");

                            }
                        %>
                    </select>
                    <input id="sensor" class="obligaorio" name="numero" min="1" max="2147483646" placeholder="Indique ID de sensor..."  type="number" onkeyup="validaId()" onclick="validaId()" oninput="validity.valid||(value='');" required/>
                    <a id="estado" hidden="true"></a>
                    <input id="marca" class="obligaorio" name="marca" type="text" placeholder="Marca" onkeyup="validaMarca()" required/>
                    <a id="aviso" hidden="true"></a>
                    <br> 
                    <button id="btnRegistrar" type="button"  onclick="registrar()">Registrar</button>
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
