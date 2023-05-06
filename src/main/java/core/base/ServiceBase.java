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
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class ServiceBase {

    public final Random random = new Random();

    public final ObjectMapper objectMapper=new ObjectMapper();

    public Response response;

    private RequestSpecification requestSpecification =  defaultRequestSpecification();

    private static RequestSpecification defaultRequestSpecification() {
        return new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json").addFilter(new AllureRestAssured())
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
            return objectMapper.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T convertResponseTo(Class<T> clazz) {
        return fromJson(response.jsonPath().prettify(),clazz);
    }

    public void checkStatusCode(Integer statusCode) {
        if (statusCode != null) {
            if (response.getStatusCode() != statusCode) {
                Logging.fail(statusCode, response.getStatusCode(), "Status code error! Error: " + response.jsonPath().get("message"));
            } else {
                Logging.pass("Status code is OK. Status code : " + response.getStatusCode());
            }
        }
    }

    public Response post(String url) {
        return post(url, "");
    }

    public Response post(String url, String jsonRequestBody) {
        return post(url, jsonRequestBody, "");
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

    public Response postWithParams(String url, HashMap<String, String> params, String serviceName) {
        setAllureFilter(serviceName);
        response = given()
                .spec(requestSpecification)
                .params(params)
                .post(url);

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


    public Response get(String url, Map<String, String> queryParams, String serviceName) {
        addMultipleQueryParams(queryParams);
        return get(url, serviceName);
    }

    /**
     * @param expectedResponseTime millisecond olarak gönderilmelidir
     */
    public void checkResponseTime(long expectedResponseTime) {
        Logging.pass("Expected Response Time: " + expectedResponseTime);
        Logging.pass("Actual   Response Time: " + response.getTime());

        if (response.getTime() <= expectedResponseTime) {
            Logging.pass("Response time is OK.");
        } else {
            Logging.warning("Response time failure!");
        }
    }

    public void addBearer(String token) {
        requestSpecification.header("Authorization", "Bearer " + token);
    }

    public void addApiKey(String apiKey) {
        requestSpecification.header("api",apiKey);
    }

    public void addMultipleHeader(Map<String, String> multipleHeaders) {
        requestSpecification.headers(multipleHeaders);
    }

    public void addMultipleQueryParams(Map<String, String> multipleQueryParams) {
        requestSpecification.queryParams(multipleQueryParams);
    }

    public void addMultiplePart(Map<String, String> multipleParts) {
        if (multipleParts.isEmpty())
            return;

        requestSpecification.accept(ContentType.MULTIPART);
        requestSpecification.contentType(ContentType.MULTIPART);

        for (Map.Entry<String, String> multiplePart : multipleParts.entrySet()) {
            requestSpecification.multiPart(multiplePart.getKey(), multiplePart.getValue());
        }
    }

    @Deprecated
    public Response getRequest(String url, String serviceName, String pathVariables, int statusCode) {

        response = given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .get(url + serviceName + pathVariables)
                .then()
                .extract().response();

        control(response.getStatusCode() == statusCode, serviceName
                + " Service Status Code:"
                + statusCode
                + " success", serviceName
                + " Service Status Code:"
                + statusCode
                + " failure. Status: "
                + response.getStatusLine());
        return response;

    }

    //TODO silinecek yazılmış ancak method overload edilmeli pathvariables kullanmayacak biri boş veri atmamalı .
    @Deprecated
    public Response getRequest(String url, String serviceName, int statusCode) {

        response = given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .get(url + serviceName)
                .then()
                .extract().response();

        control(response.getStatusCode() == statusCode, serviceName
                + " Service Status Code:"
                + statusCode
                + " success", serviceName
                + " Service Status Code:"
                + statusCode
                + " failure. Status: "
                + response.getStatusLine());
        return response;

    }

    // TODO: tüm dönüşüm yapıldğında silinecek
    @Deprecated
    public Response postRequest(String serviceUrl, JSONObject requestBody, int statusCode) {
        Response response = given().filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post(serviceUrl)
                .then()
                .extract().response();

        if (response.getStatusCode() != statusCode) {
            Logging.fail("Status code error : " + response.getStatusCode());
        } else {
            Logging.pass("Status code is OK. Status code : " + response.getStatusCode());
        }
        return response;
    }

    // TODO: tüm dönüşüm yapıldğında silinecek
    @Deprecated
    public Response postRequest(String key, String baseURL, String endpoint, JSONObject requestBody, int statusCode) {

        //TODO bu kısmı bir metot olarak al (body parametresi alsın) sonra bu metodu diğer yerlerde çağır
        //TODO bu şekilde metotdaki jsonobject yerine string request body de atılabilir.
        response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer ")
                .header("api", key)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(baseURL + endpoint)
                .then()
                .extract().response();

        if (response.getStatusCode() != statusCode) {
            Logging.fail("Status code error! : " + response.getStatusCode());
        } else {
            Logging.pass("Status code is OK. Status code : " + response.getStatusCode());
        }

        return response;
    }

    @Deprecated
    public Response postRequest(String key, String baseURL, String endpoint, String requestBody, int statusCode) {

        //TODO bu kısmı bir metot olarak al (body parametresi alsın) sonra bu metodu diğer yerlerde çağır
        //TODO bu şekilde metotdaki jsonobject yerine string request body de atılabilir.
        response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer ")
                .header("api", key)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(baseURL + endpoint)
                .then()
                .extract().response();

        if (response.getStatusCode() != statusCode) {
            Logging.fail("Status code error! : " + response.getStatusCode());
        } else {
            Logging.pass("Status code is OK. Status code : " + response.getStatusCode());
        }

        return response;
    }

    //TODO: tüm dönüşüm yapıldğında silinecek
    @Deprecated
    public void postRequest(String url, String requestBody) {
        Response response = given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post(url)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().response();

    }

    /**
     * Post API servisini çağırıp response u string olarak döner.
     *
     * @return string response.
     */
    @Deprecated
    public Response postWithTokenMultiPart(String url, String serviceName, String requestbody, int statusCode, Map<String, String> multipleHeaders, HashMap<String, String> multipleParts, HashMap<String, String> multipleQueryParams) {

        try {
            RequestSpecification requestSpec = serviceHeadersWithTokenMultiPart(multipleHeaders, multipleParts, multipleQueryParams);

            response = given().spec(requestSpec)
                    .body(requestbody)
                    .when()
                    .post(url + serviceName)
                    .then()
                    .extract()
                    .response();
            control(response.getStatusCode() == statusCode, serviceName
                    + " Service Status Code:"
                    + statusCode
                    + " success", serviceName
                    + " Service Status Code:"
                    + statusCode
                    + " failure. Status: "
                    + response.getStatusLine());
        } catch (Exception e) {
            Logging.fail("Message: " + e);

        }
        return response;
    }

    /**
     * Post API servisini çağırıp response u string olarak döner.
     *
     * @return string response.
     */
    @Deprecated
    public Response getWithTokenMultiPart(String url, String serviceName, String requestbody, int statusCode, HashMap<String, String> multipleHeaders, HashMap<String, String> multipleParts, HashMap<String, String> multipleQueryParams) {

        try {
            RequestSpecification requestSpec = serviceHeadersWithTokenMultiPart(multipleHeaders, multipleParts, multipleQueryParams);

            response = given().spec(requestSpec)
                    .body(requestbody)
                    .when()
                    .get(url + serviceName)
                    .then()
                    .extract()
                    .response();
            control(response.getStatusCode() == statusCode, serviceName
                    + " Service Status Code:"
                    + statusCode
                    + " success", serviceName
                    + " Service Status Code:"
                    + statusCode
                    + " failure. Status: "
                    + response.getStatusLine());
        } catch (Exception e) {
            Logging.fail("Message: " + e);

        }
        return response;
    }

    protected RequestSpecification serviceHeadersWithTokenMultiPart(Map<String, String> multipleHeaders, HashMap<String, String> multipleParts, HashMap<String, String> multipleQueryParams) {


        RequestSpecification request = given();
        request.contentType("application/json");
        request.filter(new AllureRestAssured());

        for (Map.Entry<String, String> entry : multipleHeaders.entrySet()) {
            request.header(entry.getKey(), entry.getValue());
        }

        if (!(multipleParts.size() < 1)) {
            request.accept(ContentType.MULTIPART);
            request.contentType(ContentType.MULTIPART);

            for (Map.Entry<String, String> entry : multipleParts.entrySet()) {
                request.multiPart(entry.getKey(), entry.getValue());
            }

        }

        for (Map.Entry<String, String> entry : multipleQueryParams.entrySet()) {
            request.queryParams(entry.getKey(), entry.getValue());
        }
        return request;
    }

    public void control(boolean statement, String onTrue, String onFalse) {

        if (statement) {
            Logging.pass(onTrue);
        } else {
            Logging.fail(onFalse);
        }
    }
}
