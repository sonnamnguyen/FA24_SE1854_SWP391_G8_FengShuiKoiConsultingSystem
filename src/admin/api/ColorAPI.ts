import api from "../../axious/axious";
import Color from "../../models/Color";

interface ResultInterface {
    result: Color[];
    pageTotal: number;
    totalElements: number;
}

export async function getAllColors(page: number, pageSize: number): Promise<ResultInterface | null> {
    const result: Color[] = [];
    try {
        const response = await api.get(`/colors?page=${page}&size=${pageSize}`);

        if (response.data.code === 1000) {
            const responseData = response.data.result.data;
            console.log("API Response:", response.data);
            const colors = responseData.map((colorData: any) => ({
                id: colorData.id,
                color: colorData.color,
                destiny: colorData.destiny
                    ? {
                        id: colorData.destiny.id,
                        destiny: colorData.destiny.destiny,
                        directions: colorData.destiny.directions?.map((direction: any) => ({
                            id: direction.id,
                            direction: direction.direction,
                        })) || [],
                        numbers: colorData.destiny.numbers?.map((number: any) => ({
                            id: number.id,
                            number: number.number,
                        })) || [],
                    }
                    : null,
                status: colorData.status,
                createdDate: colorData.createdDate,
                createdBy: colorData.createdBy,
                updatedBy: colorData.updatedBy,
            }));

            result.push(...colors);

            const pageTotal: number = response.data.result.totalPages || 1; // Default to 1 if not provided
            const totalElements: number = response.data.result.totalElements || result.length; // Fallback to result length if not provided

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

export async function findByColor(name: string, page: number, pageSize: number): Promise<ResultInterface | null> {
    const result: Color[] = [];

    try {
        const response = await api.get(`/colors/search-colors?name=${name}&page=${page}&size=${pageSize}`);
        if (response.data.code === 1000) {
            const responseData = response.data.result.data;

                responseData.forEach((colorData: any) => {
                    result.push({
                        id: colorData.id,
                        color: colorData.color,
                        destiny: {
                            id: colorData.destiny.id,
                            destiny: colorData.destiny.destiny,
                            directions: colorData.destiny.directions.map((direction: any) => ({
                                id: direction.id,
                                direction: direction.direction,
                            })),
                            numbers: colorData.destiny.numbers.map((number: any) => ({
                                id: number.id,
                                number: number.number,
                            })),
                        },
                        status: colorData.status,
                        createdDate: colorData.createdDate,
                        createdBy: colorData.createdBy,
                        updatedBy: colorData.updatedBy,
                    });
                });

                const pageTotal: number = response.data.result.totalPages || 1;  // Fallback to 1 if not provided
                const totalElements: number = response.data.result.totalElements || result.length;  // Fallback to result length

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
    

