function validarFormulario() {

    var primerNombre = $("#primerNombre").val();
    var apellidoPaterno = $("#apellidoPaterno").val();
    var apellidoMaterno = $("#apellidoMaterno").val();
    var fechaNacimiento = $("#fechaNacimiento").val();
    var numeroDocumento = $("#numeroDocumento").val();

    var contentBody = "<p>Revise el formulario y vuelva a intentar</p>";


    var passed = true;

    if(primerNombre === "") {
        contentBody += "<p> * INGRESE EL NOMBRE</p>";
        passed = false;
    }

    if(apellidoPaterno === "") {
        contentBody += "<p> * INGRESE EL APELLIDO PATERNO</p>";
        passed = false;
    }
    if(apellidoMaterno === "") {
        contentBody += "<p> * INGRESE EL APELLIDO MATERNO</p>";
        passed = false;
    }
    if(fechaNacimiento === "") {
        contentBody += "<p> * SELECCIONE LA FECHA DE NACIMIENTO</p>";
        passed = false;
    }
    if(numeroDocumento === "") {
        contentBody += "<p> * INGRESE EL NUMERO DE DOCUMENTO</p>";
        passed = false;
    }

    if(!passed) {
        console.log(":(")
        $("#message_error").html(contentBody);
        $("#message_error").show();
        return false;
    } else {
        console.log("pasó")
        $("#validationDiv").hide();
        $("#sendForm").show();
        $("#message_success").show();
    }

}

function ok_inputText() {
	let div_mensaje = document.getElementById("message_error");
	div_mensaje.style.display = "none";
}