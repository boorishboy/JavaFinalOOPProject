package pl.wsb.Transactions;

import pl.wsb.Advertisement.Advert;
import pl.wsb.Players.Player;
import pl.wsb.Vehicles.Vehicle;
// Basic transaction configuration.
public class Transaction {
    public String type;
    public Boolean successful;
    public Double value;
    public Player player;
    public Vehicle vehicle;
    public String message;
    public Advert ad;

    public void setTransaction(Boolean successful, Double value, String message) {
        this.successful = successful;
        this.value = value;
        this.message = message;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Transaction type: " + this.type + ". Was successful: " + this.successful + ". Value: " + this.value +". For:\n" );
        if (this.vehicle != null) {
            str.append(this.vehicle.toStringShort());
        }
        if (this.ad != null) {
            str.append(this.ad.toString());
        }
        return str.toString();
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setAd(Advert ad) {
        this.ad = ad;
    }
}
