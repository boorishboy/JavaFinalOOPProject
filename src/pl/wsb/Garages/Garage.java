package pl.wsb.Garages;

import pl.wsb.Components.Component;
import pl.wsb.Players.Player;
import pl.wsb.Vehicles.Vehicle;

import java.util.HashMap;
import java.util.Map;

// Basic set of rules constructing a garage.
public abstract class Garage {
    public String typeOfGarage;
    public Integer riskOfFailure;
    public String name;
    public static Map<String, Double> priceMap = new HashMap<String, Double>();


    public abstract String getPricing(Vehicle veh);
    public abstract boolean fixThis(Component component, Vehicle vehicle, Player player);
    public abstract Double getPricingForComponent(Vehicle vehicle, Component component);
    // Create a price map containing default prices
    public static void setDefaultPrices(Double body, Double brakes, Double dampers, Double engine, Double gearbox) {
        priceMap.put("Body", body);
        priceMap.put("Brakes", brakes);
        priceMap.put("Dampers", dampers);
        priceMap.put("Engine", engine);
        priceMap.put("Gearbox", gearbox);
    }
    // returns info about repairs
    public String componentRepairInfo(String componentType, Double price) {
        return this.name + " repair: component - " + componentType + ", price - " + price;
    }

    public String toString() {
        return "Garage type: " + this.typeOfGarage + " , name: " + this.name;
    }
}
