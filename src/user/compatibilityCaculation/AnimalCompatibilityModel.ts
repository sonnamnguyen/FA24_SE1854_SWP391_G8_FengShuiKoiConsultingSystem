class AnimalCompatibilityResponse {
    animalScore: number;
    animalName: string;
    animalColors: string[];
    colorCompatibilityResponses: string[];

    constructor(animalScore: number, animalName: string, animalColors: string[], colorCompatibilityResponses: string[]) {
        this.animalScore = animalScore;
        this.animalName = animalName;
        this.animalColors = animalColors;
        this.colorCompatibilityResponses = colorCompatibilityResponses;
    }
}

export default AnimalCompatibilityResponse