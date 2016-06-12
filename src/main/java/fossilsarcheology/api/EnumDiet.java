package fossilsarcheology.api;

public enum EnumDiet {
	/**Eats exclusively meat.*/
    CARNIVORE(3),
	/**Eats exclusively plants.*/
    HERBIVORE(0),
	/**Eats meat,eggs and plants.*/
    OMNIVORE(1),
	/**Eats exclusively fish.*/
    PISCIVORE(1),
	/**Eats meat and eggs.*/
    CARNIVORE_EGG(2),
	/**Eats arthropods.*/
    INSECTIVORE(0),
	/**Eats meat and fish.*/
    PISCCARNIVORE(3),
	/**Animal does not eat.*/
    NONE(0);

    public int fearIndex;

    EnumDiet(int fearIndex) {
        this.fearIndex = fearIndex;
    }
}
