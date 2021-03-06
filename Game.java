import java.util.ArrayList;
import java.util.List;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private String item;
    List<String> inv = new ArrayList<String>();
    public boolean Key = false;
    Room livingRoom;
    Room wallRoom;
    Room kitchen;
    Room engineRoom;
    Room cellar;
    Room office;
    Room attic;
    public int totalScore;
    public int pointsAdded;
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room engineRoom, garage, office, bedroom, lounge, kitchen, study, livingRoom, diningRoom, exit, wallRoom, basementStairs, basement, cellar, attic;

        // create the rooms
        engineRoom = new Room("You've reached the engine room. The room in the back right of the building," 
        + " very noticably under the weather.");
        this.engineRoom = engineRoom;
        
        garage = new Room("You found where the cars should be. It's been empty for years it seems, the garage door is"
        + " starting to fall apart.");
        
        office = new Room("You enter a worn down office with newspapers from the 1800s" 
        + " scattered aimlessly.");
        this.office = office;
        
        bedroom = new Room("A bedroom with an oddly cosy looking bed. Don't trust it, though.");
        
        lounge = new Room("You find yourself in the lounge. You could sleep on the couch bed but who knows what"
        + " was there.");
        
        kitchen = new Room("An old kitchen. Look around and you might find "
        + "some nicely aged cheese.");
        this.kitchen = kitchen;
        
        study = new Room("A common study. Except for the abandoned part, as well as that crack in the wall...");
        
        livingRoom = new Room("The door to the living room you entered seems to have lost it's hinges."
        + "There appears to be the exit to your left, but it's locked.");
        this.livingRoom = livingRoom;
        
        diningRoom = new Room("You see an old table with chairs falling apart" 
        + " around it.");
        
        exit = new Room("You've finally made it out. Good Going! Type 'restart' to restart the game, or type 'quit'; to quit.");
        
        wallRoom = new Room("You've found the secret room hidden in the walls..."
        + " And you look on the ground and think you found a way out."); 
        this.wallRoom = wallRoom;
        
        basementStairs = new Room("The stairs creak as you stand on them, and there's candlewax on the stairs.");
        
        basement = new Room("You can barely see, but you can see those wide pipes... \n Maybe you can crawl in one?");
        
        cellar = new Room("You smell well-aged beer and hear drips from the taps.\n Maybe it's best to just drink all the beer and hope you too drunk to be afraid.");
        this.cellar = cellar;
        
        attic = new Room("You arrive in the attic. You see a painting of a door taunting you on the wall. \n Light comes through the ceiling, but you are unsure if you can escape from it.");
        this.attic = attic;
        
        // initialise room exits
        engineRoom.setExit("south", garage);

        garage.setExit("east", lounge);
        garage.setExit("north", engineRoom);

        office.setExit("east", bedroom);

        bedroom.setExit("south", livingRoom);
        bedroom.setExit("west", office);
        bedroom.setExit("north", attic);

        lounge.setExit("west", garage);
        lounge.setExit("south", diningRoom);
        lounge.setExit("east", livingRoom);

        kitchen.setExit("north", livingRoom);
        kitchen.setExit("west", diningRoom);
        kitchen.setExit("south", basementStairs);
        
        study.setExit("east", diningRoom);
        study.setExit("crack", wallRoom);

        livingRoom.setExit("north", bedroom);
        livingRoom.setExit("south", kitchen);
        livingRoom.setExit("west", lounge);
        
        
        diningRoom.setExit("north", lounge);
        diningRoom.setExit("east", kitchen);
        diningRoom.setExit("west", study);
        
        wallRoom.setExit("crack", study);
        
        basementStairs.setExit("south", basement);
        basementStairs.setExit("north", kitchen);

        basement.setExit("north", basementStairs);
        basement.setExit("east", cellar);
        basement.setExit("pipes", engineRoom);

        cellar.setExit("west", basement);
        
        attic.setExit("south", bedroom);
        
        currentRoom = lounge;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Hounted house of ooooOOOOOO0000 scary ghosts!");
        System.out.println("Hounted house of ooooOOOOOO0000 scary ghosts is a new, incredibly rudimantary adventure game.");
        System.out.println("Type 'help' if you ever need help, like a simpleton.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        
        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;
                
            case TAKE:
                goTake();
                break;
                
            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case INV:    
                System.out.println(inv);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around the home.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println("Hope this helps!");        
    }

    public void inventory()
    {
    
    }
    
    public void goTake()
    {   
        
        if (currentRoom == wallRoom){
            pointsAdded = pointsAdded + 125;
            Key = true;
            inv.add("Door Key");
            System.out.println("I think I might have found the way out of here.");
        }
        else if (currentRoom == kitchen){
            pointsAdded = pointsAdded + 100;
            inv.add("Ancient Cheese");
            System.out.println("Hey, at least it's exquisite in taste... I guess.");
        }
        else if (currentRoom == engineRoom) {
            pointsAdded = pointsAdded + 75;
            inv.add("Oil");
            System.out.println("You've found some oil! Wonder if we'll need it.");
        }
        else if (currentRoom == cellar) {
            pointsAdded = pointsAdded + 50;
            inv.add("Beer Barrel");
            System.out.println("Simply intoxicating!");
        }        
        else if (currentRoom == office) {
            pointsAdded = pointsAdded + 75;
            inv.add("Antique Newsaper");
            System.out.println("If the newspaper is to be believed we just" 
            + " won the Civil War.");
        }     
        else if (currentRoom == attic) {
            pointsAdded = pointsAdded + 10;
            inv.add("Creepy Painting");
            System.out.println("A painting gauranteed to lower the property value of" 
             + " any neighborhood it resides on.");
        }        
        else {
        System.out.println("There is nothing to take here!");
        }
    }
    
    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    public void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        int totalScore = getScore();
        
        int i = 0; //tracks room changes
        
        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        Room exit; //initialize Exit
        exit = new Room("You've finally made it out. Good Going! Type  'quit' to quit the game." 
         + "\nYour Score is: " + totalScore + " points.");
        
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            
            if(livingRoom == currentRoom)
            {
                //do what we need to do when in special for living room 
                if (Key == true) {
                    System.out.println("\nGood thing you found a key earlier...");
                    livingRoom.setExit("east", exit);
                    pointsAdded = pointsAdded + i * -5; //Subtract points so it's harder to grind for them
                }   
                else {
                
                }
            }   
               // do what we do for every room
            currentRoom = nextRoom;
            
            pointsAdded = pointsAdded + 10; //Add points for exploring
            i++;
            
            System.out.println(currentRoom.getLongDescription());
        }
    }

    public void setItem(String Item)
    {
        this.item = Item;
    }
    
    public int getScore()
    {
    int score;
    score = 0;
    score = pointsAdded;
    return score;
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
