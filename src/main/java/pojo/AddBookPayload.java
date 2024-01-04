package pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBookPayload {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;


}
