<%-- 
    Document   : login
    Created on : 6/12/2018, 03:35:15 AM
    Author     : Eduardo Ramírez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Iniciar sesión</title>


        <script src="https://apis.google.com/js/platform.js" async defer></script>
                <script src="scripts/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <script src="scripts/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="scripts/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
        <link href="css/Estilos.css" rel="stylesheet" type="text/css"/>
        <meta name="google-signin-client_id" content="340054074881-5aikhj5soo0ifgm55ddraja20cj70qoc.apps.googleusercontent.com">
        <script>
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    console.log(this.responseText);
                    if ("1" === this.responseText) {
                        window.location.replace("index.jsp");
                        
                    } else {
                        signOut();
                        document.getElementById('exampleModalLabel').innerHTML = "Error";
                        document.getElementById('mensaje').innerHTML = "Inicie sesión con el correo registrado en el sistema.";
                        $('#myModal').modal('show');
                    }
                }
            }
            function onSignIn(googleUser) {
                var id_token = googleUser.getAuthResponse().id_token;
                xmlhttp.open("POST", 'webresources/wsSensores/auth', true);
                xmlhttp.setRequestHeader("Content-type", "application/json");
                xmlhttp.setRequestHeader("X-Parse-Application-Id", "VnxVYV8ndyp6hE7FlPxBdXdhxTCmxX1111111");
                xmlhttp.setRequestHeader("X-Parse-REST-API-Key", "6QzJ0FRSPIhXbEziFFPs7JvH1l11111111");
                var parametros = {
                    "token": id_token
                };
                xmlhttp.send(JSON.stringify(parametros));
            }

            function signOut() {
                var auth2 = gapi.auth2.getAuthInstance();
                auth2.signOut().then(function () {
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
        <div class="contenidoLogin">
            <div class="form-style-8">
                <h2>Iniciar Sesión</h2><br>
                <div class="btnLogin">
                    <div  class="g-signin2"  data-onsuccess="onSignIn" ></div>
                </div>
            </div>
        </div>
    </div>


</body>

</html>