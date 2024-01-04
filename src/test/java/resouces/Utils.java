package resouces;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.JiraCreateSessionPayload;

import java.io.*;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Utils {

    RequestSpecification req;
    JiraCreateSessionPayload jiraCreateSessionPayload = new JiraCreateSessionPayload();
    SessionFilter session = new SessionFilter();
    PrintStream log;

    public RequestSpecification requestSpecification() throws IOException {
        log = new PrintStream(new FileOutputStream("log.txt"));
        req = new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrlUdemy")).addQueryParam("key", "qaclick123")
                .addHeader("Content-Type", "application/json")
                .addFilter(RequestLoggingFilter.logRequestTo(log))
                .addFilter(ResponseLoggingFilter.logResponseTo(log)).build();

        return req;
    }

    public RequestSpecification requestSpecificationHK() throws IOException {
        log = new PrintStream(new FileOutputStream("logHK.txt"));
        req = new RequestSpecBuilder().setBaseUri(getGlobalValue("baseURLHK"))
                .addHeader("Content-Type", "application/json")
                .addFilter(RequestLoggingFilter.logRequestTo(log))
                .addFilter(ResponseLoggingFilter.logResponseTo(log)).build();
        return req;
    }

    public RequestSpecification requestSpecificationWithToken(String token) {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        req = new RequestSpecBuilder().setBaseUri("https://restful-booker.herokuapp.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "token=" + token).build();
        return req;
    }


    public RequestSpecification jiraCreateSessionRequestSpecification() {
        req = new RequestSpecBuilder().setBaseUri("http://localhost:8080")
                .addHeader("Content-Type", "application/json").build().filter(session);
        return req;
    }

    public RequestSpecification jiraAddProjectRequestSpecification() {
        req = new RequestSpecBuilder().setBaseUri("http://localhost:8080")
                .addHeader("Content-Type", "application/json").build().filter(session);
        return req;
    }

    public static String getGlobalValue(String key) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Admin\\Documents\\APIFrameworkver1.1\\src\\test\\java\\resouces\\global.properties");
        properties.load(fileInputStream);
        return properties.getProperty(key);
    }

    public String getJSONPath(Response response, String key) {
        String responseString = response.asString();
        JsonPath js = new JsonPath(responseString);
        return js.get(key).toString();
    }

}
