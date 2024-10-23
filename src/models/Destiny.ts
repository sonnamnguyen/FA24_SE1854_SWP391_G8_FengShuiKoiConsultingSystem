import Direction from "./Direction";
import Number from "./Number";

class Destiny {
    id?: number;
    destiny?: string;
    directions: Direction[];
    numbers: Number[];
    constructor(
        id?: number,
        destiny?: string,
        directions: Direction[] = [],
        numbers: Number[] = []
    ) {
        this.id = id;
        this.destiny = destiny;
        this.directions = directions;
        this.numbers = numbers;
    }
}
export default Destiny