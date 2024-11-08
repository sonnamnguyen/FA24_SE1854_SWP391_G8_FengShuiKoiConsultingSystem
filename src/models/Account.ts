import { Gender } from './enums/Gender';
import { Status } from './enums/Status';
import { Role } from './Role';
import { Bill } from './Bill';
import { ConsultationRequest } from './ConsultationRequest';
import { ConsultationResult } from './ConsultationResult';
import { Post } from './Post';

export class Account {
    accountId: number;
    userName: string;
    password: string;
    fullName: string;
    email: string;
    phoneNumber: string;
    gender: Gender | null;
    avatar: string | null;
    dob: Date | null;
    code: string | null;
    status: Status;
    createdDate: Date;
    createdBy: string;
    updatedDate: Date;
    updatedBy: string;
    roles: Set<Role>;
    bills: Set<Bill>;
    consultationRequests: Set<ConsultationRequest>;
    consultationResults: Set<ConsultationResult>;
    posts: Set<Post>;

    constructor() {
        this.accountId = 0;
        this.userName = '';
        this.password = '';
        this.fullName = '';
        this.email = '';
        this.phoneNumber = '';
        this.gender = null;
        this.avatar = null;
        this.dob = null;
        this.code = null;
        this.status = Status.INACTIVE;
        this.createdDate = new Date();
        this.createdBy = '';
        this.updatedDate = new Date();
        this.updatedBy = '';
        this.roles = new Set<Role>();
        this.bills = new Set<Bill>();
        this.consultationRequests = new Set<ConsultationRequest>();
        this.consultationResults = new Set<ConsultationResult>();
        this.posts = new Set<Post>();
    }
}
