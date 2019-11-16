console.log("reg encuesta");
function result() {
    let totalItems = 2;
    
    for (i = 1; i <= 2; i++){
        let radio = document.getElementsByName("item" + i)
        for (j = 0; j < radio.length; j++) {
            if (radio[j].checked) {
                console.log(radio[j].value);
            }
        }
    } 
    
}
