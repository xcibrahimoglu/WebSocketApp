const client = new WebSocket("ws://localhost:8080/WebSocketApp/chat");
client.onopen = function (event) {
        console.log("Connected");
    };
    //var date = new Date();
    //var currentDate = date.getFullYear()+'-'+("0" + (date.getMonth() + 1)).slice(-2)+'-'+("0" + date.getDate()).slice(-2);
	//var message = {content: "hey", sender : "can", receiver : "gizem", receivedDate: currentDate };
	//client.send(JSON.stringify(message));

    client.onmessage = function (event) { 
    	if (typeof event.data === "string") {
		var webSocketMessage = JSON.parse(event.data);
        console.log(`${webSocketMessage.sender} : ${webSocketMessage.content}`);
		console.log(`Delivery time : ${webSocketMessage.receivedDate}`);
    	}
    };