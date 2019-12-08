$(document).ready(function () {
	$('#tableEstudiantes').DataTable({
		"language": {
			"url": "//cdn.datatables.net/plug-ins/1.10.19/i18n/Spanish.json"
		}
	});

});

function verPerfil(codPersona) {
	const data = new FormData();
	data.append('codEstudiante', codPersona);

	fetch('/estudiante/estudianteById', {
			method: 'POST',
			body: data
		})
		.then(response => response.json())
		.then((json) => {
			console.log('Respuesta /estudiante/estudianteById: ', json)

			if (json.status === "true") {
				let estudiante = json.estudiante;
				let genero = (estudiante.genero === 'M') ? 'MASCULINO' : 'FEMENINO';

				let perfilBody = `<table class="table">
	                              <tbody>
	
	                                  <tr>
	                                      <td>Nombres y Apellidos</td>
	                                      <td>${estudiante.primerNombre} ${estudiante.segundoNombre} ${estudiante.apellidoPaterno} ${estudiante.apellidoMaterno}</td>
	                                  </tr>
	                                  <tr>
	                                      <td>Fecha de nacimiento: </td>
	                                      <td>${estudiante.fechaNacimiento}</td>
	                                  </tr>
	                                  <tr>
	                                    <td>Género: </td>
	                                    <td>${genero}</td>
									  </tr>
									  <tr>
	                                    <td>Tipo y número de documento: </td>
	                                    <td>${estudiante.tipoDocumento} ${estudiante.numeroDocumento}</td>
									  </tr>
									  <tr>
	                                    <td>Tipo de familia: </td>
	                                    <td>${estudiante.tipoFamilia}</td>
									  </tr>
									  <tr>
	                                    <td>Dirección: </td>
	                                    <td>${estudiante.contactos[0].direccion}</td>
									  </tr>
									  <tr>
	                                    <td>Correo elec.: </td>
	                                    <td>${estudiante.contactos[0].correoElectronico}</td>
									  </tr>
									  <tr>
	                                    <td>N° teléfono/móvil: </td>
	                                    <td>${estudiante.contactos[0].numeroTelefonico}</td>
									  </tr>

	                                  
	                              </tbody>
	                          </table>`;
				$("#perfilBody").html(perfilBody);
			}

		})
		.catch((error) => console.log("Error: " + error.message))



	//		.then(response => {
	//			let respJson = response.json();
	//			console.log(respJson)
	//		})
	//		.then(d => {
	//			console.log(d)
	//		})
	//		.catch( (error) => console.log("Error: " + error.message) )
}