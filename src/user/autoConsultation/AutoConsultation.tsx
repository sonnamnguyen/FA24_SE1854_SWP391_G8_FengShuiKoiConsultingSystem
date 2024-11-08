import React, { useState } from "react";
import { getAutoConsultationByYear } from "./AutoConsultationAPI";
import AutoConsultationContainer from "./AutoConsultationContainer";
import "./AutoConsultation.css"
import AnimalCategory from "../../models/AnimalCategory";
import ShelterCategory from "../../models/ShelterCategory";




const AutoConsultationComponent: React.FC = () => {
    const currentYear = new Date().getFullYear();
    const [year, setYear] = useState<number | string>('');
    const [consultationData, setConsultationData] = useState<AutoConsultationContainer | null>(null);
    const [error, setError] = useState<string>("");
    const years = [];
    const [isAnimalModalVisible, setAnimalModalVisible] = useState(false);
    const [isShelterModalVisible, setShelterModalVisible] = useState(false);
    const [selectedAnimal, setSelectedAnimal] = useState<AnimalCategory | null>(null);
    const [selectedShelter, setSelectedShelter] = useState<ShelterCategory | null>(null);

    const showAnimalModal = (animal: AnimalCategory) => {
        setSelectedAnimal(animal);
        setAnimalModalVisible(true);
    };

    const showShelterModal = (shelter: ShelterCategory) => {
        setSelectedShelter(shelter);
        setShelterModalVisible(true);
    };

    const handleAnimalModalClose = () => setAnimalModalVisible(false);
    const handleShelterModalClose = () => setShelterModalVisible(false);


    for (let i = 1930; i <= currentYear; i++) {
        years.push(i);
    }

    const handleYearChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setYear(Number(event.target.value));
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError("");
        setConsultationData(null);
        const parsedYear = Number(year);
        if (year === '' || isNaN(parsedYear)) {
            setError("Invalid year. Please select a valid year.");
            return;
        }
        const result = await getAutoConsultationByYear(parsedYear);
        if (result) {
            setConsultationData(result.result);
        } else {
            setError("Failed to fetch data. Please try again.");
        }
    };

    return (
        <div className="autoConsultation-container">
            <form className="AutoConsultationForm" onSubmit={handleSubmit}>
                <div>
                    <h1 className="titleDestiny">Tra Cứu Mệnh Ngũ Hành</h1>
                    <label htmlFor="year">Choose Year:</label>
                    <select id="year" value={year} onChange={handleYearChange}>
                        <option>-- Select a Year --</option>
                        {years.map((y) => (
                            <option key={y} value={y}>
                                {y}
                            </option>
                        ))}
                    </select>
                </div>
                <button className="autoConsultation" type="submit">Kiểm Tra</button>
            </form>

            {error && (
                <>
                    <div className="ac-error-overlay" onClick={() => setError("")}></div>
                    <div className="ac-error-popup">
                        <div className="ac-error-icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2" color="#f44336">
                                <circle cx="12" cy="12" r="10" stroke="#f44336" strokeWidth="2" fill="none"></circle>
                                <line x1="15" y1="9" x2="9" y2="15" stroke="#f44336" strokeWidth="2"></line>
                                <line x1="9" y1="9" x2="15" y2="15" stroke="#f44336" strokeWidth="2"></line>
                            </svg>
                        </div>
                        <p className="ac-error-text">{error}</p>
                        <button className="ac-error-button" onClick={() => setError("")}>OK</button>
                    </div>
                </>
            )}            {consultationData && (
                <div className="consultation-results">
                    <h2 className="titleDestiny2">Mệnh Ngũ Hành {consultationData.destiny}</h2>
                    <div id={consultationData.destiny}>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                    </div>
                    <div id="fire" className={consultationData.destiny}>
                        <div id={consultationData.destiny} className="red flame"></div>
                        <div id={consultationData.destiny} className="orange flame"></div>
                        <div id={consultationData.destiny} className="yellow flame"></div>
                        <div id={consultationData.destiny} className="white flame"></div>
                        <div id={consultationData.destiny} className="blue circle"></div>
                        <div id={consultationData.destiny} className="black circle"></div>
                    </div>
                    <div id="water" className={consultationData.destiny}>
                        <div id={consultationData.destiny} className="wave_1 outer-shadow">
                            <div id={consultationData.destiny} className="inner-shadow align-center"></div>
                        </div>
                        <div id={consultationData.destiny} className="wave_2 outer-shadow align-center">
                            <div id={consultationData.destiny} className="inner-shadow align-center"></div>
                        </div>
                        <div id={consultationData.destiny} className="wave_3 outer-shadow align-center">
                            <div id={consultationData.destiny} className="inner-shadow align-center"></div>
                        </div>
                        <div id={consultationData.destiny} className="drop align-center"></div>
                    </div>
                    <div id={consultationData.destiny} className="spaceForRock">
                        <div id="earth" className={consultationData.destiny}></div>
                    </div>
                    <div id="shield" className={consultationData.destiny}>
                        <div id={consultationData.destiny} className="shield__inner"></div>
                    </div>
                    <div>
                        <h3 className="titleDestiny2">1. Số lượng cá</h3>
                        <p>Đối với người mệnh {consultationData.destiny}, số lượng cá nuôi phù hợp không chỉ hỗ trợ vận khí mà còn kích hoạt phong thủy, giúp thu hút tài lộc và tạo ra sự hài hòa trong cuộc sống.</p>
                        <p>Các số may mắn thuộc hành {consultationData.destiny}, như {consultationData.consultation2.numbers.join(", ")} sẽ góp phần ổn định và tăng cường năng lượng bản mệnh của bạn. Điều này đem lại sự bền vững và thuận lợi, giúp bạn đạt được hòa hợp và phát triển.</p>
                        <p>Trong khi đó, các số đại diện cho hành {consultationData.destinyTuongSinh}, như {consultationData.consultation1.numbers.join(", ")}, có sức mạnh tương sinh, bổ trợ mệnh {consultationData.destiny}. Chọn số lượng cá theo những con số này sẽ mang lại sự phát đạt, gia tăng vận may và mở rộng cơ hội thành công trong sự nghiệp.</p>
                        <p>Từ 11 con cá koi trở đi, bỏ chữ số hàng chục, lấy theo chữ số hàng đơn vị. Ví dụ: 11, 15, 38, 40, 49 con…. Tính thành 1, 5, 8, 4 , 9 con. Sau đó tiếp tục dựa vào bảng ở trên để tính.</p>
                    </div>
                    <div>
                        <h3 className="titleDestiny2">1. Số lượng cá</h3>
                        <p>Đối với người mệnh {consultationData.destiny}, số lượng cá nuôi phù hợp không chỉ hỗ trợ vận khí mà còn kích hoạt phong thủy, giúp thu hút tài lộc và tạo ra sự hài hòa trong cuộc sống.</p>
                        <p>Các số may mắn thuộc hành {consultationData.destiny}, như {consultationData.consultation2.numbers.join(", ")}, sẽ góp phần ổn định và tăng cường năng lượng bản mệnh của bạn. Điều này đem lại sự bền vững và thuận lợi, giúp bạn đạt được hòa hợp và phát triển.</p>
                        <p>Trong khi đó, các số đại diện cho hành {consultationData.destinyTuongSinh}, như {consultationData.consultation1.numbers.join(", ")}, có sức mạnh tương sinh, bổ trợ mệnh {consultationData.destiny}. Chọn số lượng cá theo những con số này sẽ mang lại sự phát đạt, gia tăng vận may và mở rộng cơ hội thành công trong sự nghiệp.</p>
                        <p>Từ 11 con cá koi trở đi, bỏ chữ số hàng chục, lấy theo chữ số hàng đơn vị. Ví dụ: 11, 15, 38, 40, 49 con…. Tính thành 1, 5, 8, 4 , 9 con. Sau đó tiếp tục dựa vào bảng ở trên để tính.</p>
                    </div>
                    <div>
                        <h3 className="titleDestiny2">2. Màu sắc và loài cá</h3>
                        <p>Những loài cá và màu sắc tương sinh và tương hợp với mệnh {consultationData.destiny}:</p>
                        <ul>
                            <li>Cá {consultationData.consultation2.colors.join(", ")}: Những màu này thuộc hành {consultationData.destiny}, giúp gia tăng sự ổn định và bình yên.</li>
                            <p>Các loài cá phù hợp:
                                {consultationData.consultation2.animals.map((animal: AnimalCategory, index: number) => (
                                    <React.Fragment key={animal.id}>
                                        {" "}
                                        <button className="autoConsultation-view" onClick={() => showAnimalModal(animal)}>
                                            {animal.animalCategoryName}
                                        </button>
                                        {index < consultationData.consultation2.animals.length - 1 && ", "}
                                    </React.Fragment>
                                ))}
                            </p>
                            <li>Cá màu {consultationData.consultation1.colors.join(", ")}: Những màu này thuộc hành {consultationData.destinyTuongSinh}, vì {consultationData.destinyTuongSinh} sinh {consultationData.destiny}, tạo sự hỗ trợ và phát triển cho người mệnh {consultationData.destiny}.</li>
                            <p>Các loài cá phù hợp:
                                {consultationData.consultation1.animals.map((animal: AnimalCategory, index: number) => (
                                    <React.Fragment key={animal.id}>
                                        {" "}
                                        <button className="autoConsultation-view" onClick={() => showAnimalModal(animal)}>
                                            {animal.animalCategoryName}
                                        </button>
                                        {index < consultationData.consultation1.animals.length - 1 && ", "}
                                    </React.Fragment>
                                ))}
                            </p>
                        </ul>
                    </div>
                    <div>
                        <h3 className="titleDestiny2">3. Hướng hồ</h3>
                        <p>Hướng hồ cá cho người mệnh {consultationData.destiny} nên đặt ở các hướng thuộc hành {consultationData.destiny} hoặc hành {consultationData.destinyTuongSinh}:</p>
                        <ul>
                            <li>Hướng {consultationData.consultation2.directions.join(", ")}: Thuộc hành {consultationData.destiny}, rất tốt cho sự thăng tiến và tài lộc.</li>
                            <li>Hướng {consultationData.consultation1.directions.join(", ")}: sẽ hỗ trợ và giúp gia tăng vượng khí cho mệnh {consultationData.destiny}.</li>
                        </ul>
                    </div>
                    <div>
                        <h3 className="titleDestiny2">4. Hình dáng hồ</h3>
                        <ul>
                            <li>Hình {consultationData.consultation2.shapes.join(", ")}, vì đây là các hình tượng của hành Thủy, giúp kích hoạt sự hài hòa và lưu thông năng lượng.</li>
                            <p>Các mẫu hồ chúng tôi sưu tầm:
                                {consultationData.consultation2.shelters.map((shelter: ShelterCategory, index: number) => (
                                    <React.Fragment key={shelter.id}>
                                        {" "}
                                        <button className="autoConsultation-view" onClick={() => showShelterModal(shelter)}>
                                            {shelter.shelterCategoryName}
                                        </button>
                                        {index < consultationData.consultation2.shelters.length - 1 && ", "}
                                    </React.Fragment>
                                ))}
                            </p>
                            <li>Hình dáng thuộc hành {consultationData.destinyTuongSinh} như hình {consultationData.consultation1.shapes.join(", ")} cũng rất phù hợp, vì {consultationData.destinyTuongSinh} sinh {consultationData.destiny}, tạo ra sự tương sinh mạnh mẽ, mang lại sự thịnh vượng và hỗ trợ cho vận khí của gia chủ.</li>
                            <p>Các mẫu hồ chúng tôi sưu tầm:
                                {consultationData.consultation1.shelters.map((shelter: ShelterCategory, index: number) => (
                                    <React.Fragment key={shelter.id}>
                                        {" "}
                                        <button className="autoConsultation-view" onClick={() => showShelterModal(shelter)}>
                                            {shelter.shelterCategoryName}
                                        </button>
                                        {index < consultationData.consultation1.shelters.length - 1 && ", "}
                                    </React.Fragment>
                                ))}
                            </p>
                        </ul>
                    </div>
                    {selectedAnimal && (
                        <div className="popup-overlay" onClick={() => setSelectedAnimal(null)}>
                            <div className="popup-content" onClick={(e) => e.stopPropagation()}>
                                <h3>{selectedAnimal.animalCategoryName}</h3>
                                <p>Description: {selectedAnimal.description}</p>
                                <p>Origin: {selectedAnimal.origin}</p>
                                <p>Images:</p>
                                {selectedAnimal.animalImages?.length ? (
                                    selectedAnimal.animalImages.map((img, index) => (
                                        <img
                                            key={index}
                                            src={img.imageUrl}
                                            alt={`${selectedAnimal.animalCategoryName} image ${index + 1}`}
                                            style={{ maxWidth: "100%", height: "auto", objectFit: "contain", marginBottom: "10px" }}
                                        />
                                    ))
                                ) : (
                                    <p>No images available</p>
                                )}
                            </div>
                        </div>
                    )}


                    {/* Shelter Pop-up */}
                    {selectedShelter && (
                        <div className="popup-overlay" onClick={() => setSelectedShelter(null)}>
                            <div className="popup-content" onClick={(e) => e.stopPropagation()}>
                                <h3>{selectedShelter.shelterCategoryName}</h3>
                                <p>Description: {selectedShelter.description}</p>
                                <p>Dimensions: {selectedShelter.width} x {selectedShelter.height} x {selectedShelter.length}</p>
                                <p>Diameter: {selectedShelter.diameter}</p>
                                <p>Water Volume: {selectedShelter.waterVolume} liters</p>
                                <p>Filtration System: {selectedShelter.waterFiltrationSystem}</p>
                                <p>Images:</p>
                                {selectedShelter.shelterImages?.length ? (
                                    selectedShelter.shelterImages.map((img, index) => (
                                        <img
                                            key={index}
                                            src={img.imageUrl}
                                            alt={`${selectedShelter.shelterCategoryName} image ${index + 1}`}
                                            style={{ maxWidth: "100%", height: "auto", objectFit: "contain", marginBottom: "10px" }}
                                        />
                                    ))
                                ) : (
                                    <p>No images available</p>
                                )}
                            </div>
                        </div>
                    )}


                </div>
            )
            }
        </div >
    );
};

export default AutoConsultationComponent;
