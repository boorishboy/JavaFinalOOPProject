package pl.wsb.Vehicles;

import pl.wsb.Components.Component;
// Declaration of "truck" type vehicle with its features including "load capacity"

public class Truck extends Vehicle {
    public Double load_capacity;

    public Truck(Integer id, Double value, String make, Integer mileage, String colour, String segment, Double load_capacity) {
        this.type = "Truck";
        this.id = id;
        this.value = value;
        this.make = make;
        this.mileage = mileage;
        this.colour = colour;
        this.load_capacity = load_capacity;
        this.segment = segment;
        this.load_capacity = load_capacity;
    }

    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append(this.type + ", ID: " + this.id + ", Value: " + this.value + ", brand: " + this.make +  ", mileage: " + this.mileage + ", color: " + this.colour +
                        ", segment: " + this.segment+ ", load capacity: " + this.load_capacity +"\n");
        for (Component comp : this.components) {
            msg.append(comp.toString()).append("\n");
        }
        return msg.toString();
    }
}