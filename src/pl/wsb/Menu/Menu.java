package pl.wsb.Menu;

import pl.wsb.Advertisement.Advert;
import pl.wsb.Advertisement.OnlineAdvert;
import pl.wsb.Advertisement.NewspaperAdvert;
import pl.wsb.Components.Component;
import pl.wsb.Customers.Customer;
import pl.wsb.Game.Game;
import pl.wsb.Garages.Garage;
import pl.wsb.Players.Player;
import pl.wsb.Vehicles.Vehicle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
// Constructing a menu with all options and logic and interface.
public class Menu {
    private Game game;
    // create array of options
    public Map<Integer, String> optionsDict = new HashMap<Integer, String>();
    ArrayList<String> options = new ArrayList<>();
    // initialize menu
    public Menu(Game game) {
        this.game = game;
        this.BuildMenuOptions();
    }
    // print menu
    public void PrintMenu() {
        for (Map.Entry<Integer, String> menuEntry : this.optionsDict.entrySet()) {
            System.out.println(menuEntry.getKey()+menuEntry.getValue());
        }
    }
    //Building options by adding them to array.
    public void BuildMenuOptions() {
        this.options.add(": Available cars listings");
        this.options.add(": Buy a car");
        this.options.add(": Owned cars lists");
        this.options.add(": Repair a car");
        this.options.add(": Potential clients");
        this.options.add(": Sell a car to potential client");
        this.options.add(": Buy an advertising campaign");
        this.options.add(": See your bank account");
        this.options.add(": See transactions history");
        this.options.add(": See car repair history");
        this.options.add(": Check costs of fixing a car.");
        this.options.add(": Skip turn.");
        for (int key = 1; key < 13; key++) {
            this.optionsDict.put(key, this.options.get(key - 1));
        }
    }

    public void getEnterKey() throws IOException {
        System.in.read();
    }
    // ask for and read user input about final
    public Integer getChoice() {
        Integer choice = 0;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("\nWhat would you like to do?");
                choice = scanner.nextInt();
                scanner.nextLine();
            }
            catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
                continue;
            }
            if (choice < 1 || choice > 13) {
                System.out.println("Invalid  input.");
                continue;
            }
            return choice;
        }
    }
    // a very long switch case handling user input to continue the game
    public Boolean selectAction(Integer choice) throws IOException, SQLException {
        Player currentPlayer = game.getCurrentPlayer();
        Vehicle playerVehicle;
        Garage thisGarage;
        Customer thisCustomer;
        Component thisComponent;
        Boolean finishTurn = false;
        switch (choice) {
            // List all available vehicles
            case 1:
                System.out.println("All available vehicles: \n");
                game.listAllVehicles();
                System.out.println("Press enter to continue.");
                finishTurn = false;
                break;

            case 2:
                game.printAllVehiclesShort();
                playerVehicle = this.userInputGetFromAllVehicles();
                if (playerVehicle == null) {
                    System.out.println("Press enter to continue.");
                    finishTurn = false;
                    break;
                }
                if (currentPlayer.buyVehicle(playerVehicle)) {
                    game.availableVehicles.remove(playerVehicle);
                    System.out.println("Press enter to continue.");
                    finishTurn = true;
                    break;
                }
                finishTurn = false;
                break;
            case 3:
                System.out.println("These are all your cars:");
                System.out.println(currentPlayer.listOwnedCars());
                System.out.println("Press enter to continue.");
                finishTurn = false;
                break;
            case 4:
                System.out.println(game.listWorkshops());
                currentPlayer.printVehiclesShort();
                playerVehicle = this.inpGetPlayersVeh(currentPlayer);
                if (playerVehicle == null) {
                    System.out.println("Failed to get vehicle");
                    finishTurn = false;
                    break;
                }
                thisComponent = this.userInputGetVehComponent(playerVehicle);
                if (thisComponent == null) {
                    System.out.println("Failed to get component");
                    finishTurn = false;
                    break;
                }
                for (Garage garage : game.availableGarages) {
                    String message = garage.name + ", garage type: " + garage.typeOfGarage +", to fix " + thisComponent.type +
                            " will cost " + garage.getPricingForComponent(playerVehicle, thisComponent);
                    System.out.println(message);
                }
                thisGarage = this.userInputSelectGarage();
                if (thisGarage == null) {
                    System.out.println("Failed to get garage");
                    finishTurn = false;
                    break;
                }
                if (thisGarage.fixThis(thisComponent, playerVehicle, currentPlayer)) {
                    System.out.println("Success");
                } else {
                    System.out.println("Failure");
                }
                finishTurn = true;
                break;
            case 5:
                System.out.println("Here are potential buyers:");
                System.out.println(game.listCustomers());
                System.out.println("Press enter to continue.");
                finishTurn = false;
                break;
            case 6:

                currentPlayer.printVehiclesShort();
                playerVehicle = this.inpGetPlayersVeh(currentPlayer);
                if (playerVehicle == null) {
                    System.out.println("Unable to get vehicle");
                    finishTurn = false;
                    break;
                }
                System.out.println(game.listCustomers());
                thisCustomer = this.inpGetCustomer();
                if (thisCustomer == null) {
                    System.out.println("Unable to get customer");
                    finishTurn = false;
                    break;
                }
                finishTurn = currentPlayer.sellVehicle(playerVehicle, thisCustomer);
                break;
            case 7:
                //Select and buy advertisement
                Advert thisAd = userInputGetAd();
                if (thisAd == null) {
                    finishTurn = false;
                    break;
                }
                if (!thisAd.buyAnAd(currentPlayer)) {
                    finishTurn = false;
                    break;
                }
                System.out.println(thisAd.CustomerInterest + " new customers are available thanks to this campaign.");
                Customer.setupCustomers(game, thisAd.CustomerInterest);
                finishTurn = true;
                break;
            case 8:
                //Check Players money balance
                System.out.println("Your account balance is: " +  currentPlayer.getMoney());
                System.out.println("Press enter to continue.");
                finishTurn = false;
                break;
            case 9:
                //Get Players transaction history
                System.out.println(currentPlayer.getTransactionHistory());
                finishTurn = false;
                break;
            case 10:
                //CheckRepairHist
                currentPlayer.printVehiclesShort();
                Vehicle veh = inpGetPlayersVeh(currentPlayer);
                if (veh == null) {
                    System.out.println("You don't own this vehicle");
                    finishTurn = false;
                    break;
                }
                System.out.println(veh.getRepairHistory());
                finishTurn = false;
                break;
            case 11:
                thisGarage = userInputSelectGarage();
                currentPlayer.printVehiclesShort();
                playerVehicle = inpGetPlayersVeh(currentPlayer);
                String msg = thisGarage.getPricing(playerVehicle);
                System.out.println(msg);
                finishTurn = false;
                break;
            case 12:
                finishTurn = true;
                break;
        }
        this.getEnterKey();
        return finishTurn;
    }
    //player buying an ad. Gives him a chance to choose what kind of advertisement he wants and continues to transaction and execution of the order.
    public Advert userInputGetAd() {
        System.out.println("What kind of advertisement do you want? Campaign in a newspaper can get you up to 15 new customers for 5500.\n" +
                "Online advertisement can get you 1 new customer, however its cheaper - only 1240. \n['Newspaper' or 'Online']");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if (choice.equals("Newspaper")) {
            return new NewspaperAdvert();
        }
        else if (choice.equals("Online")) {
            return new OnlineAdvert();
        }
        else {
            return null;
        }
    }
    // Reading user input to select a car by id
    public Vehicle inpGetPlayersVeh(Player player) {
        System.out.println("Select a car. Please enter an id");
        Scanner scanner = new Scanner(System.in);
        try {
            return player.getVehicleById(scanner.nextInt());
        }
        catch (InputMismatchException e) {
            return null;
        }

    }
    // Reading user input to select a customer by name or id.
    public Customer inpGetCustomer() {
        System.out.println("Select customer. Please enter a name or id.");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        try {
            return game.getCustomerById(Integer.parseInt(choice));
        } catch (NumberFormatException e) {
            return game.getCustomerByName(choice);
        }
    }
    // Reading user input to select a workshop.
    public Garage userInputSelectGarage() {
        System.out.println("Select workshop: [Cheap, Medium or Expensive]");
        Scanner scanner = new Scanner(System.in);
        return game.getGarageByType(scanner.next());
    }
    // Reading user input to select a vehicle by id
    public Vehicle userInputGetFromAllVehicles() {
        System.out.println("Select a car. Please enter an id");
        Scanner scanner = new Scanner(System.in);
        try {
            return game.getVehicleById(scanner.nextInt());
        }
        catch (InputMismatchException e) {
            return null;
        }

    }
    // Reading user input to select component
    public Component userInputGetVehComponent(Vehicle veh) {
        System.out.println(veh.toString());
        System.out.println("Select component: [Body, Brakes, Dampers, Engine or Gearbox]");
        Scanner scanner = new Scanner(System.in);
        return veh.getComponentByName(scanner.next());
    }


}
