//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/user/");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };

//Send message if "Send" is clicked
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
});

//Send message if "Send" is clicked
id("signup").addEventListener("click", function () {
    console.log("button pressed");
    username = id("name").value.trim();
    if (username.length > 2 && username.length < 20)
        webSocket.send("001" + id("opengames").value + "," + username);
});

////Send a message if it's not empty, then clear the input field
//function sendMessage(message) {
//    console.log(message);
//    if (message === "") {
//        webSocket.send(message);
//        id("message").value = "";
//    }
//    if (message === "showgames"){
//        webSocket.send(id("opengames").value);
//    }
//}

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
                insert("userlist", "<li>" + game + "</li>");

                var sel = document.getElementById('opengames');
                var opt = document.createElement('option');
                opt.innerHTML = game;
                opt.value = game;
                sel.appendChild(opt);
            });
        }

    } else if(typeof data.signedup !== "undefined"){            // if signed up
        id("name").style.display="none";
        id("signup").style.display="none";
        id("opengames").style.display="none";
        console.log(data.signedup);

    } else if (typeof data.notsignedup !== "undefined"){        // if not signed up

    }
    else {
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