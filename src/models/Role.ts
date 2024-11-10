import { Account } from './Account';

export class Role {
    id: number;
    name: string;
    accounts: Set<Account>;

    constructor() {
        this.id = 0;
        this.name = '';
        this.accounts = new Set<Account>();
    }
}
