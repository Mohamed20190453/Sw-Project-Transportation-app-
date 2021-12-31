import java.util.ArrayList;

public class Ride {

    private String source;
    private String destination;
    private Passenger passenger;
    private Driver driver;
    private Double price;
    private double priceAfterDiscount;
    private int numberOfPassengers;
    private ArrayList<Offer> offers = new ArrayList<>();
    private SystemData data = DataArrays.getInstance();
    private ArrayList<Event> events = new ArrayList<>();

    public Ride() {}

    public Ride(String source, String destination, Passenger passenger, int numberOfPassengers) {
        this.source = source;
        this.destination = destination;
        this.passenger = passenger;
        this.price = null;
        this.driver = null;
        this.numberOfPassengers = numberOfPassengers;

        ArrayList<Driver> drivers = data.getDrivers();
        for (Driver driver : drivers) {
            if (driver.canTakeRide(this))
                driver.notify(driver, "There is a ride that has a source area from your favorite areas!", this);
        }
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Double getPrice() {
        return price;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void calculatePriceAfterDiscount() {
        Discount discount = new FirstRideDiscount(this);
        discount.linkWith(new DestinationAreaDiscount(this)).
                linkWith(new MultiplePassengersDiscount(this)).
                linkWith(new PublicHolidayDiscount(this)).
                linkWith(new BirthdateDiscount(this)).
                linkWith(null);
        priceAfterDiscount = discount.discount(price);
    }

    public Driver getDriver() {
        return driver;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public boolean makeTransaction() {
        calculatePriceAfterDiscount();
        return driver.deposit(getPrice()) && passenger.withdraw(getPriceAfterDiscount());
    }
}