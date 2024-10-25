import api from "../../axious/axious";
import AnimalCategory from "../../models/AnimalCategory";
import ShelterCategory from "../../models/ShelterCategory";
import { getToken } from "../../service/localStorageService";

interface ResultInterface {
    result: ShelterCategory[];
    pageTotal: number;
    totalElements: number;
}

export async function getAllShelters(): Promise<ResultInterface | null> {
    const result: ShelterCategory[] = [];

    try {
        const response = await api(`/shelters`);

        const database = response.data;  
        if (database.code === 1000) {
            const responseData = database.result.data;
            result.push(...responseData.map((shelter: any) => ({
                id: shelter.id,
                shelterCategoryName: shelter.shelterCategoryName,
                width: shelter.width,
                height: shelter.height,
                length: shelter.length,
                diameter: shelter.diameter,
                waterVolume: shelter.waterVolume,
                waterFiltrationSystem: shelter.waterFiltrationSystem,
                description: shelter.description,
                status: shelter.status,
                createdDate: shelter.createdDate,
                createdBy: shelter.createdBy,
                updatedDate: shelter.updatedDate,
                updatedBy: shelter.updatedBy,
                shape: {
                    id: shelter.shape.id,
                    destiny: {
                        id: shelter.shape.destiny.id,
                        destiny: shelter.shape.destiny.destiny,
                        directions: shelter.shape.destiny.directions.map((direction: any) => ({
                            id: direction.id,
                            direction: direction.direction,
                        })),
                        numbers: shelter.shape.destiny.numbers.map((number: any) => ({
                            id: number.id,
                            number: number.number,
                        })),
                    },
                    shape: shelter.shape.shape,
                },
                shelterImages: shelter.shelterImages.map((image: any) => ({
                    id: image.id,
                    imageUrl: image.imageUrl,
                })),
            })));

            const pageTotal: number = database.result.totalPages || 1;
            const totalElements: number = database.result.totalElements || result.length;

            return {
                result: result,
                pageTotal: pageTotal,
                totalElements: totalElements
            };
        } else {
            console.error("Error in response: ", database.message);
            return null;
        }
    } catch (error) {
        console.error("Error fetching shelters: ", error);
        return null;
    }
}

export async function findByShelterCategory(name: string): Promise<ResultInterface | null> {
    const endpoint: string = `/shelters?name=${encodeURIComponent(name)}`;
    const result: ShelterCategory[] = [];

    try {
        const response = await api(endpoint);  // Use endpoint here for consistency
        const database = response.data;

        if (database.code === 1000) {
            const responseData = database.result.data;

            // Use map to construct the result array
            result.push(...responseData.map((shelter: any) => ({
                id: shelter.id,
                shelterCategoryName: shelter.shelterCategoryName,
                width: shelter.width,
                height: shelter.height,
                length: shelter.length,
                diameter: shelter.diameter,
                waterVolume: shelter.waterVolume,
                waterFiltrationSystem: shelter.waterFiltrationSystem,
                description: shelter.description,
                status: shelter.status,
                createdDate: shelter.createdDate,
                createdBy: shelter.createdBy,
                updatedDate: shelter.updatedDate,
                updatedBy: shelter.updatedBy,
                shape: {
                    id: shelter.shape.id,
                    destiny: {
                        id: shelter.shape.destiny.id,
                        destiny: shelter.shape.destiny.destiny,
                        directions: shelter.shape.destiny.directions.map((direction: any) => ({
                            id: direction.id,
                            direction: direction.direction,
                        })),
                        numbers: shelter.shape.destiny.numbers.map((number: any) => ({
                            id: number.id,
                            number: number.number,
                        })),
                    },
                    shape: shelter.shape.shape,
                },
                shelterImages: shelter.shelterImages.map((image: any) => ({
                    id: image.id,
                    imageUrl: image.imageUrl,
                })),
            })));

            const pageTotal: number = database.result.totalPages || 1;  // Fallback to 1 if not provided
            const totalElements: number = database.result.totalElements || result.length;  // Fallback to result length

            return {
                result: result,
                pageTotal: pageTotal,
                totalElements: totalElements
            };
        } else {
            console.error("Error in response: ", database.message);
            return null;
        }
    } catch (error) {
        console.error("Error fetching shelters: ", error);
        return null;  // Indicate failure
    }
}

