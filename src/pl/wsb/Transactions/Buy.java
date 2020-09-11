package pl.wsb.Transactions;

import pl.wsb.Players.Player;
// Declaration of "buy" transaction type
public class Buy extends Transaction{
    public Buy(Player player) {
        this.type = "Buy";
        this.player = player;
    }
}
