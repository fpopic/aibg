var canvas = document.getElementById("myCanvas");
var ctx = canvas.getContext("2d");
var socket = new WebSocket("ws://localhost:8080/");
var size = 20
var blue = new Image();
blue.src="./blue.png"
var red= new Image();
red.src="./red.png";
window.onload = init()
socket.onmessage = onMessage;

var gameHistory = [];
var currentStateNo = -1;
function onMessage(event) {
	var data = JSON.parse(event.data);
	gameHistory.push(data)
	currentStateNo++
	console.log(data)
	clearContext()
	handleState(data)
}
function init() {
	ctx.translate(0.5, 0.5)
}
function clearContext() {
	ctx.clearRect(-0.5, -0.5, canvas.width, canvas.height);
}
function drawAgent(agent, color) {
	var object=agent.object
	if (object != null) {
		ctx.translate(object.x,object.y)
		var angle = Math.atan2(object.vector.j, object.vector.i) - Math.PI/2; 
		ctx.rotate(angle);
		if(color=='blue'){
			ctx.drawImage(blue,-object.radius,-object.radius);
		}
		else if (color=='red'){		
			ctx.drawImage(red,-object.radius,-object.radius);
		}
		ctx.rotate(-angle)
		ctx.translate(-object.x,-object.y)
	}
}
function handleState(state) 
{
	var agent_colors = ["red", "blue"];
	
	var agents = state.agents;

	for(var i = 0; i < agents.length; i++) 
	{
	        var agent_color = agent_colors[i];
    		var agent = agents[i];
    		for(var j = 0; j < agent.length; j++) 
		{

			if (agent[j].alive)
		        	drawAgent(agent[j], agent_color); 
			else
		        	draw(agent[j], 'gray');
		
	
		}

    	}

	for (var i = 0; i < state.asteroids.length; i++)
		drawAsteroid(state.asteroids[i]);
		
	for (var i = 0; i < state.bullets.length; i++)
		draw(state.bullets[i], 'black');
}
function drawAsteroid(object) {
	if (object != null) {
		var asteroid= new Image();
		asteroid.src="./met1.png";
		ctx.drawImage(asteroid,object.x-object.radius,object.y-object.radius,object.radius*2,object.radius*2);
	}
}
function draw(object, color) {
	if (object != null) {
		ctx.fillStyle = color;
		ctx.beginPath();
		ctx.arc(object.x, object.y, object.radius, 0, 2 * Math.PI);
		ctx.closePath();
		ctx.stroke();
		ctx.fill()
	}
}
window.addEventListener("keydown", doKeyDown, false);
function doKeyDown(e) {
	if (socket.readyState === socket.CLOSED) {
		switch (e.keyCode) {
		case 37:
			if (currentStateNo > 0) 
	    		currentStateNo--;
			break;
		case 39:
			if (currentStateNo < gameHistory.length - 1) 
	    		currentStateNo++;
		}
		console.log(currentStateNo)
		
		clearContext()
		handleState(gameHistory[currentStateNo])
	}
}