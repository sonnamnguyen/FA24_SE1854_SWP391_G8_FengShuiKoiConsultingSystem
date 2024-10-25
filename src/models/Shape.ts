import Destiny from "./Destiny";

class Shape {
    id?: number;
    destiny?: Destiny; 
    shape?: string;
    status?: string;
    createdDate?: Date;
    createdBy?: string;
    updatedDate?: Date;
    updatedBy?: string;

    constructor(
        id?: number,
        destiny?: Destiny, 
        shape?: string,
        status?: string,
        createdDate?: Date,
        createdBy?: string,
        updatedDate?: Date,
        updatedBy?: string,
    ) {
        this.id = id;
        this.destiny = destiny; 
        this.shape = shape;
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
    }
}

export default Shape;
