package pl.wsb.Game;

import pl.wsb.Customers.Customer;
import pl.wsb.Garages.PPHUAdrian;
import pl.wsb.Garages.Garage;
import pl.wsb.Garages.JanuszAuto;
import pl.wsb.Garages.MarianCars;
import pl.wsb.Menu.Menu;
import pl.wsb.Players.Player;
import pl.wsb.Vehicles.Vehicle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

// Set of starting rules for a new game. Handling of IO, interactions and communication with game participants.
public class Game {

    public int numPlayers = 1;
    public ArrayList<Player> players = new ArrayList<>();
    public static Double startingMoney = 150000.00;
    public int numStarterCars = 6;
    public int numStarterClients = 6;
    public static Boolean gameEnded = false;
    public ArrayList<Vehicle> availableVehicles = new ArrayList<>();
    public ArrayList<Customer> availableCustomers = new ArrayList<>();
    public ArrayList<Garage> availableGarages = new ArrayList<>();
    private int currentPlayerId = 0;
    // Get current player.
    public Player getCurrentPlayer() {

        return this.players.get(this.currentPlayerId);
    }

    public Game() {
    }
    // Main part of game logic. Controls launch of menu, turns and if the game's finished
    public Boolean Play(Menu menu, Integer playerId) throws IOException, SQLException {
        this.currentPlayerId = playerId;
        System.out.println(this.getCurrentPlayer().toString());
        this.getCurrentPlayer().processInstallments();
        Boolean endGame = false;
        menu.PrintMenu();
        Integer choice = menu.getChoice();
        Boolean finishTurn = menu.selectAction(choice);
        if (finishTurn) {
            if (this.checkIfWon()) {
                endGame = true;
                System.out.println("And the winner is .......");
                System.out.println(this.getCurrentPlayer().toString());
                System.out.println("Congratulations, you won!");
                return endGame;
            }
            this.players.get(this.currentPlayerId).moveNumber++;
            if (this.currentPlayerId + 1 < this.numPlayers) {
                this.currentPlayerId++;
            }
            else {
                this.currentPlayerId = 0;
            }
            return endGame;
        }

        return endGame;
    }
    // Establishes new game. Initiates players, vehicles, customers and garages accordingly to the set of starting rules.
    public void newGame() throws SQLException, IOException {
        System.out.println("Welcome to Car Dealer Tycoon. A game where everyone can become the greatest Handlarz Janusz of all time.");
        Player.setupPlayers(this, this.numPlayers);
        Vehicle.setupVehicles(this, this.numStarterCars);
        Customer.setupCustomers(this, this.numStarterClients);
        Garage.setDefaultPrices(10000.00, 6000.00, 4500.00, 12500.00, 10250.00);
        this.availableGarages.add(new PPHUAdrian("Cheap", 5, "PPHU Adrian"));
        this.availableGarages.add(new MarianCars("Medium", 10, "Marian Cars Ltd."));
        this.availableGarages.add(new JanuszAuto("Expensive", 0, "JanuszAuto Sp. z o.o."));
        Menu mainMenu = new Menu(this);
        while (!gameEnded) {
            gameEnded = Play(mainMenu, this.currentPlayerId);
        }

    }
    // Get a vehicle by an ID
    public Vehicle getVehicleById(Integer id) {
        for (Vehicle veh : this.availableVehicles) {
            if (veh.id.equals(id)) {
                return veh;
            }
        }
        System.out.println("No vehicle with id: " + id);
        return null;
    }
    // List available vehicles.
    public void listAllVehicles() {
        for (Vehicle vehicle : this.availableVehicles) {
            System.out.println(vehicle.toString());
        }
    }
    // List available customers.
    public String listCustomers() {
        StringBuilder data = new StringBuilder();
        for (Customer customer : this.availableCustomers) {
            data.append(customer.toString()).append("\n");
        }
        return data.toString();
    }
    // List available workshops.
    public String listWorkshops() {
        StringBuilder data = new StringBuilder();
        for (Garage garage : this.availableGarages) {
            data.append(garage.toString()).append("\n");
        }
        return data.toString();
    }
    // Get garage type
    public Garage getGarageByType(String type) {
        for (Garage garage : this.availableGarages) {
            if (garage.typeOfGarage.equals(type)) {
                return garage;
            }
        }
        System.out.println("There is no garage with id: " + type);
        return null;
    }
    // Get customer by an ID
    public Customer getCustomerById(Integer id) {
        for (Customer customer:this.availableCustomers) {
            if (customer.id.equals(id)) {
                return customer;
            }
        }
        return null;
    }
    // Get customer by a name
    public Customer getCustomerByName(String name) {
        for (Customer customer : this.availableCustomers) {
            if (customer.name.equals(name)) {
                return customer;
            }
        }
        System.out.println("There is no customer with name: " + name);
        return null;
    }
    // Print out all available vehicles
    public void printAllVehiclesShort() {
        for (Vehicle vehicle : this.availableVehicles) {
            System.out.println(vehicle.toStringShort());
        }
    }
    // Checks if won
    public Boolean checkIfWon() {
        return this.getCurrentPlayer().getMoney() >= 2 * startingMoney;
    }

}
