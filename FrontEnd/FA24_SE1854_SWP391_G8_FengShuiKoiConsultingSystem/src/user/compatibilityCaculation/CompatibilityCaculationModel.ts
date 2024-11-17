import AnimalCompatibilityResponse from "./AnimalCompatibilityModel";

class CompatibilityResultResponse {
    yourDestiny: string;
    directionName: string;
    directionScore: number | null;
    directionExplanation: string | null;
    directionsAdvice: Set<string> | null;
    shapeName: string;
    shapeScore: number | null;
    shapeExplanation: string | null;
    shapesAdvice: Set<string> | null;
    number: number | null;
    numberScore: number | null;
    numberExplanation: string | null;
    numbersAdvice: Set<number> | null;
    animalAverageScore: number | null;
    animalCompatibilityResponse: AnimalCompatibilityResponse[] | null;
    animalAdvice: Set<string> | null;

    constructor(
        yourDestiny: string,
        directionName: string,
        directionScore: number | null,
        directionExplanation: string | null,
        directionsAdvice: Set<string> | null,
        shapeName: string,
        shapeScore: number | null,
        shapeExplanation: string | null,
        shapesAdvice: Set<string> | null,
        number: number | null,
        numberScore: number | null,
        numberExplanation: string | null,
        numbersAdvice: Set<number> | null,
        animalAverageScore: number | null,
        animalCompatibilityResponse: AnimalCompatibilityResponse[] | null,
        animalAdvice: Set<string> | null
    ) {
        this.yourDestiny = yourDestiny;
        this.directionName = directionName;
        this.directionScore = directionScore;
        this.directionExplanation = directionExplanation;
        this.directionsAdvice = directionsAdvice;
        this.shapeName = shapeName;
        this.shapeScore = shapeScore;
        this.shapeExplanation = shapeExplanation;
        this.shapesAdvice = shapesAdvice;
        this.number = number;
        this.numberScore = numberScore;
        this.numberExplanation = numberExplanation;
        this.numbersAdvice = numbersAdvice;
        this.animalAverageScore = animalAverageScore;
        this.animalCompatibilityResponse = animalCompatibilityResponse;
        this.animalAdvice = animalAdvice;
    }
}

export default CompatibilityResultResponse;
