import AnimalCategory from "../../models/AnimalCategory";
import ShelterCategory from "../../models/ShelterCategory";


class AutoConsultation {
    numbers: number[];
    directions: string[];
    colors: string[];
    shapes: string[];
    shelters: ShelterCategory[];
    animals: AnimalCategory[];

    constructor(
        numbers: number[],
        directions: string[],
        colors: string[],
        shapes: string[],
        shelters: ShelterCategory[],
        animals: AnimalCategory[]
    ) {
        this.numbers = numbers;
        this.directions = directions;
        this.colors = colors;
        this.shapes = shapes;
        this.shelters = shelters;
        this.animals = animals;
    }
}
export default AutoConsultation;
