import CompatibilityResultResponse from "./CompatibilityCaculationModel";
import AnimalCompatibilityResponse from "./AnimalCompatibilityModel";
import api from "../../axious/axious";

interface ResultInterface {
    result: CompatibilityResultResponse;
}
interface AnimalRequest {
    animalId: number;
    animalName: string;
}


interface CompatibilityRequest {
    directionId: number | null;        
    directionName: string | null;     
    shapeId: number | null;            
    shapeName: string | null;           
    numberId: number | null;            
    numberName: string | null;          
    animal: AnimalRequest[] | [];   
}

export async function getCompatibilityResult(year: number, compatibilityRequest: CompatibilityRequest): Promise<ResultInterface | null> {
    let result: CompatibilityResultResponse | null = null;
    try {
        const response = await api.post(`/destinys/compatibility/${year}`, compatibilityRequest);
        const data = response.data;
        if (data.code === 1000) {
            const responseData = data.result;
            result = new CompatibilityResultResponse(
                responseData.yourDestiny,
                responseData.directionName || "",
                responseData.directionScore,
                responseData.directionExplanation || null,
                responseData.directionsAdvice ? new Set(responseData.directionsAdvice) : null,
                responseData.shapeName || "", 
                responseData.shapeScore,
                responseData.shapeExplanation || null,
                responseData.shapesAdvice ? new Set(responseData.shapesAdvice) : null,
                responseData.number || null,
                responseData.numberScore,
                responseData.numberExplanation || null,
                responseData.numbersAdvice ? new Set(responseData.numbersAdvice) : null,
                responseData.animalAverageScore,
                responseData.animalCompatibilityResponse
                    ? responseData.animalCompatibilityResponse.map((ac: AnimalCompatibilityResponse) =>
                        new AnimalCompatibilityResponse(
                            ac.animalScore,
                            ac.animalName || "", 
                            ac.animalColors || [], 
                            ac.colorCompatibilityResponses || []
                        )
                    )
                    : [], 
                responseData.animalAdvice ? new Set(responseData.animalAdvice) : null
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