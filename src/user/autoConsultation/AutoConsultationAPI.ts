import AutoConsultationContainer from "./AutoConsultationContainer";
import AutoConsultation from "./AutoConsultationModel";
import api from "../../axious/axious";



interface ResultInterface {
    result: AutoConsultationContainer;
}
export async function getAutoConsultationByYear(year: number): Promise<ResultInterface | null> {
    let result: AutoConsultationContainer | null = null;
    try {
        const response = await api(`/destinys/autoConsultation/${year}`);
        const data= response.data;
            if (data.code === 1000) { 
                const responseData = data.result;
                result = new AutoConsultationContainer(
                    responseData.destiny,
                    responseData.destinyTuongSinh,
                    new AutoConsultation(
                        responseData.consultation1.numbers,
                        responseData.consultation1.directions,
                        responseData.consultation1.colors,
                        responseData.consultation1.shapes,
                        responseData.consultation1.shelters,
                        responseData.consultation1.animals
                    ),
                    new AutoConsultation(
                        responseData.consultation2.numbers,
                        responseData.consultation2.directions,
                        responseData.consultation2.colors,
                        responseData.consultation2.shapes,
                        responseData.consultation2.shelters,
                        responseData.consultation2.animals
                    )
                );
                return { result };
            } else {
                console.error("API returned an error code: ", data.code);
                return null;
            }
    } catch (error) {
        console.error( error);
        return null;
    }
}