package pl.wsb.Customers;

import pl.wsb.Database.Connector;
import pl.wsb.Game.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

// Creates new customer with basic setup and actions.
public class Customer {
    public Integer id;

    public void setId(Integer id) {

        this.id = id;
    }

    public String name;
    public Double cash;
    public String typeWanted;
    public Boolean BuyDamaged;
    // Creation of new client.
    public Customer(String name, Double cash, String typeWanted, Boolean BuyDamaged) {
        this.name = name;
        this.cash = cash;
        this.typeWanted = typeWanted;
        this.BuyDamaged = BuyDamaged;
    }
    // Randomized setup of new customer, with all variables such as name, money and what he's looking for.
    // It queries required data from database in random manner.
    public static void setupCustomers(Game thisGame, Integer totalAmount) throws SQLException {
        Integer allClients = Connector.getNumRows("clients");
        Set<Integer> ids = new HashSet<>();
        while (ids.size() != totalAmount) {
            Integer id = ThreadLocalRandom.current().nextInt(1, allClients);
            if (!thisGame.availableCustomers.contains(thisGame.getCustomerById(id))) {
                ids.add(id);
            }
        }

        for (Integer id : ids) {
            String sql = "Select * from clients where clients.id = "+ id;
            ResultSet res = Connector.executeQuery(sql);
            res.next();
            String name = res.getString("name");
            Double cash = res.getDouble("cash");
            String wantedType = res.getString("wantedType");
            Boolean buyDamaged = res.getBoolean("BuyDamaged");

            Customer thisCustomer = new Customer(name, cash, wantedType, buyDamaged);
            thisCustomer.setId(id);
            thisGame.availableCustomers.add(thisCustomer);
        }
    }

    public String toString() {
        return "Hello, my name is " + this.name + ". I am a potential buyer. I have" + this.cash +" to spend on: " + this.typeWanted +
                "It is that likely: " + this.BuyDamaged + " that I will buy a damaged vehicle. ID: " + this.id;
    }
}
