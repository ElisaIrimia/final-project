package com.elisa.petadoption.config;

import com.elisa.petadoption.entity.AppUser;
import com.elisa.petadoption.entity.Pet;
import com.elisa.petadoption.entity.PetStatus;
import com.elisa.petadoption.entity.Role;
import com.elisa.petadoption.entity.Shelter;
import com.elisa.petadoption.repository.AppUserRepository;
import com.elisa.petadoption.repository.PetRepository;
import com.elisa.petadoption.repository.ShelterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final AppUserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final PetRepository petRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AppUserRepository userRepository, ShelterRepository shelterRepository, PetRepository petRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.shelterRepository = shelterRepository;
        this.petRepository = petRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            createUser("admin", "admin@adoptihub.local", "admin123", Role.ROLE_ADMIN);
        }
        if (!userRepository.existsByUsername("elisa")) {
            createUser("elisa", "elisa@adoptihub.local", "user123", Role.ROLE_USER);
        }

        Shelter north = findOrCreateShelter("North Haven Rescue", "Cluj-Napoca", "north@adoptihub.local");
        Shelter paws = findOrCreateShelter("Second Chance Paws", "Brasov", "paws@adoptihub.local");

        pet("Mira", "Dog", "Mixed Shepherd", 3, "Calm, loyal and excellent with older children. Mira loves long walks and quiet evenings.", "/images/pets/mira.jpg", PetStatus.AVAILABLE, north);
        pet("Pixel", "Cat", "European Shorthair", 2, "Curious indoor cat with a playful personality and a suspicious relationship with cardboard boxes.", "/images/pets/pixel.jpg", PetStatus.AVAILABLE, paws);
        pet("Nori", "Rabbit", "Mini Lop", 1, "Gentle rabbit who needs a quiet home and someone patient with daily care.", "/images/pets/nori.jpg", PetStatus.AVAILABLE, north);
        pet("Bruno", "Dog", "Border Collie", 4, "Energetic and clever, Bruno is happiest when he has a game to learn or a trail to explore. He would thrive with an active family.", "/images/pets/bruno.jpg", PetStatus.AVAILABLE, north);
        pet("Luna", "Cat", "Maine Coon Mix", 3, "Affectionate and gentle, Luna enjoys sunny windows, soft blankets, and calm company. She is comfortable with respectful children.", "/images/pets/luna.jpg", PetStatus.AVAILABLE, paws);
        pet("Kiki", "Cat", "British Shorthair", 2, "A confident little character with a playful side. Kiki is looking for an indoor home where she can be the center of attention.", "/images/pets/kiki.jpg", PetStatus.AVAILABLE, paws);
    }

    private void createUser(String username, String email, String password, Role role) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);
    }

    private Shelter findOrCreateShelter(String name, String city, String email) {
        return shelterRepository.findAll().stream()
                .filter(shelter -> shelter.getName().equals(name))
                .findFirst()
                .orElseGet(() -> shelter(name, city, email));
    }

    private Shelter shelter(String name, String city, String email) {
        Shelter shelter = new Shelter();
        shelter.setName(name);
        shelter.setCity(city);
        shelter.setContactEmail(email);
        return shelterRepository.save(shelter);
    }

    private void pet(String name, String species, String breed, int age, String story, String imageUrl, PetStatus status, Shelter shelter) {
        Pet existingPet = petRepository.findAll().stream()
                .filter(pet -> pet.getName().equals(name))
                .findFirst()
                .orElse(null);
        if (existingPet != null) {
            if (existingPet.getImageUrl() == null || existingPet.getImageUrl().isBlank()) {
                existingPet.setImageUrl(imageUrl);
                petRepository.save(existingPet);
            }
            return;
        }

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setAge(age);
        pet.setStory(story);
        pet.setImageUrl(imageUrl);
        pet.setStatus(status);
        pet.setShelter(shelter);
        petRepository.save(pet);
    }
}
