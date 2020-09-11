package pl.wsb.Garages;

import pl.wsb.Components.Component;
import pl.wsb.Players.Player;
import pl.wsb.Transactions.Pay;
import pl.wsb.Vehicles.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PPHUAdrian extends Garage {
    public Map<String, Double> prices = new HashMap<String, Double>(Garage.priceMap);
    private Double budgetPriceModifier = 0.7;
    private Double standardPriceModifier = 1.0;
    private Double premiumPriceModifier = 1.3;
    public static final Integer riskOfTotaling = 50;
    // create specific garage
    public PPHUAdrian(String typeOfGarage, Integer riskOfFailure, String name) {
        this.typeOfGarage = typeOfGarage;
        this.riskOfFailure = riskOfFailure;
        this.name = name;
        updatePriceMap();
    }
    // update price map by defined price multiplier
    public void updatePriceMap() {
        for (Map.Entry<String, Double> entry : this.prices.entrySet()) {
            String key = entry.getKey();
            double defaultPriceModifier = 0.75;
            entry.setValue(entry.getValue() * defaultPriceModifier);
        }
    }
    //Method containing logic behind the repairs. Checks if component fits the vehicle or if it needs a repair
    // and handles transactions for these actions.
    public boolean fixThis(Component component, Vehicle vehicle, Player player) {
        Boolean fixFail = false;
        Boolean carTotalled = false;
        Double priceModifier = this.standardPriceModifier;
        Pay thisTransaction = new Pay(player, vehicle);
        //checks if component fits the vehicle
        if (vehicle.segment.equals("budget"))
            priceModifier = this.budgetPriceModifier;
        else if (vehicle.segment.equals("premium"))
            priceModifier = this.premiumPriceModifier;
        Double price = this.prices.get(component.type) * priceModifier;
        // motorcycle price multiplier
        if (vehicle.type.equals("Motorcycle")) {
            price *= 0.9;
        }
        if (!vehicle.components.contains(component)) {
            System.out.println("This component doesn't fit to this vehicle");
            thisTransaction.setTransaction(false, 0.00, "Component doesn't fit.");
            player.addTransaction(thisTransaction);
            return false;
        }
        // checks if damaged
        if (!component.damaged) {
            System.out.println(component.type + " isn't damaged, so no repairs needed");
            thisTransaction.setTransaction(false, 0.00, "No repair needed");
            player.addTransaction(thisTransaction);
            return false;
        }
        if (player.getMoney() < price) {
            System.out.println(player.getName() + " has not enough money to perform repair");
            thisTransaction.setTransaction(false, 0.00, "Not enough money.");
            player.addTransaction(thisTransaction);
            return false;
        }
        //Take into account the risk of failure
        if (ThreadLocalRandom.current().nextInt(this.riskOfFailure) == 1) {
            fixFail = true;
            System.out.println("Oops, we might have screw up repair.");
        }
        // Possibility of totalling a car. Cheapest workshop can total a car and still charges you for it
        carTotalled = ThreadLocalRandom.current().nextInt(PPHUAdrian.riskOfTotaling) == 1;
        if (carTotalled) {
            System.out.println("Sorry, repair was unsuccessful but you have to pay anyway.");
            Vehicle.totalThisVehicle(vehicle);
            thisTransaction.setTransaction(false, price, "Car is totalled.");
            player.subtractMoney(price);
            player.addTransaction(thisTransaction);
            return false;
        }
        //handling of fix failure
        if (fixFail) {
            System.out.println("Sorry, repair was unsuccessful but you have to pay anyway.");
            player.subtractMoney(price);
            thisTransaction.setTransaction(false, price, "Car is totalled.");
            player.addTransaction(thisTransaction);
            return false;
        }
        System.out.println("Don't worry. It'll be repaired.");
        player.subtractMoney(price);
        component.damaged = false;
        component.health = 100.00;
        vehicle.value += vehicle.value * component.priceIncrease;
        thisTransaction.setTransaction(true, price, "Repaired successfully.");
        vehicle.repairHistory.add(this.componentRepairInfo(component.type, price));
        player.addTransaction(thisTransaction);
        return true;
    }



    //Compute cost of repair for a single component
    public Double getPricingForComponent(Vehicle vehicle, Component component) {
        Double basePrice = this.prices.get(component.type);
        if (vehicle.segment.equals("budget"))
            basePrice = basePrice * this.budgetPriceModifier;
        else if (vehicle.segment.equals("premium"))
            basePrice = basePrice * this.premiumPriceModifier;
        if (vehicle.type.equals("Motorcycle")) {
            basePrice = basePrice * 0.9;
        }
        return basePrice;
    }

    // Get cost of repair for the given vehicle (sum of all components repairs)
    public String getPricing(Vehicle veh) {
        Double totalPrice = 0.0;
        StringBuilder msg = new StringBuilder();
        msg.append("Cost of repair:\n");
        for (Component comp : veh.components) {
            if (comp.damaged) {
                msg.append("To fix: " + comp.type + " - " + this.getPricingForComponent(veh, comp));
                totalPrice += this.getPricingForComponent(veh, comp);
            }
        }
        msg.append("Total price is: " + totalPrice);
        return msg.toString();
    }
}
