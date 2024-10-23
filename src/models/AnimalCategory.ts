import AnimalImage from "./AnimalImage";
import Color from "./Color";

class AnimalCategory {
    id?: number;
    animalCategoryName?: string;
    description?: string;
    origin?: string;
    status?: string;
    createdDate?: Date;
    createdBy?: string;
    updatedDate?: Date;
    updatedBy?: string;
    colors: Color[];
    animalImages: AnimalImage[]; 
    constructor(
        id?: number,
        animalCategoryName?: string,
        description?: string,
        origin?: string,
        status?: string,
        createdDate?: Date,
        createdBy?: string,
        updatedDate?: Date,
        updatedBy?: string,
        colors: Color[] = [],
        animalImages: AnimalImage[] = []
    ) {
        this.id = id;
        this.animalCategoryName = animalCategoryName;
        this.description = description;
        this.origin = origin;
        this.status = status;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
        this.colors = colors; 
        this.animalImages = animalImages;
    }
}

export default AnimalCategory;
