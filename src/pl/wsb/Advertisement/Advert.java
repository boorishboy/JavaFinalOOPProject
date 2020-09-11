package pl.wsb.Advertisement;

import pl.wsb.Players.Player;
import pl.wsb.Transactions.Buy;
import pl.wsb.Transactions.Transaction;

// Basic advertisement configuration and actions including transaction handling for buying an advert.
public abstract class Advert {
    public String type;
    public Double price;
    public Integer CustomerInterest;

    // Method handling buying an ad. Establishes new transaction, and checks if player has enough money and if not
    // refuses the transaction. Else processes the transactions further.
    public Boolean buyAnAd(Player player) {
        Transaction thisTransaction = new Buy(player);
        thisTransaction.setAd(this);
        if (this.price > player.getMoney()) {
            System.out.println(player.getName() + " has not the required amount of money.");
            return false;
        }
        player.subtractMoney(this.price);
        player.addTransaction(thisTransaction);
        return true;
    }
    public String toString() {
        return "Type: " + this.type + ", " + "Price: " +  this.price + "Customer reach out: " + this.CustomerInterest;
    }
}
