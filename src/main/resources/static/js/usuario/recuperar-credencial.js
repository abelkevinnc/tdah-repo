var alert_success = document.getElementById("alert_success");
var alert_danger = document.getElementById("alert_danger");

function enviarDatos(){
	
	let nombreUsuario = document.getElementById("nombreUsuario");
	let numeroTelefono = document.getElementById("numeroTelefono");
	
	const data = new FormData();
	data.append('nombreUsuario', nombreUsuario.value);
	data.append('numeroTelefono', numeroTelefono.value);
	
	fetch('/usuario/recuperar-credencial', {
		method: 'POST',
		body: data
	})
	.then(response => response.json())
	.then((json) => {
		console.log('Respuesta /usuario/recuperar-credencial: ', json)
		let statusCode = json.statusCode;
		
		if(statusCode === '1'){
			var div_codigo = document.getElementById("div_codigo");
			div_codigo.style.display = "block";

		} else if (statusCode === '2') {
			showMessageDanger("El número telefónico no está asociado al usuario.");
		} else if (statusCode === '3') {
			showMessageDanger("El usuario no existe.");
		} else if (statusCode === '4') {
			showMessageDanger("No se pudo enviar el código correctamente, reintentar.");
		}
	})
	.catch((error) => console.log("Error: " + error.message))

}

function validarCodigo() {
	let codigoVerificacion = document.getElementById("codigoVerificacion");
	const data = new FormData();
	data.append('codigoVerificacion', codigoVerificacion.value);
	
	fetch('/usuario/validar-codigo', {
		method: 'POST',
		body: data
	})
	.then(response => response.json())
	.then((json) => {
		console.log(json);
		let status = json.status;
		if(status === true) {
			let step1 = document.getElementById("step1");
			let step2 = document.getElementById("step2");
			step1.style.display = "none";
			step2.style.display = "block";
						
			let li_step1 = document.getElementById("li_step1");
			li_step1.classList.remove("active");
			li_step1.classList.add("disabled");
			let li_step2 = document.getElementById("li_step2");
			li_step2.classList.add("active");
			li_step2.classList.remove("disabled");
			
		} else {
			console.log("CODIGO INCORRECTO");
		}
	})
	.catch((error) => console.log("Error: " + error.message))
	
}

function guardarCambios() {
	let nuevaClave1 = document.getElementById("nuevaClave1");
	let nuevaClave2 = document.getElementById("nuevaClave2");
	
	
	if(nuevaClave1.value === "" || nuevaClave2.value === "") {
		showMessageDanger("Debe ingresar las dos claves.")
		return;
	}
	
	if(nuevaClave1.value !== nuevaClave2.value){
		showMessageDanger("Las claves no coinciden.")
		return;
	}
	
	const data = new FormData();
	data.append('nuevaClave', nuevaClave1.value);
	
	fetch('/usuario/cambiar-clave', {
		method: 'POST',
		body: data
	})
	.then(response => response.json())
	.then((json) => {
		console.log(json);
		let status = json.status;
		if(status === true) {
			
			showMessageSuccess("Se cambió la contraseña exitosamente");
			let myVar = setInterval(redireccionar, 2500);	
			
		} else {
			console.log("ocurrio algo en el cambio de clave");
		}
	})
	.catch((error) => console.log("Error: " + error.message))
	
}
	
function redireccionar() {	
	location.href = "/autenticacion/login";
}

function showMessageSuccess(mensaje) {
	document.getElementById("message_success").innerHTML = "" + mensaje;
	alert_success.style.display = "block";
}

function showMessageDanger(mensaje) {
	document.getElementById("message_danger").innerHTML = "" + mensaje;
	alert_danger.style.display = "block";
	setInterval(closeMessageDanger, 3500);	
}

function closeMessageDanger() {
	alert_danger.style.display = "none";
}