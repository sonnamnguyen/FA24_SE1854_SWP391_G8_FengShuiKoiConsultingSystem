import api from "../../axious/axious";
import DestinyTuongKhac from "../../models/DestinyTuongKhac";
import DestinyTuongSinh from "../../models/DestinyTuongSinh";

// Define type for Destiny
interface DestinyData {
    id: number;
    destiny: string;
    directions: { id: number; direction: string }[];  // Directions data
    numbers: { id: number; number: number }[];        // Numbers data
    destinyTuongSinh: DestinyTuongSinh[]; // Array of DestinyTuongSinh models
    destinyTuongKhac: DestinyTuongKhac[]; // Array of DestinyTuongKhac models
}

export async function getDestiny(destiny: string): Promise<DestinyData | null> {
    try {
        const response = await api.get(`/destinys/${destiny}`);

        if (response.data.code === 1000) {
            const responseData = response.data.result;

            // Log the API response for debugging
            console.log("API Response:", response.data);

            const destinyData: DestinyData = {
                id: responseData.id,
                destiny: responseData.destiny,
                directions: responseData.directions || [],  
                numbers: responseData.numbers || [],        
                // Ensure to map the destinyTuongSinh and destinyTuongKhac correctly
                destinyTuongSinh: responseData.destinyTuongSinhs?.map((tuongSinh: any) => ({
                    name: tuongSinh.name // Ensure 'name' exists in the response
                })) || [], // Default to empty array if undefined
                destinyTuongKhac: responseData.destinyTuongKhacs?.map((tuongKhac: any) => ({
                    name: tuongKhac.name // Ensure 'name' exists in the response
                })) || [], // Default to empty array if undefined
            };

            return destinyData;
        } else {
            console.error("Unexpected response code: ", response.data.code);
            return null;
        }
    } catch (error) {
        console.error("Error fetching destiny: ", error);
        return null;
    }
}
