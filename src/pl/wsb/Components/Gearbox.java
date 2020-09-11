package pl.wsb.Components;

// Basic configuration of Gearbox component
public class Gearbox extends Component {

    public Gearbox(Double health) {
        this.priceIncrease = 0.50;
        this.health = health;
        this.damaged = health < 60.00;
        this.type = "Gearbox";
    }
}
