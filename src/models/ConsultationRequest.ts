import { Account } from './Account';
import { Package } from './Package';
import { Gender } from './enums/Gender';
import { Request } from './enums/Request';
import { ConsultationRequestDetail } from './ConsultationRequestDetail';
import { ConsultationResult } from './ConsultationResult';
import { Bill } from './Bill';

export class ConsultationRequest {
    requestId: number;
    account: Account | null;
    packageId: Package | null;
    fullName: string;
    gender: Gender;
    yob: number;
    email: string;
    phone: string;
    description: string;
    status: Request;
    createdDate: Date;
    updatedDate: Date;
    createdBy: string;
    updatedBy: string;
    consultationRequestDetails: Set<ConsultationRequestDetail>;
    consultationResults: Set<ConsultationResult>;
    bills: Set<Bill>;

    constructor() {
        this.requestId = 0;
        this.account = null;
        this.packageId = null;
        this.fullName = '';
        this.gender = Gender.OTHER;
        this.yob = new Date().getFullYear();
        this.email = '';
        this.phone = '';
        this.description = '';
        this.status = Request.PENDING;
        this.createdDate = new Date();
        this.updatedDate = new Date();
        this.createdBy = '';
        this.updatedBy = '';
        this.consultationRequestDetails = new Set<ConsultationRequestDetail>();
        this.consultationResults = new Set<ConsultationResult>();
        this.bills = new Set<Bill>();
    }
}
