function operacionEncuesta(codEncuesta, tipoOperacion) {
	
	const data = new FormData();
	data.append('codEncuesta', codEncuesta);
	data.append('tipoOperacion', tipoOperacion);

	fetch('/encuesta/operacion', {
			method: 'POST',
			body: data
		})
		.then(response => response.json())
		.then((json) => {
			console.log('Respuesta /encuesta/operacion: ', json)
			if (json.status === "true") {
				location.reload();
			}
		})
		.catch((error) => console.log("Error: " + error.message))

}