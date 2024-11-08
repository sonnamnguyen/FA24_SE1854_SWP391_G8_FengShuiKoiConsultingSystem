import { Account } from './Account';
import { Payment } from './Payment';
import { BillStatus } from './enums/BillStatus';
import { Package } from './Package';
import { ConsultationRequest } from './ConsultationRequest';

export class Bill {
    billId: number;
    account: Account | null;
    payment: Payment | null;
    subAmount: number | null;
    status: BillStatus;
    vat: number | null;
    vatAmount: number | null;
    totalAmount: number;
    createdDate: Date;
    createdBy: string;
    updatedDate: Date | null;
    updatedBy: string | null;
    packageFields: Set<Package>;
    consultationRequest: ConsultationRequest | null;

    constructor() {
        this.billId = 0;
        this.account = null;
        this.payment = null;
        this.subAmount = null;
        this.status = BillStatus.PENDING;
        this.vat = null;
        this.vatAmount = null;
        this.totalAmount = 0;
        this.createdDate = new Date();
        this.createdBy = '';
        this.updatedDate = null;
        this.updatedBy = null;
        this.packageFields = new Set<Package>();
        this.consultationRequest = null;
    }
}
