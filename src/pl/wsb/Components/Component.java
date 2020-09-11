package pl.wsb.Components;

import pl.wsb.Vehicles.Vehicle;

import java.util.concurrent.ThreadLocalRandom;

// Generic car component (engine, body, brakes, etc.)
public abstract class Component {
    public String type;
    public Boolean damaged;
    public Double priceIncrease;
    public Double health;

    // Compose a car from random components. Randomly generates each one of the components.
    public static void addComponents(Vehicle vehicle) {
        vehicle.components.add(new Body(ThreadLocalRandom.current().nextDouble(18.00, 100.00)));
        vehicle.components.add(new Brakes(ThreadLocalRandom.current().nextDouble(55.00, 100.00)));
        vehicle.components.add(new Dampers(ThreadLocalRandom.current().nextDouble(25.00, 100.00)));
        vehicle.components.add(new Engine(ThreadLocalRandom.current().nextDouble(45.00, 100.00)));
        vehicle.components.add(new Gearbox(ThreadLocalRandom.current().nextDouble(15.00, 100.00)));
    }

    public String toString() {
        return "Component type: " + this.type + ", " + "damaged: " + this.damaged + ", " + "car value increased if fixed: " + this.priceIncrease * 100.00;
    }
}
