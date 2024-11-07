import React, { useState } from "react";
import { getAutoConsultationByYear } from "./AutoConsultationAPI";
import AutoConsultationContainer from "./AutoConsultationContainer";
import "./AutoConsultation.css"

const AutoConsultationComponent: React.FC = () => {
    const currentYear = new Date().getFullYear();
    const [year, setYear] = useState<number | string>('');
    const [consultationData, setConsultationData] = useState<AutoConsultationContainer | null>(null);
    const [error, setError] = useState<string>("");
    const years = [];
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

            {error && <div className="error-message">{error}</div>}
            {consultationData && (
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
                        <p>Các số may mắn thuộc hành {consultationData.destiny}, như {consultationData.consultation2.numbers.join(", ")}, sẽ góp phần ổn định và tăng cường năng lượng bản mệnh của bạn. Điều này đem lại sự bền vững và thuận lợi, giúp bạn đạt được hòa hợp và phát triển.</p>
                        <p>Trong khi đó, các số đại diện cho hành {consultationData.destinyTuongSinh}, như {consultationData.consultation1.numbers.join(", ")}, có sức mạnh tương sinh, bổ trợ mệnh {consultationData.destiny}. Chọn số lượng cá theo những con số này sẽ mang lại sự phát đạt, gia tăng vận may và mở rộng cơ hội thành công trong sự nghiệp.</p>
                        <p>Từ 11 con cá koi trở đi, bỏ chữ số hàng chục, lấy theo chữ số hàng đơn vị. Ví dụ: 11, 15, 38, 40, 49 con…. Tính thành 1, 5, 8, 4 , 9 con. Sau đó tiếp tục dựa vào bảng ở trên để tính.</p>
                    </div>
                    <div>
                        <h3 className="titleDestiny2">2. Màu sắc và loài cá</h3>
                        <p>Những loài cá và màu sắc tương sinh và tương hợp với mệnh {consultationData.destiny}:</p>
                        <ul>
                            <li>Cá {consultationData.consultation2.colors.join(", ")}: Những màu này thuộc hành {consultationData.destiny}, giúp gia tăng sự ổn định và bình yên.</li>
                            <p>Các loài cá phù hợp: {consultationData.consultation2.animals.join(", ")}</p>
                            <li>Cá màu {consultationData.consultation1.colors.join(", ")}: Những màu này thuộc hành {consultationData.destinyTuongSinh}, vì {consultationData.destinyTuongSinh} sinh {consultationData.destiny}, tạo sự hỗ trợ và phát triển cho người mệnh {consultationData.destiny}.</li>
                            <p>Các loài cá phù hợp: {consultationData.consultation1.animals.join(", ")}</p>
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
                            <p>Các mẫu hồ chúng tôi sưu tầm: {consultationData.consultation2.shelters.join(", ")}</p>
                            <li>Hình dáng thuộc hành {consultationData.destinyTuongSinh} như hình {consultationData.consultation1.shapes.join(", ")} cũng rất phù hợp, vì {consultationData.destinyTuongSinh} sinh {consultationData.destiny}, tạo ra sự tương sinh mạnh mẽ, mang lại sự thịnh vượng và hỗ trợ cho vận khí của gia chủ.</li>
                            <p>Các mẫu hồ chúng tôi sưu tầm: {consultationData.consultation1.shelters.join(", ")}</p>
                        </ul>
                    </div>
                </div>
            )
            }
        </div >
    );
};

export default AutoConsultationComponent;
