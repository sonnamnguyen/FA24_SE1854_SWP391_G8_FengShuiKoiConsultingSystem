import api from "../../axious/axious";
import Color from "../../models/Color";
import Shape from "../../models/Shape";

interface ResultInterface {
    result: Shape[];
    pageTotal: number;
    totalElements: number;
}

export async function getAllShapes(): Promise<ResultInterface | null> {
    const result: Shape[] = [];
    try {
        const response = await api(`/shapes`);
        const database = response.data;

        if (database.code === 1000) {
            const responseData = database.result.data;
            console.log("API Response:", response.data);
            const colors = responseData.map((shapeData: any) => ({
                id: shapeData.id,
                shape: shapeData.shape,
                destiny: shapeData.destiny
                    ? {
                        id: shapeData.destiny.id,
                        destiny: shapeData.destiny.destiny,
                        directions: shapeData.destiny.directions?.map((direction: any) => ({
                            id: direction.id,
                            direction: direction.direction,
                        })) || [],
                        numbers: shapeData.destiny.numbers?.map((number: any) => ({
                            id: number.id,
                            number: number.number,
                        })) || [],
                    }
                    : null,
                status: shapeData.status,
                createdDate: shapeData.createdDate,
                createdBy: shapeData.createdBy,
                updatedBy: shapeData.updatedBy,
            }));

            result.push(...colors);

            const pageTotal: number = database.result.totalPages || 1; // Default to 1 if not provided
            const totalElements: number = database.result.totalElements || result.length; // Fallback to result length if not provided

            return {
                result: result,
                pageTotal: pageTotal,
                totalElements: totalElements
            };
        } else {
            console.error("Unexpected response code: ", response.data.code);
            return null;
        }
    } catch (error) {
        console.error("Error fetching colors: ", error);
        return null;
    }
}

export async function findByShape(name: string): Promise<ResultInterface | null> {
    const result: Shape[] = [];

    try {
        const response = await api(`/shapes/shape-search?search=${name}`);
        const database = response.data;  
       

            if (database.code === 1000) {
                const responseData = database.result.data;
                console.log("API Response:", response.data);
                const colors = responseData.map((shapeData: any) => ({
                    id: shapeData.id,
                    shape: shapeData.shape,
                    destiny: shapeData.destiny
                        ? {
                            id: shapeData.destiny.id,
                            destiny: shapeData.destiny.destiny,
                            directions: shapeData.destiny.directions?.map((direction: any) => ({
                                id: direction.id,
                                direction: direction.direction,
                            })) || [],
                            numbers: shapeData.destiny.numbers?.map((number: any) => ({
                                id: number.id,
                                number: number.number,
                            })) || [],
                        }
                        : null,
                    status: shapeData.status,
                    createdDate: shapeData.createdDate,
                    createdBy: shapeData.createdBy,
                    updatedBy: shapeData.updatedBy,
                }));
    
                result.push(...colors);
    

                result.push(...colors);

                const pageTotal: number = database.result.totalPages || 1; // Default to 1 if not provided
                const totalElements: number = database.result.totalElements || result.length; // Fallback to result length if not provided
    
                return {
                    result: result,
                    pageTotal: pageTotal,
                    totalElements: totalElements
                };
            } else {
                console.error("Unexpected response code: ", response.data.code);
                return null;
            }
        } catch (error) {
            console.error("Error fetching colors: ", error);
            return null;
        }
    }
    
