import Shape from "./Shape";
import ShelterImage from "./ShelterImage";

class ShelterCategory {
    id?: number;
    shelterCategoryName?: string;
    width?: number;
    height?: number;
    length?: number;
    diameter?: number;
    waterVolume?: number;
    waterFiltrationSystem?: string;
    description?: string;
    status?: string;
    createdDate?: Date;
    createdBy?: string;
    updatedDate?: Date;
    updatedBy?: string;
    shape?: Shape;
    shelterImages?: ShelterImage[]; 

    constructor(
        id?: number,
        shelterCategoryName?: string,
        width?: number,
        height?: number,
        length?: number,
        diameter?: number,
        waterVolume?: number,
        waterFiltrationSystem?: string,
        description?: string,
        status?: string,
        createdDate?: Date,
        createdBy?: string,
        updatedDate?: Date,
        updatedBy?: string,
        shape?: Shape, 
        shelterImages?: ShelterImage[], 
    ) {
        this.id = id;
        this.shelterCategoryName = shelterCategoryName;
        this.width = width;
        this.height = height;
        this.length = length;
        this.diameter = diameter;
        this.waterVolume = waterVolume;
        this.waterFiltrationSystem = waterFiltrationSystem;
        this.description = description; 
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
        this.shape = shape; 
        this.shelterImages = shelterImages; 
    }
}

export default ShelterCategory;
