const client = new WebSocket("ws://localhost:8080/WebSocketApp//chat");
client.onopen = function (event) {
        console.log("Connected");
    };
	//var message = {content: "hey", sender : "can", receivedDate: Date(Date.now()) };
	//client.send(JSON.stringify(message));

    client.onmessage = function (event) { 
		var webSocketMessage = JSON.parse(event.data);
        console.log(`${webSocketMessage.sender} : ${webSocketMessage.content}`);
		console.log(`Delivery time : ${webSocketMessage.receivedDate}`);
    };