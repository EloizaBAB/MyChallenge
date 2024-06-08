import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiTestMbWay {


    public void setUpUrl() {
        RestAssured.baseURI = "https://api.mbway.exampletest.com";
    }

    public void checkTransactionsList() {
        // Send a GET request to retrieve  history of transactions

        Response response = given()
                .header("Authorization", "Bearer your_valid_token_here") // Replace with actual token
                .when()
                .get("/api/transfer-history")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("transactions", notNullValue())
                .extract()
                .response();
//Retrieve a list of objects and iterate through each one of them and verify that the fields are not empty
        List<Object> transactions = response.jsonPath().getList("transactions");

        for (Object transaction : transactions) {
            Map<String, Object> transactionMap = (Map<String, Object>) transaction;

            assertNotNull(transactionMap.get("transactionId"));
            assertEquals(true, transactionMap.get("status"));
            assertEquals(true, transactionMap.get("amount"));
            assertEquals(true, transactionMap.get("date"));
            assertEquals(true, transactionMap.get("recipient"));
        }

    }

    //Retrieve the transaction details according to the Id I want
    public void getTransactionById() {
        String transactionId = "12345";

        Response response = given()
                .header("Authorization", "Bearer valid_token")
                .when()
                .get("/api/transfer-history/" + transactionId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("transactionId", equalTo(transactionId))
                .body("status", equalTo("completed"))
                .extract()
                .response();

        System.out.println(response.asString());
    }

    public void validateErrorMessageWithInvalidId() {
        String invalidId = "123f3";

        Response response = (Response) given()
                .header("Authorization", " Bearer valid_token")
                .when()
                .get("/api/transfer-history/" + invalidId)
                .then()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Transaction not found"));
    }

    //filter transactions by status equals to completed
    public void validateStatus() {
        Response response = given()
                .header("Authorization", "Bearer valid_token")
                .queryParam("status", "completed")
                .when()
                .get("/api/transfer-history")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("transactions", notNullValue())
                .extract()
                .response();

        List<Object> transactions = response.jsonPath().getList("transactions");

        for (Object transaction : transactions) {
            Map<String, Object> transactionMap = (Map<String, Object>) transaction;
            assertEquals("completed", transactionMap.get("status"));
        }
    }

    public static void main(String[] args) {
        ApiTestMbWay tests = new ApiTestMbWay();
        tests.setUpUrl();
        tests.checkTransactionsList();
        tests.getTransactionById();
        tests.validateErrorMessageWithInvalidId();
        tests.validateStatus();
    }
}
