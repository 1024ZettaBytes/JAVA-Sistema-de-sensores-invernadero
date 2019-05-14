<%-- 
    Document   : consultarEstadisticas
    Created on : 12/05/2019, 05:49:07 PM
    Author     : ed000
--%>

<%@page import="objetos.Sensor"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="objetos.Invernadero"%>
<%@page import="Conexion.ConexionBD"%>
<%@page import="Interfaces.IConexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <head>  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <link rel="stylesheet" href="chartjs/chartist.min.css">
        <script src="scripts/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="scripts/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="scripts/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
        <script src="scripts/canvasjs.min.js"></script>
        <script src="chartjs/chartist.min.js"></script>
        <link href="css/Estilos.css" rel="stylesheet" type="text/css"/>
        <title>Estadíticas</title>
        
        <script>
            function actualizaGSensor(){
                
            }
            function actualizaGInvernadero(){  
            }
            window.onload = function () {
                document.getElementById('fecha').valueAsDate = new Date();
// Initialize a Line chart in the container with the ID chart1
  new Chartist.Line('#graficoInvernadero', {
    labels: ["lunes", "martes", "miercoles", "jueves"],
    series: [[100, 120, 180, 200]]
  });
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
                    <a href="javascript:void(0)" class="dropbtn">Rentas</a>
                    <div class="dropdown-content">
                        <a href="rentarVideojuego.jsp">Rentar un videojuego</a>                        
                        <a href="devolverVideojuego.jsp">Devolver un videojuego</a>
                        <a href="consultaRentasCliente.jsp">Consultar rentas de un cliente</a>                        
                        <a href="consultaRentasPeriodo.jsp">Consultar rentas dado un periodo de fechas</a>
                    </div>
                </li>

                <li class="dropdown">
                    <a href="javascript:void(0)" class="dropbtn">Clientes</a>
                    <div class="dropdown-content">
                        <a href="agregarCliente.jsp">Agregar</a>                        
                        <a href="eliminaCliente.jsp">Eliminar</a>                         
                        <a href="listaClientes.jsp">Consultar lista de clientes</a>
                        <a href="consultaClienteCred.jsp">Consultar cliente por ID</a>                        

                    </div>

                </li>
                <li class="dropdown">
                    <a href="javascript:void(0)" class="dropbtn">Videojuegos</a>
                    <div class="dropdown-content">                       
                        <a href="agregarVideojuego.jsp">Agregar videojuego</a>                                                                   
                    </div>
                <li class="dropdown">
                    <a href="javascript:void(0)" class="dropbtn">Inventario</a>
                    <div class="dropdown-content">                       
                        <a href=agregarInventario.jsp>Inventariar unidades</a>                        
                        <a href="eliminarInventario.jsp">Desinventariar videojuego</a>                                             
                    </div>
                </li>
                <li><a href="controlPrincipal?tarea=logout">Logout</a></li>
            </ul>
        </nav>
        <div class="contenido">
            <h1>Estadíticas de lecturas</h1>
            <div class="form-style-8">
                <h2>Consulta por fecha</h2>
                <form id="formulario">
                    <input id="fecha" type="date" />
                    <select id="invernaderoOption" onChange="actualizaGInvernadero()" class="obligatorio" required>
                        <option value="" disabled selected>Seleccione un invernadero...</option>
                        <%
                            IConexion conexion = new ConexionBD();
                            if (conexion.conectar()) {
                                ArrayList<Invernadero> invernaderos = conexion.consultarInvernaderos();
                                for (Invernadero i : invernaderos) {
                                    int id = i.getIdInvernadero();
                                    out.print("<option value='" + id + "'>Invernadero " + id + "</option>");
                                }
                            } else {
                                mostrarModal(out, false, "No se pudo conectar a la base de datos, itente nuevamente recargando la página.");

                            }
                        %>
                    </select>
                    <select id="sensorOption" class="obligatorio" required>
                        <option value="" disabled selected>Seleccione un sensor...</option>
                    </select>
                    <a id="aviso" hidden="true"></a>
                    <br>
                    <a id="aviso" hidden="true"></a>
                    <div id="graficoInvernadero" class="ct-chart ct-perfect-fourth"></div>

                    <button id="btnRegistrar" type="button"  onclick="registrar()">Registrar</button>
                </form>
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