package resouces;

import pojo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDataBuild {

    public AddPlacePayload addPlacePayload(String name, String language, String address) {
        AddPlacePayload addPlacePayload = new AddPlacePayload();
        Location location = new Location();
        addPlacePayload.setAccuracy(50);
        addPlacePayload.setName(name);
        addPlacePayload.setPhone_number("(+91) 983 893 3937");
        addPlacePayload.setAddress(address);
        List<String> myList = new ArrayList<String>();
        myList.add("shoe park");
        myList.add("shop");
        addPlacePayload.setTypes(myList);
        addPlacePayload.setWebsite("http://google.com");
        addPlacePayload.setLanguage(language);
        location.setLat(-38.383494);
        location.setLat(33.427362);
        addPlacePayload.setLocation(location);
        return addPlacePayload;
    }

    public CreateSessionPayload createSessionPayload() {
        CreateSessionPayload createSessionPayload = new CreateSessionPayload();
        createSessionPayload.setUsername("pawel.wasilewski198531");
        createSessionPayload.setPassword("Jakoktochce#1234");
        return createSessionPayload;

    }

    public AddBookPayload addBookPayload() {
        AddBookPayload addBookPayload = new AddBookPayload();
        addBookPayload.setFirstname("Author" + generateRandomInt());
        addBookPayload.setLastname("Lastname" + generateRandomInt());
        addBookPayload.setDepositpaid(true);
        addBookPayload.setTotalprice(generateRandomInt());
        addBookPayload.setAdditionalneeds("No");
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("2020-01-01");
        bookingDates.setCheckout("2021-01-01");
        addBookPayload.setBookingdates(bookingDates);
        return addBookPayload;
    }

    public AuthPayload getTokenPayload() {
        AuthPayload authPayload = new AuthPayload();
        authPayload.setUsername("admin");
        authPayload.setPassword("password123");
        return authPayload;
    }

    public int generateRandomInt() {
        Random random = new Random();
        int randomNumber = 0;
        while (randomNumber < 10000) {
            randomNumber = random.nextInt(90000) + 10000;
        }
        return randomNumber;
    }

}
