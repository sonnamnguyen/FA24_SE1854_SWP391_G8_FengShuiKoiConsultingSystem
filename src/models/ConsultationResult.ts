import { ConsultationRequest } from './ConsultationRequest';
import { ConsultationRequestDetail } from './ConsultationRequestDetail';
import { Account } from './Account';
import { ConsultationCategory } from './ConsultationCategory';
import { ConsultationAnimal } from './ConsultationAnimal';
import { ConsultationShelter } from './ConsultationShelter';
import { Request } from './enums/Request';

export class ConsultationResult {
    consultationResultId: number;
    request: ConsultationRequest | null;
    requestDetail: ConsultationRequestDetail | null;
    account: Account | null;
    consultationCategory: ConsultationCategory | null;
    consultationDate: Date | null;
    consultantName: string | null;
    status: Request;
    description: string;
    createdDate: Date;
    createdBy: string;
    updatedDate: Date;
    updatedBy: string;
    consultationAnimals: Set<ConsultationAnimal>;
    consultationShelters: Set<ConsultationShelter>;

    constructor() {
        this.consultationResultId = 0;
        this.request = null;
        this.requestDetail = null;
        this.account = null;
        this.consultationCategory = null;
        this.consultationDate = null;
        this.consultantName = '';
        this.status = Request.PENDING;
        this.description = '';
        this.createdDate = new Date();
        this.createdBy = '';
        this.updatedDate = new Date();
        this.updatedBy = '';
        this.consultationAnimals = new Set<ConsultationAnimal>();
        this.consultationShelters = new Set<ConsultationShelter>();
    }
}
