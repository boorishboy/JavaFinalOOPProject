package pl.wsb.Advertisement;

import java.util.concurrent.ThreadLocalRandom;

// Basic configuration of newspaper (offline) advert
public class NewspaperAdvert extends Advert {
    public NewspaperAdvert() {
        this.CustomerInterest = ThreadLocalRandom.current().nextInt(1,15);
        this.price = 5500.00;
        this.type = "Newspaper Advert";
    }
}
