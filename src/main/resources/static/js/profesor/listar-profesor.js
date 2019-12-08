$(document).ready(function() {
	$('#tableProfesores').DataTable({
		"language" : {
			"url" : "//cdn.datatables.net/plug-ins/1.10.19/i18n/Spanish.json"
		}
	});

});

function verPerfil(codPersona) {
	const data = new FormData();
	data.append('codProfesor', codPersona);

	fetch('/profesor/profesorById', {
			method: 'POST',
			body: data
		})
		.then(response => response.json())
		.then((json) => {
			console.log('Respuesta /profesor/profesorById: ', json)

			if (json.status === "true") {
				let profesor = json.profesor;
				let genero = (profesor.genero === 'M') ? 'MASCULINO' : 'FEMENINO';

				let perfilBody = `<table class="table">
	                              <tbody>
	
	                                  <tr>
	                                      <td>Nombres y Apellidos</td>
	                                      <td>${profesor.primerNombre} ${profesor.segundoNombre} ${profesor.apellidoPaterno} ${profesor.apellidoMaterno}</td>
	                                  </tr>
	                                  <tr>
	                                      <td>Fecha de nacimiento: </td>
	                                      <td>${profesor.fechaNacimiento}</td>
	                                  </tr>
	                                  <tr>
	                                    <td>Género: </td>
	                                    <td>${genero}</td>
									  </tr>
									  <tr>
	                                    <td>Tipo y número de documento: </td>
	                                    <td>${profesor.tipoDocumento} ${profesor.numeroDocumento}</td>
									  </tr>
									  <tr>
	                                    <td>Código modular: </td>
	                                    <td>${profesor.codigoDocente}</td>
									  </tr>
									  <tr>
	                                    <td>Dirección: </td>
	                                    <td>${profesor.contactos[0].direccion}</td>
									  </tr>
									  <tr>
	                                    <td>Correo elec.: </td>
	                                    <td>${profesor.contactos[0].correoElectronico}</td>
									  </tr>
									  <tr>
	                                    <td>N° teléfono/móvil: </td>
	                                    <td>${profesor.contactos[0].numeroTelefonico}</td>
									  </tr>

	                                  
	                              </tbody>
	                          </table>`;
				$("#perfilBody").html(perfilBody);
			}

		})
		.catch((error) => console.log("Error: " + error.message))
}