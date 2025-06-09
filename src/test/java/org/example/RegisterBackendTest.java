package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/*
 * @author Grace Hanson
 * Tests backend components, such as REST components and posts/gets
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegisterBackendTest {

    @BeforeAll
    public void setup() {
        //set base URI for backend testing
        RestAssured.baseURI = "http://localhost:5000"; // change if backend uses another port
    }

    @Test
    public void testSuccessfulRegistration() {
        //simulate valid registration request
        given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"testuser123\", \"password\": \"password123\", \"bio\": \"test bio\", \"nameVar\": \"Test User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201)
                .body("message", equalTo("User registered successfully"));
    }

    @Test
    public void testRegistrationWithShortPassword() {
        //simulate frontend behavior when password is too short
        given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"shortpassuser\", \"password\": \"123\", \"bio\": \"bio\", \"nameVar\": \"User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(anyOf(is(400), is(500))); // backend may return 400 or 500 depending on validation
    }

    @Test
    public void testDuplicateUsername() {
        //run this after registering "testuser123" to see duplicate handling
        given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"testuser123\", \"password\": \"password123\", \"bio\": \"duplicate\", \"nameVar\": \"Duplicate User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(500)
                .body("error", containsString("Failed to register user"));
    }
}
