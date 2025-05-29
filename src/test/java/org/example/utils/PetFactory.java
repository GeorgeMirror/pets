package org.example.utils;

import org.example.models.Pet;

public class PetFactory {

    // Создать базового питомца с заданным ID, именем и статусом
    public static Pet createPet(Long id, String name, String status) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setStatus(status);
        return pet;
    }

    // Создать питомца с дефолтным именем и статусом
    public static Pet createDefaultPet() {
        return createPet(12345L, "Buddy", "available");
    }
}
