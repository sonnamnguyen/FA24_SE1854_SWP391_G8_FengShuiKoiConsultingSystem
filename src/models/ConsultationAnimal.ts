import { ConsultationResult } from './ConsultationResult';
import { AnimalCategory } from './AnimalCategory';
import { NumberEntity } from './Number'; // Renamed to avoid conflict with native `Number` type
import { Request } from './enums/Request';

export class ConsultationAnimal {
    consultationAnimalId: number;
    consultationResult: ConsultationResult | null;
    animalCategory: AnimalCategory | null;
    description: string | null;
    numbers: Set<NumberEntity>;
    status: Request;
    createdDate: Date;
    createdBy: string;
    updatedDate: Date | null;
    updatedBy: string | null;

    constructor() {
        this.consultationAnimalId = 0;
        this.consultationResult = null;
        this.animalCategory = null;
        this.description = null;
        this.numbers = new Set<NumberEntity>();
        this.status = Request.CANCELLED;
        this.createdDate = new Date();
        this.createdBy = '';
        this.updatedDate = null;
        this.updatedBy = null;
    }
}
