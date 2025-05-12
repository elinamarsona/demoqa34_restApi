package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("API-тесты")
public class ReqresInLombokTest extends TestBase {

    private static final String API_KEY = "reqres-free-v1";

    @Test
    @DisplayName("post. Успешная регистрация")
    void successfulRegisterTest() {
        RegisterBodyModel authData = new RegisterBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");

        RegisterResponseModel response = given()
                .header("x-api-key", API_KEY)
                .body(authData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(RegisterResponseModel.class);
        assertEquals(4, response.getId());
        assertNotNull(response.getToken());
    }

    @Test
    @DisplayName("post. Неуспешная регистрация только с email")
    void UnSuccessfulRegisterTest() {
        RegisterBodyModel authData = new RegisterBodyModel();
        authData.setEmail("eve.holt@reqres.in");

        ErrorBodyModel response = given()
                .header("x-api-key", API_KEY)
                .body(authData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .extract().as(ErrorBodyModel.class);

        assertEquals("Missing password",response.getError());
    }

    @Test
    @DisplayName("post. Успешный вход")
    void loginSuccessfulTest() {
        LoginBodyModel authData = new LoginBodyModel();
                authData.setEmail("eve.holt@reqres.in");
                authData.setPassword("pistol");

        LoginResponseModel response = given()
                .header("x-api-key", API_KEY)
                .body(authData)
                .contentType(JSON)
                .log().all()

                .when()
                .log().all()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract().as(LoginResponseModel.class);
        assertNotNull(response.getToken());
    }

    @Test
    @DisplayName("post. Неудачный вход без пароля")
    void loginUnSuccessfulTest() {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");

        ErrorBodyModel response = given()
                .header("x-api-key", API_KEY)
                .body(authData)
                .contentType(JSON)
                .log().all()

                .when()
                .log().all()
                .post("/login")
                .then()
                .log().all()
                .statusCode(400)
                .extract().as(ErrorBodyModel.class);

        assertEquals("Missing password",response.getError());
    }

    @Test
    @DisplayName("delete. Удалить существующего пользователя")
    void deleteTest() {

        given()
                .header("x-api-key", API_KEY)
                .log().all()

                .when()
                .log().all()
                .delete("/users/4")
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("patch. Частичное обновление пользователя")
    void successfulPatchUpdateTest() {
        PatchUpdateBodyModel authData = new PatchUpdateBodyModel();
        authData.setName("morpheus");
        authData.setJob("zion resident");

        PatchUpdateResponseModel response = given()
                .header("x-api-key", API_KEY)
                .body(authData)
                .contentType(JSON)
                .log().uri()

                .when()
                .patch("/users/2")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(PatchUpdateResponseModel.class);
        assertEquals("morpheus", response.getName());
        assertEquals("zion resident", response.getJob());
        assertNotNull(response.getUpdatedAt());
    }

}


