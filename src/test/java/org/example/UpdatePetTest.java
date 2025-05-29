package org.example;

import org.example.api.PetApiClient;
import org.example.models.Pet;
import org.example.utils.HttpStatusCodes;
import org.example.utils.PetFactory;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PUT /pet — обновление питомца")
public class UpdatePetTest {

    private static PetApiClient petApiClient;
    private static Pet petToUpdate;
    private static Response response;

    @BeforeAll
    public static void prepare() {
        petApiClient = new PetApiClient();

        Pet originalPet = PetFactory.createDefaultPet();
        petApiClient.addPet(originalPet);
        originalPet.setName("UpdatedName");
        originalPet.setStatus("sold");

        petToUpdate = originalPet;
    }

    @Test
    @DisplayName("Успешное обновление существующего питомца")
    public void shouldUpdatePetSuccessfully() {
        response = petApiClient.updatePet(petToUpdate);

        assertEquals(HttpStatusCodes.OK, response.getStatusCode());

        Pet updatedPet = response.as(Pet.class);
        assertEquals(petToUpdate.getId(), updatedPet.getId());
        assertEquals("UpdatedName", updatedPet.getName());
        assertEquals("sold", updatedPet.getStatus());
    }

    @Test
    @DisplayName("Обновление удалённого питомца должно вернуть 404")
    public void shouldReturn404WhenUpdatingDeletedPet() {
        petApiClient.deletePet(petToUpdate.getId());

        response = petApiClient.updatePet(petToUpdate);

        assertEquals(HttpStatusCodes.NOT_FOUND, response.getStatusCode(), "Ожидали 404 при обновлении удалённого питомца");
    }

    @Test
    @DisplayName("Отправка невалидного тела должна вернуть 405")
    public void shouldReturn405WhenSendingInvalidBody() {
        Pet invalidPet = new Pet();

        response = petApiClient.updatePet(invalidPet);

        assertEquals(HttpStatusCodes.METHOD_NOT_ALLOWED, response.getStatusCode(), "Ожидали 405 при отправке невалидного тела");
    }

    @Test
    @DisplayName("Отправка невалидного тела (строка) должна вернуть ошибку (405 или 400)")
    public void shouldReturnErrorWhenSendingInvalidBodyAsString() {
        String invalidBody = "\"Just a plain string instead of JSON object\"";

        Response response = petApiClient.updatePetRawBody(invalidBody);

        // Проверяем либо 405, либо 400 — зависит от поведения сервера
        int status = response.getStatusCode();
        boolean isExpected = (status == HttpStatusCodes.METHOD_NOT_ALLOWED) || (status == HttpStatusCodes.BAD_REQUEST);
        System.out.println(response.getStatusCode());
        assertTrue(isExpected, "Ожидали 405 или 400 при отправке невалидного тела, получили: " + status);
    }
}