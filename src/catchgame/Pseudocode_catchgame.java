package catchgame;

/*
 
 //////////// Catch ////////////

Catch launches the login pane that is passed event handlers to handle logging in
The login event handler sends a LoginPacket to the server
The launchServer event handler launches the ServerPane and the Server
The server has a while true that is called HandleConnectionsTask
If a client connects, it will launch a HandleAClientTask
That will:
	listen for LoginPackets
	reply With ServerCodePackets
If the username and password are correct, the client receives 
the ServerCode packet for logging in and the CatchServer launches
a ClientSideGameControl object
When the client logs in, a GameControl object is launched
GameControl has a main pane that contains both navigation buttons 
and a pane that can be switched in or out
The three areas in the game are:
	View Resources
	Go Fishing
	Go to the Market
Each has a pane that can be loaded into the main pane
For the Fishing Pane, catch communicates with the server, sending
SubOceanFishStatePackets
Then Server sends back the appropriate FishPacketsPacket
Whenever a packet is added to the ClientSubOcean, for each fish a Circle
is given that's x and y are random numbers that lie on the pane,
and the circle is added to the SimpleFishingPane gui
When the Extract Fish button is clicked, the last fish in the Ocean's
ArrayList for cod is removed, and it's Circle is removed from the view

 //////////// Ocean ////////////
  
 Ocean contains array list for the six populations of sea creatures
 
 It let's you create a sea creature with a random weight
 
 It lets you add a bunch of sea creatures by calling the previous method 
 in a for loop
 
 it let's you fill the oceanInitially by adding a bunch of seacreatures
 up to the initial amount
 
 it has methods for creating packte's of fish based on both the 
 Ocean's and the ClientSubOcean's conditions
 
 These fish are taken from the Ocean's populations,
 so there must be a way to regenerate the populations
 
 This done with regnerateOcean, which launches a thread
 that is run with a timer
 Right now, every five seconds, it adds fish based on a formula
 from Wikipedia about population growth based on
 initial population, max population, and time
*/