package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import pojo.*;
import resouces.APIResourcesHK;
import resouces.TestDataBuild;
import resouces.Utils;

import static org.assertj.core.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class HeroeKuappStepDefinitions extends Utils {

    RequestSpecification res;
    HeroekuappCreateBookResponse heroekuappCreateBookResponse;
    List<GetAllBooksResponse> getAllBooksResponse;
    Response response;
    GetBookResponse getBookResponse;
    AuthPayload authPayload;
    Map<String, Object> jsonMap;
    int randomIdToGet;
    String firstName;
    String lastName;
    int totalPrice;
    Boolean depositpaid;
    String checkIn;
    String checkOut;
    String additionalneeds;
    String authToken;


    TestDataBuild testDataBuild = new TestDataBuild();


    @Given("Add Book payload to add new book")
    public void addBookPayloadToAddNewBook() throws IOException {
        res = given().spec(requestSpecificationHK().body(testDataBuild.addBookPayload()).log().all());
    }

    public int getBookId() {
        return heroekuappCreateBookResponse.getBookingid();
    }

    @Then("HTTP status of the response is {int}")
    public void httpStatusOfTheResponseIs(int httpStatus) {
        Assert.assertEquals(response.getStatusCode(), httpStatus);
    }

    @And("field {string} exists in response")
    public void fieldExistsInResponse(String field) {
        Assert.assertTrue(jsonMap.containsKey(field));
        assertThat(heroekuappCreateBookResponse.getBooking().getBookingdates().getCheckin())
                .isEqualTo(testDataBuild.addBookPayload().getBookingdates().getCheckin());
    }

    @And("value for bookingid is an integer")
    public void valueForBookingidIsAnInteger() {
        System.out.println(heroekuappCreateBookResponse.getBookingid());
        assertThat(heroekuappCreateBookResponse.getBookingid()).isInstanceOf(Integer.class);
    }

    @When("User calls {string} by id")
    public void userCallsById(String arg0) throws IOException {
        res = given().spec(requestSpecificationHK()).pathParam("id", getBookId()).log().all();
        response = res.when().get("/booking/{id}").then().log().all().extract().response();
    }

    @And("response contains all required fields")
    public void responseContainsAllRequiredFields() {
        jsonMap = response.as(new TypeRef<Map<String, Object>>() {
        });
        assertThat(jsonMap.containsKey("firstname")).isTrue();
        assertThat(jsonMap.containsKey("lastname")).isTrue();
        assertThat(jsonMap.containsKey("totalprice")).isTrue();
        assertThat(jsonMap.containsKey("depositpaid")).isTrue();
        assertThat(jsonMap.containsKey("bookingdates")).isTrue();
        assertThat(jsonMap.containsKey("additionalneeds")).isTrue();

    }

    @Given("Send request to get all books and pick random book id")
    public void sendRequestToGetAllBooks() throws IOException {
        res = given().spec(requestSpecificationHK()).log().all();
        response = res.when().get("/booking")
                .then().log().all().extract().response();
        getAllBooksResponse = response.as(new TypeRef<List<GetAllBooksResponse>>() {
        });
        GetAllBooksResponse randomId = getAllBooksResponse.stream().skip(new Random().nextInt(getAllBooksResponse.size()))
                .findFirst().orElseThrow(null);
        System.out.println("Random id is " + randomId.getBookingid());
        randomIdToGet = randomId.getBookingid();
    }

    @And("User calls {string} by random id")
    public void userCallsByRandomId(String arg0) throws IOException {
        res = given().spec(requestSpecificationHK()).pathParam("id", randomIdToGet).log().all();
        response = res.when().get("/booking/{id}").then().log().all().extract().response();
        getBookResponse = response.as(GetBookResponse.class);
    }


    @And("Save values of book before updating")
    public void saveValuesOfBookBeforeUpdating() {
        firstName = getBookResponse.getFirstname();
        System.out.println("First name before updating is " + firstName);
        lastName = getBookResponse.getLastname();
        System.out.println("Last name before updating is " + lastName);
        totalPrice = getBookResponse.getTotalprice();
        System.out.println("Total price before updating is " + totalPrice);
        depositpaid = getBookResponse.isDepositpaid();
        System.out.println("Is deposit paid before updating is " + depositpaid);
        checkIn = getBookResponse.getBookingdates().getCheckin();
        System.out.println("Check in before updating is " + checkIn);
        checkOut = getBookResponse.getBookingdates().getCheckout();
        System.out.println("Check out before updating is " + checkOut);
        additionalneeds = getBookResponse.getAdditionalneeds();
        System.out.println("Additional needs before updating is " + additionalneeds);
    }

    @And("Send request to create token for updating book")
    public void sendRequestToCreateTokenForUpdatingBook() throws IOException {

        res = given().spec(requestSpecificationHK()).log().all().body(testDataBuild.getTokenPayload());
        Token token = res.when().post("/auth")
                .then().log().all().extract().response().as(Token.class);
        authToken = token.getToken();
    }

    @When("User updates the book by id picked randomly")
    public void userUpdatesTheBookByIdPickedRandomly() {
        res = given().spec(requestSpecificationWithToken(authToken)).pathParam("id", randomIdToGet).
                body(testDataBuild.addBookPayload()).log().all();
        response = res.when().put("/booking/{id}").then()
                .log().all().extract().response();
    }

    @Then("User calls {string} by random id to check whether is updated properly")
    public void userCallsByRandomIdToCheckWhetherIsUpdatedProperly(String arg0) throws IOException {
        res = given().spec(requestSpecificationHK()).pathParam("id", randomIdToGet).log().all();
        response = res.when().get("/booking/{id}").then().log().all().extract().response();
        getBookResponse = response.as(GetBookResponse.class);
    }

    @And("Verify first name is correctly updated")
    public void verifyFirstNameIsCorrectlyUpdated() {
        assertThat(getBookResponse.getFirstname()).isNotEqualToIgnoringWhitespace(firstName);
    }

    @And("Verify last name is correctly updated")
    public void verifyLastNameIsCorrectlyUpdated() {
        assertThat(getBookResponse.getLastname()).isNotEqualToIgnoringWhitespace(lastName);
    }

    @And("Verify total price is correctly updated")
    public void verifyTotalPriceIsCorrectlyUpdated() {
        assertThat(getBookResponse.getTotalprice()).isNotEqualTo(totalPrice);
    }

    @When("User calls {string} with {string} http request HK")
    public void userCallsWithHttpRequestHK(String resource, String method) throws IOException {
        APIResourcesHK apiResourcesHK = APIResourcesHK.valueOf(resource);
        System.out.println(apiResourcesHK.getResource());
        switch (method) {
            case "POST" -> {
                response = res.when().post(apiResourcesHK.getResource()).then().log().all().extract().response();
                heroekuappCreateBookResponse = response.as(HeroekuappCreateBookResponse.class);
                jsonMap = response.as(new TypeRef<Map<String, Object>>() {
                });
            }
            case "GET" -> {
                res = given().spec(requestSpecificationHK()).log().all();
                response = res.when().get(apiResourcesHK.getResource()).then().log().all().extract().response();
            }
            case "PUT" -> {
                res = given().spec(requestSpecificationWithToken(authToken)).pathParam("id", randomIdToGet).
                        body(testDataBuild.addBookPayload()).log().all();
                response = res.when().put(apiResourcesHK.getResource()).then()
                        .log().all().extract().response();
            }
            case "DELETE" -> {
                System.out.println("================ID to delete==============" + getBookId());
                res = given().spec(requestSpecificationWithToken(authToken)).pathParam("id", getBookId())
                        .log().all();
                response = res.when().delete(apiResourcesHK.getResource()).then()
                        .log().all().extract().response();
            }
        }
    }

    @And("User picks random book id")
    public void userPicksRandomBookId() {
        getAllBooksResponse = response.as(new TypeRef<List<GetAllBooksResponse>>() {
        });
        GetAllBooksResponse randomId = getAllBooksResponse.stream().skip(new Random().nextInt(getAllBooksResponse.size()))
                .findFirst().orElseThrow(null);
        System.out.println("Random id is " + randomId.getBookingid());
        randomIdToGet = randomId.getBookingid();
    }
}
