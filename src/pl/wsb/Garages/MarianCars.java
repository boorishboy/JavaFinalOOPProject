package pl.wsb.Garages;

import pl.wsb.Components.Component;
import pl.wsb.Players.Player;
import pl.wsb.Transactions.Pay;
import pl.wsb.Vehicles.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MarianCars extends Garage {
    public Map<String, Double> prices = new HashMap<String, Double>(Garage.priceMap);
    private Double budgetPriceMultiplier = 1.1;
    private Double standardPriceMultiplier = 1.3;
    private Double premiumPriceMultiplier = 1.7;

    // create specific garage
    public MarianCars(String typeOfGarage, Integer riskOfFailure, String name) {
        this.typeOfGarage = typeOfGarage;
        this.riskOfFailure = riskOfFailure;
        this.name = name;
        updatePriceMap();
    }
    // update price map by defined price multiplier
    public void updatePriceMap() {
        for (Map.Entry<String, Double> entry : this.prices.entrySet()) {
            String key = entry.getKey();
            double defaultPriceModifier = 1.5;
            entry.setValue(entry.getValue() * defaultPriceModifier);
        }
    }
    //Method containing logic behind the repairs. Checks if component fits the vehicle or if it needs a repair
    // and handles transactions for these actions.
    public boolean fixThis(Component component, Vehicle vehicle, Player player) {
        Boolean fixFail = false;
        Double priceModifier = this.standardPriceMultiplier;
        Pay thisTransaction = new Pay(player, vehicle);
        // checking segment of a vehicle and setting price multiplier accordingly
        if (vehicle.segment.equals("budget"))
            priceModifier = this.budgetPriceMultiplier;
        else if (vehicle.segment.equals("premium"))
            priceModifier = this.premiumPriceMultiplier;
        Double price = this.prices.get(component.type) * priceModifier;
        if (vehicle.type.equals("Motorcycle")) {
            price *= 0.9;
        }
        //checks if component fits the vehicle
        if (!vehicle.components.contains(component)) {
            System.out.println("This component doesn't fit to this vehicle");
            thisTransaction.setTransaction(false, 0.00, "Component doesn't fit.");
            player.addTransaction(thisTransaction);
            return false;
        }
        if (!component.damaged) {
            System.out.println(component.type + " isn't damaged, so no repairs needed");
            thisTransaction.setTransaction(false, 0.00, "No repair needed");
            player.addTransaction(thisTransaction);
            return false;
        }
        // transaction refusal. if player doesn't have enough money, transaction is being refused
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
        // repair failure
        if (fixFail) {
            System.out.println("Sorry, repair was unsuccessful but you have to pay anyway.");
            player.subtractMoney(price);
            thisTransaction.setTransaction(false, price, "Car totalled.");
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
            basePrice = basePrice * this.budgetPriceMultiplier;
        else if (vehicle.segment.equals("premium"))
            basePrice = basePrice * this.premiumPriceMultiplier;
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
