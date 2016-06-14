//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/user/");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };
id("game").style.display="none";

//Send message if "signup" is clicked
id("signup").addEventListener("click", function () {
    console.log("signup pressed");
    username = id("name").value.trim();
    if (username.length > 2 && username.length < 20)
        webSocket.send("001" + id("opengames").value + "," + username);
});

//Send message if "newgamebutton" is clicked
id("newgamebutton").addEventListener("click", function () {
    console.log("newgame pressed");
    gamename = id("gamename").value.trim();
    if (gamename.length > 2 && gamename.length < 20)
        webSocket.send("002" + id("dices").value + "," + gamename);
});

//Send message if "rolldice" is clicked
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

    } else if (typeof data.dices !== "undefined") {

        var str = data.dices;
        str = str.replace("[","");
        str = str.replace("]","");
        str = str.trim();
        var dices = str.split(",");
        console.log(dices);
        if(dices){
            dices.forEach(function (dice) {
                var sel = document.getElementById('dices');
                var opt = document.createElement('option');
                opt.innerHTML = dice;
                opt.value = dice;
                sel.appendChild(opt);
            });
        }

    } else if (typeof data.event !== "undefined") {

    console.log(data);

             var table = document.getElementById('table');
             var tr = document.createElement('tr');
             var tdId = document.createElement('td');
             tdId.appendChild(document.createTextNode(data.event));
             tr.appendChild(tdId);
             var tdType = document.createElement('td');
             tdType.appendChild(document.createTextNode(data.type));
             tr.appendChild(tdType);
             var tdName = document.createElement('td');
             tdName.appendChild(document.createTextNode(data.name));
             tr.appendChild(tdName);
             var tdReason = document.createElement('td');
             tdReason.appendChild(document.createTextNode(data.reason));
             tr.appendChild(tdReason);
             var tdResource = document.createElement('td');
             tdResource.appendChild(document.createTextNode(data.resource));
             tr.appendChild(tdResource);
             var tdPlayer = document.createElement('td');
             tdPlayer.appendChild(document.createTextNode(data.player));
             tr.appendChild(tdPlayer);
             table.appendChild(tr);

         } else if(typeof data.signedup !== "undefined"){            // if signed up

        id("signin").style.display="none";
        id("newgame").style.display="none";
        id("turn").style.display="none";
        id("game").style.display="block";
        id("rolldice").style.display="block";
        id("rolldice").disabled = true;
        var namefield = document.getElementById('playername');
        namefield.appendChild(document.createTextNode(data.signedup));
        console.log("Hello "+data.signedup+", you are logged in!");

    } else if (typeof data.notsignedup !== "undefined"){        // if not signed up

    } else if (typeof data.yourdice !== "undefined"){        // if get a dice

        console.log("You´ve rolled a " + data.yourdice);
        var throwfield = document.getElementById('playerlastthrow');
        throwfield.appendChild(document.createTextNode(data.yourdice));

    } else if (typeof data.saldo !== "undefined"){        // if get saldo

        console.log("You#re saldo is " + data.saldo);
        var saldofield = document.getElementById('playersaldo');
        saldofield.appendChild(document.createTextNode(data.saldo));

    } else if (typeof data.rolleddice !== "undefined"){        // if not signed up

        id("rolldice").disabled = true;
        id("turn").style.display="none";
        id("wait").style.display="block";

    } else if (typeof data.turn !== "undefined"){        // if not signed up
        console.log(data.turn);
        id("rolldice").disabled = false;
        id("turn").style.display="block";
        id("wait").style.display="none";

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