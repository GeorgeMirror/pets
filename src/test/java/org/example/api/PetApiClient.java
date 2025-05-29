package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.models.Pet;
import org.example.utils.ApiEndpoints;
import org.example.utils.Config;

import static io.restassured.RestAssured.given;

public class PetApiClient {

    public Response addPet(Pet pet) {
        return given()
                .baseUri(Config.getBaseUrl())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(pet)
                .when()
                .post(ApiEndpoints.ADD_PET);
    }

    public Response getPetById(long petId) {
        return given()
                .baseUri(Config.getBaseUrl())
                .accept(ContentType.JSON)
                .when()
                .get(ApiEndpoints.GET_PET_BY_ID, petId);
    }

    public Response getPetByIdRaw(String id) {
        return given()
                .baseUri(Config.getBaseUrl())
                .pathParam("petId", id)
                .when()
                .get(ApiEndpoints.GET_PET_BY_ID, id);
    }

    public Response deletePet(long petId) {
        return given()
                .baseUri(Config.getBaseUrl())
                .accept(ContentType.JSON)
                .pathParam("petId", petId)
                .when()
                .delete(ApiEndpoints.DELETE_PET_BY_ID);
    }

    public Response deletePetWithInvalidId(String invalidId) {
        return given()
                .baseUri(Config.getBaseUrl())
                .accept(ContentType.JSON)
                .pathParam("petId", invalidId)
                .when()
                .delete(ApiEndpoints.DELETE_PET_BY_ID);
    }

    public Response updatePet(Pet pet) {
        return given()
                .baseUri(Config.getBaseUrl())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(pet)
                .when()
                .put(ApiEndpoints.UPDATE_PET);
    }

    public Response updatePetRawBody(String rawBody) {
        return given()
                .baseUri(Config.getBaseUrl())
                .contentType("application/json")
                .body(rawBody)
                .when()
                .put(ApiEndpoints.UPDATE_PET);
    }
}
