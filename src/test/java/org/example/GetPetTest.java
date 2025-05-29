package org.example;

import io.restassured.response.Response;
import org.example.api.PetApiClient;
import org.example.models.Pet;
import org.example.utils.HttpStatusCodes;
import org.example.utils.PetFactory;
import org.example.utils.SchemaChecker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetPetTest {

    private static PetApiClient petApiClient;

    @BeforeAll
    public static void prepare() {
        petApiClient = new PetApiClient();
    }

    @Test
    @DisplayName("Получение питомца по ID — ожидаем 200 и корректные данные")
    void shouldReturnPetById() {
        Pet newPet = PetFactory.createPet(12345L, "Keksik", "available");

        petApiClient.addPet(newPet);

        Response getResponse = petApiClient.getPetById(newPet.getId());

        assertEquals(HttpStatusCodes.OK, getResponse.statusCode(), "Питомец должен быть найден");
        assertEquals("Keksik", getResponse.jsonPath().getString("name"), "Имя питомца должно совпадать");
        assertEquals("available", getResponse.jsonPath().getString("status"), "Статус должен быть 'available'");
        assertEquals(newPet.getId(), getResponse.jsonPath().getLong("id"), "ID питомца должен совпадать");
        SchemaChecker.assertMatchesSchema(getResponse, "schemas/get-pet-schema.json");
    }

    @Test
    @DisplayName("Попытка получить питомца с несуществующим ID — ожидаем 404")
    void shouldReturn404ForNonexistentPet() {
        long nonexistentPetId = 999999L;

        petApiClient.deletePet(nonexistentPetId);

        Response response = petApiClient.getPetById(nonexistentPetId);

        assertEquals(HttpStatusCodes.NOT_FOUND, response.statusCode(), "Ожидали 404 для несуществующего питомца");
    }

    @Test
    @DisplayName("Попытка получить питомца с некорректным ID — ожидаем 400 (если поддерживается)")
    void shouldReturn400ForInvalidPetId() {
        Response response = petApiClient.getPetByIdRaw("invalid-id");

        assertEquals(HttpStatusCodes.BAD_REQUEST, response.statusCode(), "Ожидали 400 для некорректного ID");
    }
}
