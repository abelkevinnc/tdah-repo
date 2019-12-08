function randomSelect() {
	
	
	for(i=1; i <= 18; i++) {
		 const numberRandom = Math.floor((Math.random() * 2) + 1);
		 let idRadio = "radio" + numberRandom + "_"+i;
		 console.log(idRadio)
		 
		 document.getElementById(idRadio).checked = true;
		 
	}
}