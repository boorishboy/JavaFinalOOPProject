package pl.wsb.Transactions;

import pl.wsb.Players.Player;
import pl.wsb.Vehicles.Vehicle;
 // Declaration of payment action
public class Pay extends Transaction {
    public Pay(Player player, Vehicle vehicle) {
        this.type = "Pay";
        this.player = player;
        this.vehicle = vehicle;
    }
}
