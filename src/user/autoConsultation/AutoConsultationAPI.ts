import AutoConsultationContainer from "./AutoConsultationContainer";
import AutoConsultation from "./AutoConsultationModel";
import AnimalCategory from "../../models/AnimalCategory";
import ShelterCategory from "../../models/ShelterCategory";
import AnimalImage from "../../models/AnimalImage";
import ShelterImage from "../../models/ShelterImage";
import api from "../../axious/axious";
import Shape from "../../models/Shape";


interface AnimalData {
    id: number;
    animalCategoryName: string;
    description: string;
    origin: string;
    animalImages: AnimalImage[]; 
}

interface ResultInterface {
    result: AutoConsultationContainer;
}

const parseAnimalCategory = (animalData: AnimalData): AnimalCategory => {
    return new AnimalCategory(
        animalData.id,
        animalData.animalCategoryName,
        animalData.description,
        animalData.origin,
        "",
        new Date(), 
        "",
        new Date(),
        "",
        [],
        animalData.animalImages
    );
};

interface ShelterData {
    id: number;
    shelterCategoryName: string;
    width: number;
    height: number;
    length: number;
    diameter: number;
    waterVolume: number;
    waterFiltrationSystem: string;
    description: string;
    shelterImages: ShelterImage[];
    
}

const parseShelterCategory = (shelterData: ShelterData): ShelterCategory => {
    return new ShelterCategory(
        shelterData.id,
        shelterData.shelterCategoryName,
        shelterData.width,
        shelterData.height,
        shelterData.length,
        shelterData.diameter,
        shelterData.waterVolume,
        shelterData.waterFiltrationSystem,
        shelterData.description,
        "",
        new Date(), 
        "",
        new Date(),
        "",
        new Shape(),
        shelterData.shelterImages
    );
};
export async function getAutoConsultationByYear(year: number): Promise<ResultInterface | null> {
    let result: AutoConsultationContainer | null = null;
    try {
        const response = await api(`/destinys/autoConsultation/${year}`);
        const data = response.data;

        if (data.code === 1000) { 
            const responseData = data.result;

            const consultation1 = new AutoConsultation(
                responseData.consultation1.numbers,
                responseData.consultation1.directions,
                responseData.consultation1.colors,
                responseData.consultation1.shapes,
                responseData.consultation1.shelters.map(parseShelterCategory),
                responseData.consultation1.animals.map(parseAnimalCategory)
            );

            const consultation2 = new AutoConsultation(
                responseData.consultation2.numbers,
                responseData.consultation2.directions,
                responseData.consultation2.colors,
                responseData.consultation2.shapes,
                responseData.consultation2.shelters.map(parseShelterCategory),
                responseData.consultation2.animals.map(parseAnimalCategory)
            );

            result = new AutoConsultationContainer(
                responseData.destiny,
                responseData.destinyTuongSinh,
                consultation1,
                consultation2
            );

            return { result };
        } else {
            console.error("API returned an error code: ", data.code);
            return null;
        }
    } catch (error) {
        console.error(error);
        return null;
    }
}
