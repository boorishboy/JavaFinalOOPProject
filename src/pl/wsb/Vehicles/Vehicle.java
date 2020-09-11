package pl.wsb.Vehicles;

import pl.wsb.Components.*;
import pl.wsb.Database.Connector;
import pl.wsb.Game.Game;
import pl.wsb.Components.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
// Basic vehicle configuration, also handling database querying.
public abstract class Vehicle {
    public String type;
    public Integer id;
    public Double value;
    public String make;
    public Integer mileage;
    public String colour;
    public String segment;
    public Set<Component> components = new HashSet<>();
    public ArrayList<String> repairHistory = new ArrayList<>();

    public String toString() {
        StringBuilder message = new StringBuilder(this.type + ", ID: " + this.id + ", Value: " + this.value + ", brand: " + this.make +  ", mileage: " + this.mileage + ", color: " + this.colour +
                ", segment: " + this.segment + "\n");
        message.append("Components:\n");
        for (Component comp : this.components) {
            message.append(comp.toString()).append("\n");
        }
        return message.toString();
    }

    // Query database for initial vehicles
    public static void setupVehicles(Game thisGame, Integer numberOf) throws SQLException {
        Integer allCars = Connector.getNumRows("allvehicles");
        Set<Integer> ids = new HashSet<>();
        while (ids.size() != numberOf) {
            ids.add(ThreadLocalRandom.current().nextInt(1, allCars));
        }
        for (Integer id : ids) {
            String sql = "Select * from allvehicles where allvehicles.id = " + id;
            ResultSet res = Connector.executeQuery(sql);
            res.next();
            Integer vehId = res.getInt("id");
            String vehType = res.getString("type");
            Double value = res.getDouble("value");
            String make = res.getString("make");
            Integer mileage = res.getInt("mileage");
            String color = res.getString("Color");
            String segment = res.getString("segment");
            Vehicle thisVehicle;

            // Check type of vehicle and create appropriate object
            if (vehType.equals("Car")) {
                thisVehicle = new Car(vehId, value, make, mileage, color, segment);
            }
            else if (vehType.equals("Truck")) {
                thisVehicle = new Truck(vehId, value, make, mileage, color, segment, ThreadLocalRandom.current().nextDouble(1000.00, 2800.00));
            }
            else {
                thisVehicle = new Motorcycle(vehId, value, make, mileage, color, segment);
            }

            //Add components
            Component.addComponents(thisVehicle);
            thisGame.availableVehicles.add(thisVehicle);
        }
    }
    // Get and return repair history for given vehicle
    public String getRepairHistory() {
        StringBuilder history = new StringBuilder();
        for (String message : this.repairHistory) {
            history.append(message).append("\n");
        }
        return history.toString();
    }
    // Acquire component by its name
    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (component.type.equals(name)) {
                return component;
            }
        }
        return null;
    }

    public static void totalThisVehicle(Vehicle vehicle) {
        for (Component component : vehicle.components) {
            component.health = 0.50;
            component.damaged = true;
        }
        vehicle.value = 0.0;
    }

    public String toStringShort() {
        Boolean isDamaged = false;
        for (Component component : this.components) {
            if (component.damaged) {
                isDamaged = true;
                break;
            }
        }
        return this.type + "ID: " + this.id + ". Make: " + this.make + ". Value: " + this.value +". Damaged: " + isDamaged;
    }
}
