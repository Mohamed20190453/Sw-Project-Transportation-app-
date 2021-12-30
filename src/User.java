

import java.util.ArrayList;
import java.util.Scanner;

public class User extends NewUser {
    private  Ride getoffers = new Ride();
    private ArrayList<Ride> ridesHistory=new ArrayList<Ride>();
    private SystemData Data=DataArrays.getInstance();
    public User(String username, String email, String phone, String password) {
        super(username, email, phone, password);
    }

    public User() {
    }

    public boolean register(NewUser user) {
        if (Data.getUsernames().contains(user.getUsername()))
            return false;
        Data.addUser((User) user);
        return true;
    }

    public NewUser login(String username,String password){
        ArrayList<User> users = Data.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password) && !users.get(i).isSuspended()) {
                return users.get(i);
            }
        }
        return null;
    }

    public Ride getGetoffers() {
        return getoffers;
    }

    public void requestRide(String source, String destination, int passenger) {
        Ride my_ride = new Ride(source, destination, this,passenger);
        Data.addRide(my_ride);
    }

    public void rateDriver(Driver driver, int stars) {
        driver.addUserRating(new Rating(this, stars));
    }

    public double checkDriverRating(Driver driver) {
        return driver.getAvgRating();
    }

    public double chkAvgRating(Driver driver){
        return driver.getAvgRating();
    }

    public String chkOffer(Offer offer){
        return ("The driver: " + offer.getDriver().getUsername() + " Offers Your Ride with: " + offer.getPrice() + " LE.");
    }

    public void acceptOffer(Ride ride,Boolean accept,Offer offer){
        offer.getDriver().notify(offer.getDriver(), "User " + (accept ? "accepted" : "rejected") + " the offer", ride);
        if(accept) {
            ridesHistory.add(ride);
            ride.setDriver(offer.getDriver());
            ride.setPrice(offer.getPrice());
            Data.removeRide(ride);
        }
    }

    public void clearOffers(){
        getoffers = null;
    }

    public void notify(User user, String message, Ride ride) {
        user.addNotification(message);
        getoffers =ride;
    }
}