package catchgame;

/*
Our resource game's code is divided into 4 packages ecluding the test packages:
	catchgame
	market
	resources
	userinterface
	
catchgame
	catchgame contains our application class, the start method, and main, all in class Catch
	all the other files in the package help out Catch directly or indirectly
	on the one hand, we have the CatchServer, and on the other hand we have the client
	all players play in the client, which is launched in the login function
	the server has Ocean
	the client has ClientSubOcean
	a client will load a player once logged in 
	(log-in implemented, loading player currently not)
	the server and client communicate by sending packets:
		FishPacketPackets
		ServerCodePackets
		SubOceanFishStatePacket
	Once logged in, for a given player, there is GameControl in catch,
	and a server task in the server with ServerSideGameControl
	In the end, we want all oceans to inherit from abstract ocean
	There is also a ServerCodeConstants class that just has the standardized codes
market
	the markets will let you sell an item for a given price,
	or buy an item for a given price
resources
	SeaCreature is the abstract base class for all other sea creatures
	It is of type T which is a species
	Fish and Shellfish extend SeaCreature
	They each take the value of an enum,
	FishSpecies for Fish,
	and ShellfishSpecies for Shellfish
	Shellfish or sold in a ShellfishBushel
	The equipment come in three types, each that extend abstract base equipment
		Boat
		SimpleFishingItem
		LobsterTrap
	There is an enum for their usages too in Usage
	Like with SeaCreature's descendants, there are Enums for Boat and
	SimpleFishingItem, BoatTypes, and SimpleFishingItemTypes
userinterface
	userinterface is for only user interface stuff it contains panes
	that can be loaded and can be passed EventHandlers from where they are instantiated
	
Please see the pseudocode PseudoCode_catchgame for more info on Catch and Ocean
*/