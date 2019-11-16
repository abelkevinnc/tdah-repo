console.log("reg encuesta");
function result() {
    let totalItems = 18;
    
    for (i = 1; i <= totalItems; i++){
        let radio = document.getElementsByName("item" + i)
        for (j = 0; j < radio.length; j++) {
            if (radio[j].checked) {
                console.log(radio[j].value);
            }
        }
    } 
    
}
