package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.matcher.DetailedCookieMatcher;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.*;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.junit.Assert;
import pojo.AddPlacePayload;
import pojo.Location;
import resouces.APIResources;
import resouces.TestDataBuild;
import resouces.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class StepDefinition extends Utils {
    RequestSpecification res;
    ResponseSpecification resspec;
    Response response;
    TestDataBuild testDataBuild = new TestDataBuild();
    String placeId;


    @Given("Add Place payload with {string} {string} and {string}")
    public void add_place_payload(String name, String language, String address) throws IOException {
        res = given().log().all().spec(requestSpecification())
                .body(testDataBuild.addPlacePayload(name, language, address));
    }

    @When("User calls {string} with {string} http request")
    public void user_calls_with_post_http_request(String resource, String method) {
        APIResources apres = APIResources.valueOf(resource);
        System.out.println(apres.getResource());
        switch (method) {
            case "POST":
                response = res.when().post(apres.getResource()).then().log().all().extract().response();
                break;
            case "GET":
                response = res.when().get(apres.getResource()).then().log().all().extract().response();
                break;
            case "DELETE":
                response = res.when().delete(apres.getResource()).then().log().all().extract().response();
                break;
        }


    }

    @Then("The API call is success with status code {int}")
    public void the_api_call_is_success_with_status_code(Integer statusCode) {
        assertEquals((int) statusCode, response.statusCode());
    }

    @And("{string} in response body is {string}")
    public void in_response_body_is(String field, String value) {
        assertEquals(getJSONPath(response,field), value);
    }


    @And("Verify place_id created maps to {string} using {string}")
    public void verifyPlace_idCreatedMapsToUsing(String expectedName, String resource) throws IOException {

        placeId = getJSONPath(response, "place_id");
        res = given().log().all().spec(requestSpecification()).queryParam("place_id", placeId);
        user_calls_with_post_http_request(resource, "GET");
        String actualName = getJSONPath(response,"name");
        Assertions.assertThat(actualName)
                .as("The strings are not the same. Expected: %s, Actual: %s", expectedName, actualName)
                .isEqualTo(expectedName);
    }


}
