public class ApiTestMbWay {


    public void checkTransactionsList(){
        RestAssured.baseURI= "https://gorest.co.in";


        // Send a GET request to retrieve todos


        Response response = given().when().get("public/v2/todos");


        //Validate status code


        response.then().statusCode(200);


        //Validate body response schema/JSON


        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemaJson"));





    }
