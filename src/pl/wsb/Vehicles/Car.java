package pl.wsb.Vehicles;
// Declaration of "car" type vehicle with its features
public class Car extends Vehicle {
    public Car(Integer id, Double value, String make, Integer mileage, String color, String segment) {
        this.id = id;
        this.value = value;
        this.make = make;
        this.mileage = mileage;
        this.colour = color;
        this.segment = segment;
        this.type = "Car";
    }

    public String toString() {
        return super.toString();
    }
}
