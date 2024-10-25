import AnimalCategory from "../../models/AnimalCategory";
import { getToken } from "../../service/localStorageService";

interface ResultInterface {
    result: AnimalCategory[];
    pageTotal: number;
    totalElements: number;
}

export async function getAllAnimals(): Promise<ResultInterface | null> {
    const endpoint: string = `http://localhost:9090/animals`;
    const result: AnimalCategory[] = [];

    try {
        const response = await fetch(endpoint, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${getToken()}` 
            },
        });

        if (response.ok) {
            const database = await response.json();
            if (database.code === 1000) {
                const responseData = database.result.data;
                for (const key in responseData) {
                    result.push({
                        id: responseData[key].id,
                        animalCategoryName: responseData[key].animalCategoryName,
                        description: responseData[key].description,
                        origin: responseData[key].origin,
                        status: responseData[key].status,
                        createdDate: responseData[key].createdDate,
                        createdBy: responseData[key].createdBy,
                        updatedBy: responseData[key].updatedBy,
                        colors: responseData[key].colors.map((color: any) => ({
                            id: color.id,
                            color: color.color,
                            destiny: color.destiny ? color.destiny.destiny : null
                        })),
                        
                        animalImages: responseData[key].animalImages.map((image: any) => ({
                            id: image.id,
                            imageUrl: image.imageUrl,
                        })),
                    });
                }
                const pageTotal: number = database.result.totalPages;  
                const totalElements: number = database.result.totalElements;

                return {
                    result: result,
                    pageTotal: pageTotal,
                    totalElements: totalElements
                };
            }
        } else {
            console.error("Failed to fetch data: ", response.status);
            return null;
        }
    } catch (error) {
        console.error("Error fetching animals: ", error);
        return null; // Indicate failure
    }
    return null;
}

// Hàm tìm kiếm động vật theo category
export async function findByAnimalCategory(name: string): Promise<ResultInterface | null> {
    const endpoint: string = `http://localhost:9090/animals/animal-search?search=${name}`;
    const result: AnimalCategory[] = [];

    try {
        const response = await fetch(endpoint, {
            method: "GET", 
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${getToken()}`
            },
        });

        if (response.ok) {
            const database = await response.json();
            if (database.code === 1000) {
                const responseData = database.result.data;
                for (const key in responseData) {
                    result.push({
                        id: responseData[key].id,
                        animalCategoryName: responseData[key].animalCategoryName,
                        description: responseData[key].description,
                        origin: responseData[key].origin,
                        status: responseData[key].status,
                        createdDate: responseData[key].createdDate,
                        createdBy: responseData[key].createdBy,
                        updatedDate: responseData[key].updatedDate,
                        updatedBy: responseData[key].updatedBy,
                        colors: responseData[key].colors.color,
                        animalImages: responseData[key].animalImages.imageUrl
                    });
                }
                const pageTotal: number = database.result.totalPages;
                const totalElements: number = database.result.totalElements;

                return {
                    result: result,
                    pageTotal: pageTotal,
                    totalElements: totalElements
                };
            }
        } else {
            console.error("Failed to fetch data: ", response.status);
            return null;
        }
    } catch (error) {
        console.error("Error fetching animals: ", error);
        return null; 
    }

    return null;
}
