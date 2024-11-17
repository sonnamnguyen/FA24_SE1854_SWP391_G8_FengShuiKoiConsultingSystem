import api from "../../axious/axious";
import ShelterCategory from "../../models/ShelterCategory";
import { getToken } from "../../service/localStorageService";

interface ResultInterface {
  result: ShelterCategory[];
  pageTotal: number;
  totalElements: number;
}

export async function getAllShelters(
  page: number,
  pageSize: number
): Promise<ResultInterface | null> {
  const result: ShelterCategory[] = [];

  try {
    const response = await api.get(`/shelters?page=${page}&size=${pageSize}`);
    if (response.data.code === 1000) {
      const responseData = response.data.result.data;
      result.push(
        ...responseData.map((shelter: any) => ({
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
              directions: shelter.shape.destiny.directions.map(
                (direction: any) => ({
                  id: direction.id,
                  direction: direction.direction,
                })
              ),
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
        }))
      );

      const pageTotal: number = response.data.result.totalPages || 1;
      const totalElements: number =
        response.data.result.totalElements || result.length;

      return {
        result: result,
        pageTotal: pageTotal,
        totalElements: totalElements,
      };
    } else {
      console.error("Error in response: ", response.data.message);
      return null;
    }
  } catch (error) {
    console.error("Error fetching shelters: ", error);
    return null;
  }
}

export async function findByShelterCategory(
  name: string,
  page: number,
  pageSize: number
): Promise<ResultInterface | null> {
  const endpoint: string = `/shelters/search-name?name=${encodeURIComponent(
    name
  )}&page=${page}&size=${pageSize}`;
  const result: ShelterCategory[] = [];

  try {
    const response = await api.get(endpoint); // Use endpoint here for consistency

    if (response.data.code === 1000) {
      const responseData = response.data.result.data;
      result.push(
        ...responseData.map((shelter: any) => ({
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
              directions: shelter.shape.destiny.directions.map(
                (direction: any) => ({
                  id: direction.id,
                  direction: direction.direction,
                })
              ),
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
        }))
      );

      const pageTotal: number = response.data.result.totalPages || 1;
      const totalElements: number =
        response.data.result.totalElements || result.length;

      return {
        result: result,
        pageTotal: pageTotal,
        totalElements: totalElements,
      };
    } else {
      console.error("Error in response: ", response.data.message);
      return null;
    }
  } catch (error) {
    console.error("Error fetching shelters: ", error);
    return null;
  }
}

export async function findByShelterCategoryDestiny(
  destinyList: string[] = [],
  page: number,
  pageSize: number
): Promise<ResultInterface | null> {
  const result: ShelterCategory[] = [];

  try {
    if (!Array.isArray(destinyList)) {
      throw new TypeError("Expected an array for destinyList");
    }

    // Convert destiny list to a comma-separated string
    const destinyQuery = destinyList.join(",");
    const response = await api.get(
      `/shelters/shelter-search?destiny=${destinyQuery}&page=${page}&size=${pageSize}`
    ); // Use endpoint here for consistency

    if (response.data.code === 1000) {
      const responseData = response.data.result.data;
      result.push(
        ...responseData.map((shelter: any) => ({
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
              directions: shelter.shape.destiny.directions.map(
                (direction: any) => ({
                  id: direction.id,
                  direction: direction.direction,
                })
              ),
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
        }))
      );

      const pageTotal: number = response.data.result.totalPages || 1;
      const totalElements: number =
        response.data.result.totalElements || result.length;

      return {
        result: result,
        pageTotal: pageTotal,
        totalElements: totalElements,
      };
    } else {
      console.error("Error in response: ", response.data.message);
      return null;
    }
  } catch (error) {
    console.error("Error fetching shelters: ", error);
    return null;
  }
}
export async function findByShelterCategoryDestinyAndName(
  destinyList: string[] = [],
  name: string,
  page: number,
  pageSize: number
): Promise<ResultInterface | null> {
  const result: ShelterCategory[] = [];

  try {
    if (!Array.isArray(destinyList)) {
      throw new TypeError("Expected an array for destinyList");
    }

    // Convert destiny list to a comma-separated string
    const destinyQuery = destinyList.join(",");
    const response = await api.get(
      `/shelters/shelter-search-name?destiny=${destinyQuery}&name=${name}&page=${page}&size=${pageSize}`
    ); // Use endpoint here for consistency

    if (response.data.code === 1000) {
      const responseData = response.data.result.data;
      result.push(
        ...responseData.map((shelter: any) => ({
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
              directions: shelter.shape.destiny.directions.map(
                (direction: any) => ({
                  id: direction.id,
                  direction: direction.direction,
                })
              ),
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
        }))
      );

      const pageTotal: number = response.data.result.totalPages || 1;
      const totalElements: number =
        response.data.result.totalElements || result.length;

      return {
        result: result,
        pageTotal: pageTotal,
        totalElements: totalElements,
      };
    } else {
      console.error("Error in response: ", response.data.message);
      return null;
    }
  } catch (error) {
    console.error("Error fetching shelters: ", error);
    return null;
  }
}
