package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("API-тесты")
public class ReqresInTest extends TestBase {

    private static final String API_KEY = "reqres-free-v1";

    @Test
    @DisplayName("post. Успешная регистрация")
    void successfulRegisterTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
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
                .body("id", is(4))
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("post. Неуспешная регистрация только с email")
    void UnSuccessfulRegisterTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"\"}";

        given()
                .header("x-api-key", API_KEY)
                .body(authData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    @DisplayName("get. Наличие зарегистрированного пользователя в базе")
    void userInTheDBTest() {
        given()
                .header("x-api-key", API_KEY)
                .log().all()
                .get("/users/2")
                .then()
                .log().all()
                .body("data.id", is(2))
                .statusCode(200);
    }

    @Test
    @DisplayName("get. Отсутствие пользователя в базе")
    void NotFoundUserInTheDBTest() {
        given()
                .header("x-api-key", API_KEY)
                .log().all()
                .get("/users/23")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("post. Успешный вход")
    void loginSuccessfulTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
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
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("post. Неудачный вход без пароля")
    void loginUnSuccessfulTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"\"}";

        given()
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
                .body("error", containsString("Missing password"));
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
        String authData = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        given()
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
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("updatedAt", notNullValue());
    }

}


