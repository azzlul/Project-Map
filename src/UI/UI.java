package UI;

import Exceptions.RepositoryException;
import Exceptions.ServiceException;
import Exceptions.UIException;
import Exceptions.ValidatorException;
import Service.ServiceUser;

import java.util.Scanner;

/**
 * Class that handles interface.
 */
public class UI {
    /**
     * Service for users.
     */
    ServiceUser srv;

    /**
     * Constructor for class.
     * @param srv user server
     */
    public UI(ServiceUser srv) {
        this.srv = srv;
    }

    /**
     * Prints all possible commands.
     */
    private void printInstructions(){
        System.out.println("Aplicatie management retea de socializare:");
        System.out.println("add- Add user");
        System.out.println("rem- Remove user");
        System.out.println("list- List users");
        System.out.println("addp- Add friendship");
        System.out.println("remp- Remove friendship");
        System.out.println("listp- List friendships");
        System.out.println("cmax- biggest community");
        System.out.println("count- number of communities");
        System.out.println("help- print instructions");
        System.out.println("exit- Exit application");
    }

    /**
     * Reads the user input and adds a user to repository.
     */
    private void addUser(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Insert name:");
        String name = sc.nextLine();
        srv.addUser(name);
        System.out.println("User added!");
    }

    /**
     * Runs the command specified in the given string.
     * If command is not found, a message is printed.
     * @param command command
     */
    private void findAndRunCommand(String command){
        switch (command) {
            case "add" -> addUser();
            case "addp" -> addFriendship();
            case "rem" -> removeUser();
            case "remp" -> removeFriendship();
            case "help" -> printInstructions();
            case "list" -> listUsers();
            case "listp" -> listFriendships();
            case "cmax" -> maxCommunity();
            case "count" -> communityCount();
            default -> System.out.println("Invalid command");
        }
    }

    /**
     * Function that tries to read an integer.
     * @throws UIException if it fails
     * @param sc Scanner
     * @return Integer
     */
    private int readInt(Scanner sc){
        try{
            return Integer.parseInt(sc.nextLine());
        }
        catch(NumberFormatException e){
            throw new UIException("Invalid number");
        }
    }

    /**
     * Reads the user input and adds a friendship.
     */
    private void addFriendship(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Insert first user ID:");
        int firstUserID = readInt(sc);
        System.out.print("Insert second user ID:");
        int secondUserID = readInt(sc);
        srv.addFriendship(firstUserID, secondUserID);
        System.out.println("Friendship added!");
    }

    /**
     * Reads the user input and removes the user with the given ID.
     */
    private void removeUser(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Insert user ID:");
        int userID = readInt(sc);
        srv.removeUser(userID);
        System.out.println("User removed!");
    }

    /**
     * Prints all users.
     */
    private void listUsers(){
        if(srv.sizeUsers() > 0) {
            for (var user : srv.findAllUsers()) {
                System.out.println(user);
            }
        }
        else System.out.println("No users found!");
    }

    /**
     * Prints all friendships.
     */
    private void listFriendships(){
        if(srv.sizeFriendships() > 0) {
            for (var friendship : srv.findAllFriendships()) {
                var user1 = srv.findUser(friendship.getFirstUserID());
                var user2 = srv.findUser(friendship.getSecondUserID());
                System.out.println(user1.getName() +
                        "(ID: " + user1.getId() + ") is friends with " + user2.getName() +
                        "(ID: " + user2.getId() + ")  (FriendshipID:" + friendship.getId() + ")");
            }
        }
        else System.out.println("No friendships found!");
    }

    /**
     * Reads the user input and removes the friendship with the given ID.
     */
    private void removeFriendship(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Insert friendship ID:");
        int friendshipID = readInt(sc);
        srv.removeFriendship(friendshipID);
        System.out.println("Friendship removed!");
    }

    /**
     * Prints the number of communities.
     */
    private void communityCount(){
        System.out.println("Community count:" + srv.allCommunities());
    }

    /**
     * Prints the biggest community.
     */
    private void maxCommunity(){
        System.out.println("Max community:\n" + srv.maxCommunity());
    }

    /**
     * Stars the ui.
     */
    public void runUI(){
        var scanner = new Scanner(System.in);
        printInstructions();
        while(true){
            var command = scanner.nextLine();
            if(command.equals("exit")){
                break;
            }
            try{
                findAndRunCommand(command);
            }
            catch (RepositoryException | ValidatorException | ServiceException | UIException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
