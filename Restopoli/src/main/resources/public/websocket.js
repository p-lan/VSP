//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/user/");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };
id("game").style.display="none";

//Send message if "Send" is clicked
id("signup").addEventListener("click", function () {
    console.log("button pressed");
    username = id("name").value.trim();
    if (username.length > 2 && username.length < 20)
        webSocket.send("001" + id("opengames").value + "," + username);
});

//Send message if "Send" is clicked
id("rolldice").addEventListener("click", function () {
    console.log("button pressed");
    webSocket.send("101" + "RollDice");
    id("turn").style.display="none";
    id("wait").style.display="block";
});

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
    var data = JSON.parse(msg.data);

    // show games --------------------------------------
    if(typeof data.games !== "undefined"){                      // if there are open games for you
        var str = data.games;
        str = str.replace("[","");
        str = str.replace("]","");
        str = str.trim();
        var games = str.split(",");
        console.log(games);
        if(games){
            games.forEach(function (game) {
                var sel = document.getElementById('opengames');
                var opt = document.createElement('option');
                opt.innerHTML = game;
                opt.value = game;
                sel.appendChild(opt);
            });
        }

    } else if(typeof data.signedup !== "undefined"){            // if signed up
        id("signin").style.display="none";
        id("turn").style.display="none";
        id("game").style.display="block";
        console.log(data.signedup);

    } else if (typeof data.notsignedup !== "undefined"){        // if not signed up

    } else if (typeof data.rolleddice !== "undefined"){        // if not signed up
        console.log("You´ve rolled a " + data.rolleddice);
        id("rolldice").style.display="none";

    } else {
        console.log("Message is undefined");
    }
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}
//
//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}