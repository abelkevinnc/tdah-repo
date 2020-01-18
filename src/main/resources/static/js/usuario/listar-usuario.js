$(document).ready(function() {
	$('#tableUsuarios').DataTable({
		"language" : {
			"url" : "//cdn.datatables.net/plug-ins/1.10.19/i18n/Spanish.json"
		}
	});

});

function verPerfil(codUsuario) {
	const data = new FormData();
	data.append('codUsuario', codUsuario);
	
	fetch('/usuario/usuarioById', {
		method: 'POST',
		body: data
	})
	.then(response => response.json())
	.then((json) => {
		console.log('Respuesta /usuario/usuarioById: ', json)

		if (json.status === "true") {
			
			let usuario = json.usuario;
			let genero = (usuario.genero === 'M') ? 'MASCULINO' : 'FEMENINO';
			
			let estadoUsuario = (usuario.estado === 'ACTIVO') ? 'success' : 'danger';

			let perfilBody = `<table class="table">
                              <tbody>

                                  <tr>
                                      <td>Nombres y Apellidos</td>
                                      <td>${usuario.primerNombre} ${usuario.segundoNombre} ${usuario.apellidoPaterno} ${usuario.apellidoMaterno}</td>
                                  </tr>
                                  <tr>
                                      <td>Fecha de nacimiento: </td>
                                      <td>${usuario.fechaNacimiento}</td>
                                  </tr>
                                  <tr>
                                    <td>Género: </td>
                                    <td>${genero}</td>
								  </tr>
								  <tr>
                                    <td>Tipo y número de documento: </td>
                                    <td>${usuario.tipoDocumento} ${usuario.numeroDocumento}</td>
								  </tr>
								  <tr>
                                    <td>Dirección: </td>
                                    <td>${usuario.contactos[0].direccion}</td>
								  </tr>
								  <tr>
                                    <td>Correo elec.: </td>
                                    <td>${usuario.contactos[0].correoElectronico}</td>
								  </tr>
								  <tr>
                                    <td>N° teléfono/móvil: </td>
                                    <td>${usuario.contactos[0].numeroTelefonico}</td>
								  </tr>
								  <tr>
                                    <td>Fecha de registro del usuario: </td>
                                    <td>${usuario.fechaRegistro}</td>
								  </tr>
								  <tr>
                                    <td>Nick usuario: </td>
                                    <td>${usuario.nombreUsuario}</td>
								  </tr>
								  <tr>
                                    <td>Rol asignado: </td>
                                    <td>${usuario.rol.rolUsuario}</td>
								  </tr>
								  <tr>
                                    <td>Fecha renovación permiso: </td>
                                    <td>${usuario.rol.fechaRenovacion}</td>
								  </tr>
								  <tr>
                                    <td>Estado: </td>
                                    <td><span class="badge badge-${estadoUsuario}">${usuario.estado}</span></td>
								  </tr>
								  
                              </tbody>
                          </table>`;
			$("#perfilBody").html(perfilBody);
		}

	})
	.catch((error) => console.log("Error: " + error.message))
}