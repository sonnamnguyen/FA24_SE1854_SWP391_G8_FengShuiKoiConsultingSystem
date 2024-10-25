import Destiny from "./Destiny";

class Color {
    id?: number;
    color?: string;
    destiny: Destiny; 
    status?: string;
    createdDate?: Date;
    createdBy?: string;
    updatedDate?: Date;
    updatedBy?: string;

    constructor(
        id?: number,
        color?: string,
        destiny?: Destiny, 
        status?: string,
        createdDate?: Date,
        createdBy?: string,
        updatedDate?: Date,
        updatedBy?: string,
    ) {
        this.id = id;
        this.color = color;
        this.destiny = destiny || new Destiny(); 
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
    }
}

export default Color;
