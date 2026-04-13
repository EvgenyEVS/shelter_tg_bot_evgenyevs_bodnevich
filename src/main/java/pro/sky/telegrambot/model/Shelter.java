package pro.sky.telegrambot.model;

public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PetType petType;
    private String shelterInfo;
    private String address;
    private String shelterSchedule; // расписание
    private String routeSchemaUrl; // Схема проезда в картинке. Хранится удаленно, получаем по URL
    private String contacts;
    private String safetyPrecautionsAtShelter;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Pet> pets = new ArrayList<>();


    // Helper methods
    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setShelter(this);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setShelter(null);
    }

}
