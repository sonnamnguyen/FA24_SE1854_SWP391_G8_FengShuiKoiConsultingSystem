import AutoConsultation from "./AutoConsultationModel";

class AutoConsultationContainer {
    destiny: string;
    destinyTuongSinh: string;
    consultation1: AutoConsultation;
    consultation2: AutoConsultation;
    constructor(
        destiny: string,
        destinyTuongSinh: string,
        AutoConsultation1: AutoConsultation,
        AutoConsultation2: AutoConsultation
    ) {
        this.destiny = destiny;
        this.destinyTuongSinh = destinyTuongSinh;
        this.consultation1 = AutoConsultation1;
        this.consultation2 = AutoConsultation2;
    }
}
export default AutoConsultationContainer;