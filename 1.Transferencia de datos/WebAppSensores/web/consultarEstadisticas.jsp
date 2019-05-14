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
        <script src="chartjs/chartist.min.js"></script>
        <script src="chartjs/chartist-bar-labels.js"></script>
        <script src="chartjs/tootltip/chartist-plugin-tooltip.js"></script>
        <link href="chartjs/tootltip/chartist-plugin-tooltip.css" rel="stylesheet" type="text/css"/>
        <script src="chartjs/axistitle/chartist-plugin-axistitle.js"></script>
        <link href="css/Estilos.css" rel="stylesheet" type="text/css"/>
        <title>Estadíticas</title>

        <script>
            function restaura() {
                var optInv = document.getElementById("invernaderoOption");
                var tituloInv = document.getElementById("tituloGraficoInvernadero");
                var grafInv = document.getElementById("cInvernadero");
                var optSens = document.getElementById("sensorOption");
                var tituloSens = document.getElementById("tituloGraficoSensor");
                var grafSens = document.getElementById("cSensor");
                optInv.selectedIndex = "0";
                tituloInv.innerHTML = "";
                tituloInv.hidden = true;
                grafInv.hidden = true;
                optSens.innerHTML = "<option value='' disabled selected>Seleccione un sensor...</option>";
                optSens.disabled = true;
                optSens.selectedIndex = "0";
                tituloSens.innerHTML = "";
                tituloSens.hidden = true;
                grafSens.hidden = true;
            }
            function fechaConvertida(fechaFormatoViejo) {
                var f = fechaFormatoViejo + "";
                var anio = f.substring(0, 4);
                var mes = f.substring(5, 7);
                var dia = f.substring(8, 10);
                return dia + "-" + mes + "-" + anio;
            }
            // Request al server por parte de la lista de invernaderos para obtener sensores
            var requestObtenSensores = new XMLHttpRequest();
            requestObtenSensores.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    if ("ERROR_BD" !== this.responseText) {
                        var myObj = JSON.parse(this.responseText);
                        var arraySensores = myObj.sensores;
                        var opciones = ""
                        if (arraySensores.length > 0) {
                            // Llenar lista de sensores
                            opciones = "<option value='' disabled selected>Seleccione un sensor...</option>";
                            for (var i = 0; i < arraySensores.length; i++) {
                                var idSensor = arraySensores[i].idSensor;
                                opciones += "<option value='" + idSensor + "'>Sensor " + idSensor + "</option>";
                            }
                            document.getElementById("sensorOption").disabled = false;
                        } else {
                            document.getElementById("sensorOption").disabled = true;
                            opciones = "<option value='' disabled selected>No hay sensores registrados</option>";
                        }
                        $("#sensorOption").html(opciones);
                    } else {
                        // Abrir modal informando de error en la bd
                        document.getElementById('exampleModalLabel').innerHTML = "ERROR!";
                        document.getElementById('mensaje').innerHTML = "Ocurrió un error al acceder a la base de datos. Intente nuevamente.";
                        $('#myModal').modal('show');
                    }
                }
            };
            // Request al server por parte de la lista de invernaderos para obtener los promedios de lecturas
            var requestObtenPromedios = new XMLHttpRequest();
            requestObtenPromedios.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    if ("ERROR_BD" !== this.responseText) {
                        if ("ERROR_FORMATO_FECHA" !== this.responseText) {
                            var myObj = JSON.parse(this.responseText);
                            var arrayDatos = myObj.lecturasPromedio;
                            var tituloInvernado = document.getElementById("tituloGraficoInvernadero");
                            var cGraficoInvernadero = document.getElementById("cInvernadero");
                            var idInvernadero = document.getElementById("invernaderoOption").value;
                            var fecha = fechaConvertida(document.getElementById("fecha").value);
                            if (arrayDatos.length > 0) {
                                // Guarda los promedios
                                var data = {
                                    labels: ['Temperatura', 'Humedad'],
                                    width: 200,
                                    series: [
                                        [arrayDatos[0].temperaturaPromedio],
                                        [arrayDatos[0].humedadPromedio]
                                    ]
                                };
                                var options = {
                                    seriesBarDistance: 450,
                                    plugins: [
                                        Chartist.plugins.ctBarLabels(),
                                        Chartist.plugins.ctAxisTitle({
                                            
                                            axisY: {
                                                axisTitle: 'Medida(Celcius)',
                                                axisClass: 'ct-axis-title',
                                                offset: {
                                                    x: 0,
                                                    y: 0
                                                },
                                                textAnchor: 'middle',
                                                flipTitle: false
                                            }
                                        })
                                    ],
                                    width: '100%',
                                    height: '400px'
                                };
                                new Chartist.Bar('#graficoInvernadero', data, options);
                                tituloInvernado.innerHTML = "Promedio de temperatura y humedad para el invernadero "+idInvernadero+" el día "+fecha;
                                tituloInvernado.style.color = "black";
                                tituloInvernado.hidden = false;
                                cGraficoInvernadero.hidden = false;
                            } else {
                                tituloInvernado.innerHTML = "No existen lecturas registradas para el invernadero "+idInvernadero+" el día "+fecha;
                                tituloInvernado.style.color = "red";
                                tituloInvernado.hidden = false;
                                cGraficoInvernadero.hidden = true;
                            }
                        } else {
                            // Abrir modal informando de error en el formato de fecha
                            document.getElementById('exampleModalLabel').innerHTML = "ERROR!";
                            document.getElementById('mensaje').innerHTML = "La fecha es incorrecta. Intente nuevamente.";
                            $('#myModal').modal('show');
                        }
                    } else {
                        // Abrir modal informando de error en la bd
                        document.getElementById('exampleModalLabel').innerHTML = "ERROR!";
                        document.getElementById('mensaje').innerHTML = "Ocurrió un error al acceder a la base de datos. Intente nuevamente.";
                        $('#myModal').modal('show');
                    }
                }
            };
            // Request al server por parte de la lista de sensores para obtener las lecturas del sensor seleccionado
            var requestObtenLecturasSensor = new XMLHttpRequest();
            requestObtenLecturasSensor.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    if ("ERROR_BD" !== this.responseText) {
                        if ("ERROR_FORMATO_FECHA" !== this.responseText) {
                            var myObj = JSON.parse(this.responseText);
                            var arrayLecturas = myObj.lecturas;
                            var tituloSensor = document.getElementById("tituloGraficoSensor");
                            var cGraficoSensor = document.getElementById("cSensor");
                            var idSensor = document.getElementById("sensorOption").value;
                           var fecha = fechaConvertida(document.getElementById("fecha").value);
                            if (arrayLecturas.length > 0) {
                                
                                var arrayHoras = [];
                                var arrayTemperatura = [];
                                var arrayHumedad = [];
                                for (var i = 0; i < arrayLecturas.length; i++) {
                                    var hora = arrayLecturas[i].hora;
                                    var temperatura = arrayLecturas[i].temperatura;
                                    var humedad = arrayLecturas[i].humedad;
                                    arrayHoras.push(hora);
                                    arrayTemperatura.push(temperatura);
                                    arrayHumedad.push(humedad);
                                }
                                var options = {
                                    chartPadding: {
                                        top: 20,
                                        right: 0,
                                        bottom: 30,
                                        left: 0
                                    },
                                    axisY: {
                                        onlyInteger: true
                                    },
                                    plugins: [
                                        Chartist.plugins.tooltip(),
                                        Chartist.plugins.ctAxisTitle({
                                            axisX: {
                                                axisTitle: 'Hora(24 hrs)',
                                                axisClass: 'ct-axis-title',
                                                offset: {
                                                    x: 0,
                                                    y: 50
                                                },
                                                textAnchor: 'middle'
                                            },
                                            axisY: {
                                                axisTitle: 'Medida(Celcius)',
                                                axisClass: 'ct-axis-title',
                                                offset: {
                                                    x: 0,
                                                    y: 0
                                                },
                                                textAnchor: 'middle',
                                                flipTitle: false
                                            }
                                        })
                                    ],
                                    width: '100%',
                                    height: '400px'
                                };
                                var gSensor = new Chartist.Line('#graficoSensor', {
                                    labels: arrayHoras,
                                    series: [arrayTemperatura, arrayHumedad]
                                }, options);
                                tituloSensor.innerHTML = "Lecturas realizadas por el sensor " +idSensor+" el día  "+fecha;
                                tituloSensor.style.color = "black";
                                tituloSensor.hidden = false;
                                cGraficoSensor.hidden = false;
                            } else {
                                tituloSensor.innerHTML = "No existen lecturas registradas el día "+fecha+" por el sensor  "+idSensor;
                                tituloSensor.style.color = "red";
                                tituloSensor.hidden = false;
                                cGraficoSensor.hidden = true;
                            }
                        } else {
                            // Abrir modal informando de error en el formato de fecha
                            document.getElementById('exampleModalLabel').innerHTML = "ERROR!";
                            document.getElementById('mensaje').innerHTML = "La fecha es incorrecta. Intente nuevamente.";
                            $('#myModal').modal('show');
                        }
                    } else {
                        // Abrir modal informando de error en la bd
                        document.getElementById('exampleModalLabel').innerHTML = "ERROR!";
                        document.getElementById('mensaje').innerHTML = "Ocurrió un error al acceder a la base de datos. Intente nuevamente.";
                        $('#myModal').modal('show');
                    }
                }
            };
            function seleccionSensor() {
                var sensorSeleccionado = document.getElementById("sensorOption").value;
                var fecha = document.getElementById('fecha').value;
                requestObtenLecturasSensor.open("GET", "webresources/wsSensores/lecturas/sensor/fecha/" + sensorSeleccionado + "/" + fechaConvertida(fecha), true);
                requestObtenLecturasSensor.send();
            }
            function seleccionInvernadero() {
                var invernaderoSeleccionado = document.getElementById("invernaderoOption").value;
                requestObtenSensores.open("GET", "webresources/wsSensores/sensores/invernadero/id/" + invernaderoSeleccionado, true);
                requestObtenSensores.send();
                var fecha = document.getElementById('fecha').value;
                requestObtenPromedios.open("GET", "webresources/wsSensores/lecturas/invernadero/fecha/promedio/" + invernaderoSeleccionado + "/" + fechaConvertida(fecha), true);
                requestObtenPromedios.send();
            }

            window.onload = function () {
                document.getElementById('fecha').valueAsDate = new Date();
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
            <h1>Estadísticas de lecturas</h1>
            <div class="form-style-8">
                <h2>Consulta por fecha</h2>
                <form id="formulario">
                    <input id="fecha" onchange="restaura()" type="date" />
                    <select id="invernaderoOption" onChange="seleccionInvernadero()" required>
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
                    <select id="sensorOption" onchange="seleccionSensor()" disabled="true" required>
                        <option value="" disabled selected>Seleccione un sensor...</option>
                    </select>
                    <a id="tituloGraficoInvernadero" hidden="true"></a>
                    <div id="cInvernadero" hidden="true">
                    <div id="graficoInvernadero" class="ct-chart ct-perfect-fourth"></div>
                    </div>
                    <br>
                    <a id="tituloGraficoSensor" hidden="true"></a>
                    <div id="cSensor" hidden="true">
                    <div id="graficoSensor" class="ct-chart ct-perfect-fourth"></div>
                    </div>
                    <br>
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