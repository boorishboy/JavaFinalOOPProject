package pl.wsb;

import pl.wsb.Database.Connector;
import pl.wsb.Game.Game;

import java.io.IOException;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        Connector.connect();

        Game thisGame = new Game();
        thisGame.newGame();

    }
}
