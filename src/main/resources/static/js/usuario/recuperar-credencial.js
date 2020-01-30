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
			console.log("se envio correctamente")
			var div_codigo = document.getElementById("div_codigo");
			div_codigo.style.display = "block";

		} else if (statusCode === '2') {
			console.log("se envio correctamente")
			
		} else if (statusCode === '3') {
			console.log("se envio correctamente")
			
		} else if (statusCode === '4') {
			console.log("se envio correctamente")
			
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
		
	})
	.catch((error) => console.log("Error: " + error.message))
	
}
	
	