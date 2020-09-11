package pl.wsb.Transactions;

import pl.wsb.Players.Player;
import pl.wsb.Vehicles.Vehicle;
// declaration od "sell" transaction type
public class Sell extends Transaction {
    public Sell(Player player, Vehicle vehicle) {
        this.type = "Sell";
        this.player = player;
        this.vehicle = vehicle;
    }
}
