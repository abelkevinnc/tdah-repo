$(document).ready(function() {
	$('#tableEstudiantes').DataTable({
		"language" : {
			"url" : "//cdn.datatables.net/plug-ins/1.10.19/i18n/Spanish.json"
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
		.then(response => {
			console.log(response.json());
		})
		.catch( (error) => console.log("Error: " + error.message) )
}
