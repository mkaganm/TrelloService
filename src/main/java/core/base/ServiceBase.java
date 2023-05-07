package core.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.utility.Logging;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

public class ServiceBase {

    public final ObjectMapper objectMapper = new ObjectMapper();

    public Response response;

    private RequestSpecification requestSpecification = defaultRequestSpecification();

    private static RequestSpecification defaultRequestSpecification() {
        return new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json").addFilter(new AllureRestAssured())
                .build();
    }

    private static RequestSpecification getParamRequestSpecification() {
        return new RequestSpecBuilder()
                .addHeader("Accept", "application/json").addFilter(new AllureRestAssured())
                .build();
    }

    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T convertResponseTo(Class<T> clazz) {
        return fromJson(response.jsonPath().prettify(), clazz);
    }

    public Response post(String url, String jsonRequestBody, String serviceName) {
        setAllureFilter(serviceName);
        response = given()
                .spec(requestSpecification)
                .body(jsonRequestBody)
                .post(url);

        requestSpecification = defaultRequestSpecification();
        return response;
    }

    public Response postWithParams(String url, HashMap<String, String> params, String body, String serviceName) {

        setAllureFilter(serviceName);
        response = given()
                .spec(requestSpecification)
                .queryParams(params)
                .body(body)
                .when()
                .post(url);


        requestSpecification = defaultRequestSpecification();
        return response;
    }

    public Response deleteWithParams(String url, HashMap<String, String> params, String body, String serviceName) {

        setAllureFilter(serviceName);
        response = given()
                .spec(requestSpecification)
                .queryParams(params)
                .body(body)
                .when()
                .delete(url);


        requestSpecification = defaultRequestSpecification();
        return response;
    }

    public Response getWithParams(String url, HashMap<String, String> params, String serviceName) {

        requestSpecification = getParamRequestSpecification();

        response = given()
                .spec(requestSpecification)
                .queryParams(params)
                .when()
                .get(url);

        requestSpecification = defaultRequestSpecification();
        return response;
    }

    public Response putWithParams(String url, HashMap<String, String> params, String serviceName) {

        requestSpecification = getParamRequestSpecification();

        response = given()
                .spec(requestSpecification)
                .queryParams(params)
                .when()
                .put(url);

        requestSpecification = defaultRequestSpecification();
        return response;
    }

    private void setAllureFilter(String serviceName) {
        AllureRestAssured allureRestAssured = new AllureRestAssured();
        allureRestAssured.setRequestAttachmentName(serviceName + " Request");
        allureRestAssured.setResponseAttachmentName(serviceName + " Response");
        requestSpecification.filter(allureRestAssured);
    }

    public Response get(String url, String serviceName) {
        setAllureFilter(serviceName);
        response = RestAssured.given().spec(requestSpecification).get(url);
        requestSpecification = defaultRequestSpecification();
        return response;
    }

    public void checkResponseTime(long expectedResponseTime) {
        Logging.pass("Expected Response Time: " + expectedResponseTime);
        Logging.pass("Actual   Response Time: " + response.getTime());

        if (response.getTime() <= expectedResponseTime) {
            Logging.pass("Response time is OK.");
        } else {
            Logging.warning("Response time failure!");
        }
    }

    public void checkStatusCode(int expected, int actual) {
        control(expected == actual,
                "Status Code : " + actual + " Expected : " + expected,
                "Status Code : " + actual + " Expected : " + expected);
    }

    public void control(boolean statement, String onTrue, String onFalse) {

        if (statement) {
            Logging.pass(onTrue);
        } else {
            Logging.fail(onFalse);
        }
    }
}
