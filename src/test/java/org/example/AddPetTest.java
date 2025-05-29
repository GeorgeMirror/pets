package org.example;

import static org.junit.jupiter.api.Assertions.*;
import io.restassured.response.Response;
import org.example.api.PetApiClient;
import org.example.models.Pet;
import org.example.utils.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;  // если используешь Allure

@DisplayName("API Tests for Adding Pets")
public class AddPetTest {

    private final PetApiClient petApiClient = new PetApiClient();

    @Test
    @DisplayName("Добавление нового питомца")
    @Description("Тест проверяет, что добавление нового питомца возвращает правильные данные в ответе")
    public void testAddPet() {
        // Используем конструктор с параметрами
        Pet pet = new Pet(12345, "Buddy", "available");

        Response response = petApiClient.addPet(pet);

        // Явно парсим поля из JSON-ответа
        int id = response.path("id");
        String name = response.path("name");
        String status = response.path("status");

        // Проверяем поля
        assertEquals(pet.getName(), name, "Имя питомца не совпадает");
        assertEquals(pet.getStatus(), status, "Статус питомца не совпадает");
        assertNotEquals(0, id, "ID питомца не должен быть 0");
        assertEquals(HttpStatusCodes.OK, response.statusCode(), "Ожидаем статус 200");
    }
}