package org.example;

import io.restassured.response.Response;
import org.example.api.PetApiClient;
import org.example.models.Pet;
import org.example.utils.PetFactory;
import org.example.utils.SchemaChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeletePetTest {

    private final PetApiClient client = new PetApiClient();

    @Test
    @DisplayName("Удаление питомца по ID — ожидаем 200 и отсутствие при повторном запросе")
    void shouldDeletePetSuccessfully() {
        Pet pet = PetFactory.createDefaultPet();
        Response addResponse = client.addPet(pet);
        assertEquals(200, addResponse.statusCode(), "Питомец должен быть добавлен");

        Response deleteResponse = client.deletePet(pet.getId());

        assertEquals(200, deleteResponse.statusCode(), "Питомец должен быть успешно удалён");
        SchemaChecker.assertMatchesSchema(deleteResponse, "schemas/delete-schema.json");
    }

    @Test
    @DisplayName("Удаление несуществующего питомца — ожидаем 404")
    void shouldReturn404WhenDeletingNonExistingPet() {
        Response deleteResponse = client.deletePet(99999999L);
        assertEquals(404, deleteResponse.statusCode(), "Ожидаем 404 при удалении несуществующего питомца");
    }

    @Test
    @DisplayName("Удаление с невалидным ID (строка вместо числа) — ожидаем 400")
    void shouldReturn400WhenDeletingWithInvalidId() {
        Response response = client.deletePetWithInvalidId("not-a-number");
        assertEquals(400, response.statusCode(), "Ожидаем 400 при невалидном формате ID");
    }
}