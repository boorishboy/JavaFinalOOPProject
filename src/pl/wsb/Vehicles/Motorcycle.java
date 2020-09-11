package pl.wsb.Vehicles;
// Declaration of "motorcycle" type vehicle with its features

public class Motorcycle extends Vehicle {
    public Motorcycle(Integer id, Double value, String make, Integer mileage, String colour, String segment) {
        this.type = "Motorcycle";
        this.id = id;
        this.value = value;
        this.make = make;
        this.mileage = mileage;
        this.colour = colour;
        this.segment = segment;
    }

    public String toString() {
        return super.toString();
    }
}
