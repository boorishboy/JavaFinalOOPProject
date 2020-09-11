package pl.wsb.Players;

import pl.wsb.Components.Component;
import pl.wsb.Customers.Customer;
import pl.wsb.Game.Game;
import pl.wsb.Transactions.Buy;
import pl.wsb.Transactions.Installment;
import pl.wsb.Transactions.Sell;
import pl.wsb.Transactions.Transaction;
import pl.wsb.Vehicles.Vehicle;

import java.util.ArrayList;
import java.util.Scanner;

// Game participant basic definitions and actions.

public class Player {
    public int id;
    private String name;
    //Create an array to store players vehicles
    private ArrayList<Vehicle> Vehicles = new ArrayList<>();
    //Create an array to store players transactions
    private ArrayList<Transaction> Transactions = new ArrayList<>();
    //Create an array to store players installments
    private ArrayList<Installment> Installments = new ArrayList<>();
    public ArrayList<Transaction> getTransactions() {

        return Transactions;
    }

    private Double Money;
    public Integer moveNumber = 0;
    // Define players basic information such as name, id and money.
    public Player(String name, Double money, int id) {
        this.id = id;
        this.name = name;
        Money = money;
    }

    // Assign a vehicle to player
    public void addVehicle(Vehicle vehicle) {
        if (!this.Vehicles.contains(vehicle)) {
            this.Vehicles.add(vehicle);
            System.out.println(vehicle.toString() + " has been added.");
        }
        else {
            System.out.println("You already own this vehicle: " + vehicle.toString());
        }
    }
    // Confirm and process installments for player
    public void processInstallments() {
        for (Installment installment : this.Installments) {
            if (installment.quantity > 0) {
                installment.quantity -= 1;
                System.out.println("Processing installment " + (installment.quantity - 10) + " out of 10, value: " + installment.value / 10);
                this.addMoney(installment.value / 10);
            }
        }
    }
    // subtract money from the player and return account balance after transaction
    public void subtractMoney(Double money) {
        System.out.println("Subtracting " + money + " from account value " + this.Money);
        this.Money -= money;
        System.out.println("Current balance: " + Money);
    }
    // Add money to the player and return account balance after transaction
    public void addMoney(Double money) {
        System.out.println("Adding " + money + " to account value " + this.Money);
        this.Money += money;
        System.out.println("Current balance: " +  this.Money);
    }

    public String getName() {

        return name;
    }

    public Double getMoney() {

        return Money;
    }
    // Player setup. Adding players accordingly to defined number of them and read user input to assign them a name
    public static void setupPlayers(Game thisGame, Integer numberOfPlayers) {
        Scanner scanner = new Scanner(System.in);
        String name;
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("Player number: " + (i+1) + " Your name please:");
            name = scanner.next();
            Player player = new Player(name, Game.startingMoney, i);
            thisGame.players.add(player);
        }
    }
    // Logic behind selling a vehicle. Checks if offered vehicle matches the type wanted and if the offered vehicle is damaged.
    // It also handles selling for cash or by a credit, both with taxes.
    public Boolean sellVehicle(Vehicle vehicle, Customer i) {
        Sell thisTransaction = new Sell(this, vehicle);
        Double sellTax = vehicle.value * 0.02;
        if (!i.typeWanted.equals(vehicle.type)) {
            System.out.println("Customer doesn't want " + vehicle.type + ", he/she want" + i.typeWanted);
            return false;
        }
        for (Component comp : vehicle.components) {
            if (comp.damaged && !i.BuyDamaged) {
                System.out.println("Customer will not buy a vehicle that needs repairs and parts replacement");
                return false;
            }
        }
        if (vehicle.value <= i.cash) {
            this.Vehicles.remove(vehicle);
            thisTransaction.setTransaction(true, vehicle.value + sellTax, "Vehicle has been sold to " + i.name);
            this.addTransaction(thisTransaction);
            this.addMoney(vehicle.value - sellTax);
            return true;
        }
        else {
            System.out.println("Customer doesn't have enough money. He/She will buy it in 10 installments");
            this.subtractMoney(sellTax);
            Installment thisInstallment = new Installment(vehicle.value, 10);
            this.Installments.add(thisInstallment);
            thisTransaction.setTransaction(true, vehicle.value + sellTax, "Vehicle has been sold in 10 installments to " + i.name);
            this.Vehicles.remove(vehicle);
            return true;
        }
    }
    // Logic behind operation of buying a vehicle.
    public Boolean buyVehicle(Vehicle vehicle) {
        Buy thisTransaction = new Buy(this);
        // Add tax to price of a vehicle and get total price.
        Double totalPrice = vehicle.value + (vehicle.value * 0.02);
        // Return total price with the amount of tax to pay
        System.out.println("Vehicle price is: " + vehicle.value + ", you will also pay tax: " + (vehicle.value * 0.02));
        System.out.println("For a total price of: " +  totalPrice);
        // Check if player can afford the vehicle. If true process the transaction by subtracting the money and assign
        // the vehicle to the player and return confirmation.
        // Otherwise refuse transaction and return notification about transaction refusal.
        if (totalPrice <= this.Money) {
            this.addVehicle(vehicle);
            this.subtractMoney(totalPrice);
            thisTransaction.setVehicle(vehicle);
            thisTransaction.setTransaction(true, totalPrice, "Purchase has been successful");
            this.addTransaction(thisTransaction);
            return true;
        }
        else {
            System.out.println("You don't have enough money to buy this car");
            return false;
        }
    }

    // List owned cars. If none owned return notification. Else return string of owned cars.
    public String listOwnedCars() {
        System.out.println("These are my cars: ");
        StringBuilder data = new StringBuilder();
        if (this.Vehicles.size() == 0) {
            return "You don't have any vehicles.";
        }
        for (Vehicle vehicle : this.Vehicles) {
            data.append(vehicle.toString());
        }
        return data.toString();
    }

    public String toString() {
        return "Player number: " + (this.id+1) + " Name: " + this.name + ". Account balance: " + this.Money +". Move count: " +this.moveNumber;
    }

    public Vehicle getVehicleById(Integer id) {
        for (Vehicle vehicle : this.Vehicles) {
            if (vehicle.id.equals(id)) {
                return vehicle;
            }
        }
        System.out.println("You didn't win a vehicle with ID: " + id);
        return null;
    }

    public void addTransaction(Transaction thisTransaction) {

        this.Transactions.add(thisTransaction);
    }
    // Return transaction history
    public String getTransactionHistory() {
        StringBuilder data = new StringBuilder();
        for (Transaction transaction : this.Transactions) {
            data.append(transaction.toString()).append("\n");
        }
        return data.toString();
    }
    // return short vehicles strings
    public void printVehiclesShort() {
        for (Vehicle vehicle : this.Vehicles) {
            System.out.println(vehicle.toStringShort());
        }
    }
}
