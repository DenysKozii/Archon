var countDown = 10;
var x = setInterval(function () {
    if (countDown > 0) {
        document.getElementById("demo2").innerHTML = --countDown;
    } else {
        document.getElementById("demo2").innerHTML = 0;
        // document.location.reload(true); //POST
        document.location.href = "http://localhost:8085/game/answer/0";
    }
}, 1000);