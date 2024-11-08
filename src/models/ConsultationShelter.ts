import { ConsultationResult } from './ConsultationResult';
import { ShelterCategory } from './ShelterCategory';
import { Direction } from './Direction';
import { Request } from './enums/Request';

export class ConsultationShelter {
    consultationShelterId: number;
    consultationResult: ConsultationResult | null;
    shelterCategory: ShelterCategory | null;
    directions: Set<Direction>;
    description: string | null;
    status: Request;
    createdDate: Date;
    createdBy: string;
    updatedDate: Date;
    updatedBy: string;

    constructor() {
        this.consultationShelterId = 0;
        this.consultationResult = null;
        this.shelterCategory = null;
        this.directions = new Set<Direction>();
        this.description = null;
        this.status = Request.CANCELLED;
        this.createdDate = new Date();
        this.createdBy = '';
        this.updatedDate = new Date();
        this.updatedBy = '';
    }
}
