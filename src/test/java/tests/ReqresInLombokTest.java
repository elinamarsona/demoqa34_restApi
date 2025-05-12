package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.Specs.*;

@DisplayName("API-тесты")
public class ReqresInLombokTest extends TestBase {

    @Test
    @DisplayName("post. Успешная регистрация")
    void successfulRegisterTest() {
        RegisterBodyModel authData = new RegisterBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");

        RegisterResponseModel response = step("Main request", () ->
                given(requestSpec)
                        .body(authData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(responseSpec)
                        .extract().as(RegisterResponseModel.class));

        step("Check response", () -> {
            assertEquals(4, response.getId());
            assertNotNull(response.getToken());
        });
    }

    @Test
    @DisplayName("post. Неуспешная регистрация только с email")
    void UnSuccessfulRegisterTest() {
        RegisterBodyModel authData = new RegisterBodyModel();
        authData.setEmail("eve.holt@reqres.in");

        ErrorBodyModel response = step("Main request", () ->
                given(requestSpec)
                        .body(authData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(badResponseSpec)
                        .extract().as(ErrorBodyModel.class));

        step("Check response", () ->
                assertEquals("Missing password", response.getError()));
    }

    @Test
    @DisplayName("post. Успешный вход")
    void loginSuccessfulTest() {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");

        LoginResponseModel response = step("Main request", () ->
                given(requestSpec)
                        .body(authData)

                        .when()
                        .post("/login")

                        .then()
                        .spec(responseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Check response", () ->
                assertNotNull(response.getToken()));
    }

    @Test
    @DisplayName("post. Неудачный вход без пароля")
    void loginUnSuccessfulTest() {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");

        ErrorBodyModel response = step("Main request", () ->
                given(requestSpec)
                        .body(authData)

                        .when()
                        .post("/login")

                        .then()
                        .spec(badResponseSpec)
                        .extract().as(ErrorBodyModel.class));

        step("Check response", () ->
                assertEquals("Missing password", response.getError()));
    }

    @Test
    @DisplayName("delete. Удалить существующего пользователя")
    void deleteTest() {
        step("Main request", () -> {
            given(requestSpec)

                    .when()
                    .delete("/users/4")

                    .then()
                    .spec(deleteResponseSpec);
        });
        step("Check response", () -> {
        });
    }

    @Test
    @DisplayName("patch. Частичное обновление пользователя")
    void successfulPatchUpdateTest() {
        PatchUpdateBodyModel authData = new PatchUpdateBodyModel();
        authData.setName("morpheus");
        authData.setJob("zion resident");

        PatchUpdateResponseModel response = step("Main request", () ->
                given(requestSpec)
                        .body(authData)

                        .when()
                        .patch("/users/2")

                        .then()
                        .spec(responseSpec)
                        .extract().as(PatchUpdateResponseModel.class));

        step("Check response", () -> {
            assertEquals("morpheus", response.getName());
            assertEquals("zion resident", response.getJob());
            assertNotNull(response.getUpdatedAt());
        });
    }

}


