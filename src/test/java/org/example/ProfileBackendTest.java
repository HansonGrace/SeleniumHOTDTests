package org.example;

import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/*
* @author Grace Hanson
* Tests backend components, such as REST components and posts/gets
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileBackendTest {

    private CookieFilter cookieFilter;


    @BeforeAll
    public void setup() {
        // set base uri and cookie filter for maintaining session
        //backend port:
        RestAssured.baseURI = "http://localhost:5000";
        cookieFilter = new CookieFilter();

        // simulate login to authenticate
        given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"testuser123\", \"password\": \"password123\"}")
                .filter(cookieFilter)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("message", containsString("Login successful"));
    }

    @Test
    public void testProfileFetchSuccess() {
        // hit /auth/profile after login
        given()
                .contentType(ContentType.JSON)
                .filter(cookieFilter)
                .when()
                .get("/auth/profile")
                .then()
                .statusCode(200)
                .body("username", notNullValue())
                .body("nameVar", notNullValue())
                .body("bio", notNullValue());
    }

    @Test
    public void testCompletedHikesFetch() {
        // test completed hikes endpoint
        given()
                .filter(cookieFilter)
                .when()
                .get("/auth/completed-hikes")
                .then()
                .statusCode(200)
                //expect an array
                .body("$", instanceOf(java.util.List.class));
    }

    @Test
    public void testTrailDataFetch() {
        //simulate trail status check
        given()
                .filter(cookieFilter)
                .when()
                .post("/auth/trails")
                .then()
                .statusCode(200)
                .body("status", anyOf(equalTo("in-progress"), equalTo("not-started"), nullValue()));
    }

    @Test
    public void testFriendRequestFetch() {
        // check for friend requests
        given()
                .filter(cookieFilter)
                .when()
                .get("/auth/friend-requests")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    @AfterAll
    public void logout() {
        // cleanup and logout
        given()
                .filter(cookieFilter)
                .when()
                .post("/auth/logout")
                .then()
                .statusCode(200);
    }
}
