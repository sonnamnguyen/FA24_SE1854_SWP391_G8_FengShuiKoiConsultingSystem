import { Status } from './enums/Status';
import { ConsultationRequest } from './ConsultationRequest';
import { Post } from './Post';
import { Bill } from './Bill';

export class Package {
    packageId: number;
    status: Status;
    packageName: string;
    price: number;
    description: string;
    createdDate: Date;
    createdBy: string;
    updatedDate: Date | null;
    updatedBy: string | null;
    consultationRequests: Set<ConsultationRequest>;
    posts: Set<Post>;
    bills: Set<Bill>;

    constructor() {
        this.packageId = 0;
        this.status = Status.ACTIVE;
        this.packageName = '';
        this.price = 0;
        this.description = '';
        this.createdDate = new Date();
        this.createdBy = '';
        this.updatedDate = null;
        this.updatedBy = null;
        this.consultationRequests = new Set<ConsultationRequest>();
        this.posts = new Set<Post>();
        this.bills = new Set<Bill>();
    }
}
