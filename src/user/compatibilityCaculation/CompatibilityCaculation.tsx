import React, { useState, useEffect } from "react";
import { getCompatibilityResult } from "./CompatibilityCaculationAPI";
import CompatibilityResultResponse from "./CompatibilityCaculationModel";
import api from "../../axious/axious";
import "./CompatibilityCaculation.css"

interface Direction {
    id: number;
    direction: string;
}

interface Number {
    id: number;
    number: string;
}

interface Shape {
    id: number;
    shape: string;
}

interface AnimalCategory {
    id: number;
    animalCategoryName: string;
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
interface AnimalRequest {
    animalId: number;
    animalName: string;
}

const getAllDirections = async (): Promise<Direction[] | null> => {
    try {
        const response = await api.get('/api/direction');
        return response.data.code === 1000
            ? response.data.result.map((direction: any) => ({ id: direction.id, direction: direction.direction }))
            : null;
    } catch (error) {
        console.error("Error fetching directions: ", error);
        return null;
    }
};

const getAllNumbers = async (): Promise<Number[] | null> => {
    try {
        const response = await api.get('/api/number');
        return response.data.code === 1000
            ? response.data.result.map((number: any) => ({ id: number.id, number: number.number }))
            : null;
    } catch (error) {
        console.error("Error fetching numbers: ", error);
        return null;
    }
};

const getAllShapes = async (): Promise<Shape[] | null> => {
    try {
        const response = await api.get('/shapes/getAll-Shapes');
        return response.data.code === 1000
            ? response.data.result.map((shape: any) => ({ id: shape.id, shape: shape.shape }))
            : null;
    } catch (error) {
        console.error("Error fetching shapes: ", error);
        return null;
    }
};

const getAllAnimalCategory = async (): Promise<AnimalCategory[] | null> => {
    try {
        const response = await api.get('/animals/animalCategory');
        return response.data.code === 1000
            ? response.data.result.map((animalCategory: any) => ({ id: animalCategory.id, animalCategoryName: animalCategory.animalCategoryName }))
            : null;
    } catch (error) {
        console.error("Error fetching animal categories: ", error);
        return null;
    }
};



const CompatibilityForm = () => {
    const [caculationData, setCaculationData] = useState<CompatibilityResultResponse | null>(null);
    const currentYear = new Date().getFullYear();
    const [year, setYear] = useState<number | string>('');
    const [directions, setDirections] = useState<Direction[] | null>(null);
    const [numbers, setNumbers] = useState<Number[] | null>(null);
    const [animalCategories, setAnimalCategories] = useState<AnimalCategory[] | null>(null);
    const [shapes, setShapes] = useState<Shape[] | null>(null);
    const [error, setError] = useState<string>("");
    const [selectedDirection, setSelectedDirection] = useState<number | null>(null);
    const [selectedNumber, setSelectedNumber] = useState<number | null>(null);
    const [selectedShape, setSelectedShape] = useState<number | null>(null);
    const [selectedAnimals, setSelectedAnimals] = useState<AnimalRequest[]>([]);

    const years = [];
    for (let i = 1930; i <= currentYear; i++) {
        years.push(i);
    }

    useEffect(() => {
        const fetchData = async () => {
            setDirections(await getAllDirections());
            setNumbers(await getAllNumbers());
            setAnimalCategories(await getAllAnimalCategory());
            setShapes(await getAllShapes());
        };
        fetchData();
    }, []);

    const handleAnimalChange = (index: number, value: number) => {
        const updatedAnimals = [...selectedAnimals];
        updatedAnimals[index] = {
            animalId: value,
            animalName: animalCategories?.find(a => a.id === value)?.animalCategoryName!
        };
        setSelectedAnimals(updatedAnimals);
    };

    const addAnimal = () => {
        if (selectedAnimals.length < 4) {
            setSelectedAnimals([...selectedAnimals, { animalId: 0, animalName: "" }]);
        }
    };
    const hasSelectedAllAnimals = selectedAnimals.every(animal => animal.animalId > 0);

    const removeAnimal = (index: number) => {
        const updatedAnimals = selectedAnimals.filter((_, i) => i !== index);
        setSelectedAnimals(updatedAnimals);
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError("");
        const parsedYear = Number(year);
        if (year === '' || isNaN(parsedYear)) {
            setError("Invalid year. Please select a valid year.");
            return;
        }
        const direction = directions?.find((d) => d.id === selectedDirection);
        const number = numbers?.find((n) => n.id === selectedNumber);
        const shape = shapes?.find((s) => s.id === selectedShape);

        if (!direction && !number && !shape && selectedAnimals.length === 0) {
            setError("Please select at least one option.");
            return;
        }



        const compatibilityRequest: CompatibilityRequest = {
            directionId: direction?.id ?? null,
            directionName: direction?.direction ?? null,
            shapeId: shape?.id ?? null,
            shapeName: shape?.shape ?? null,
            numberId: number?.id ?? null,
            numberName: number?.number ?? null,
            animal: selectedAnimals.filter(animal => animal.animalId > 0)
        };

        const result = await getCompatibilityResult(parsedYear, compatibilityRequest);
        if (result) {
            setCaculationData(result.result);
        } else {
            setError("Failed to fetch data. Please try again.");
        }
    };

    return (
        <div className="background-fenghsui">
            <form onSubmit={handleSubmit} className="compatibility-form">
                <div className="blur-background"></div>

                <h1 className="form-title">XEM ĐỘ PHÙ HỢP CỦA HỒ CÁ THEO NGŨ HÀNH </h1>
                <table className="compatibility-table">
                    <tbody>
                        <tr className="form-group year-section">
                            <td className="title-table">
                                <label htmlFor="year" className="form-label">Choose Year:</label>
                            </td>
                            <td>
                                <select
                                    id="yearForCaculate"
                                    className="form-select"
                                    value={year}
                                    onChange={(e) => setYear(Number(e.target.value))}
                                >
                                    <option>-- Select a Year --</option>
                                    {years.map((y) => (
                                        <option key={y} value={y}>
                                            {y}
                                        </option>
                                    ))}
                                </select>
                            </td>
                        </tr>

                        <tr className="form-group direction-section">
                            <td className="title-table">
                                <label className="form-label">Direction:</label>
                            </td>
                            <td>
                                <select
                                    className="form-select"
                                    onChange={(e) => setSelectedDirection(Number(e.target.value))}
                                >
                                    <option>-- Select Direction --</option>
                                    {directions ? (
                                        directions.map((direction) => (
                                            <option key={direction.id} value={direction.id}>
                                                {direction.direction}
                                            </option>
                                        ))
                                    ) : (
                                        <option>Loading...</option>
                                    )}
                                </select>
                            </td>
                        </tr>

                        <tr className="form-group number-section">
                            <td className="title-table">
                                <label className="form-label">Number:</label>
                            </td>
                            <td>
                                <select
                                    className="form-select"
                                    onChange={(e) => setSelectedNumber(Number(e.target.value))}
                                >
                                    <option>-- Select Number --</option>
                                    {numbers ? (
                                        numbers.map((number) => (
                                            <option key={number.id} value={number.id}>
                                                {number.number}
                                            </option>
                                        ))
                                    ) : (
                                        <option>Loading...</option>
                                    )}
                                </select>
                            </td>
                        </tr>

                        <tr className="form-group shape-section">
                            <td className="title-table">
                                <label className="form-label">Shape:</label>
                            </td>
                            <td>
                                <select
                                    className="form-select"
                                    onChange={(e) => setSelectedShape(Number(e.target.value))}
                                >
                                    <option>-- Select Shape --</option>
                                    {shapes ? (
                                        shapes.map((shape) => (
                                            <option key={shape.id} value={shape.id}>
                                                {shape.shape}
                                            </option>
                                        ))
                                    ) : (
                                        <option>Loading...</option>
                                    )}
                                </select>
                            </td>
                        </tr>

                        <tr className="form-group fish-section">
                            <td className="title-table">
                                <label className="form-label">Fishs:</label>
                            </td>
                            <td>
                                {selectedAnimals.map((animal, index) => (
                                    <div key={index} className="fish-item">
                                        <select
                                            className="form-select "
                                            value={animal.animalId}
                                            onChange={(e) => handleAnimalChange(index, Number(e.target.value))}
                                        >
                                            <option>-- Select Animal Category --</option>
                                            {animalCategories ? (
                                                animalCategories
                                                    .filter((animalCategory) =>
                                                        !selectedAnimals
                                                            .slice(0, index)
                                                            .some((selectedAnimal) => selectedAnimal.animalId === animalCategory.id)
                                                    )
                                                    .map((animalCategory) => (
                                                        <option key={animalCategory.id} value={animalCategory.id}>
                                                            {animalCategory.animalCategoryName}
                                                        </option>
                                                    ))
                                            ) : (
                                                <option>Loading...</option>
                                            )}
                                        </select>
                                        <button type="button" className="remove-btn" onClick={() => removeAnimal(index)}>Remove</button>
                                    </div>
                                ))}
                                {hasSelectedAllAnimals && selectedAnimals.length < 4 && (
                                    <button type="button" className="add-btn" onClick={addAnimal}>Add Animal</button>
                                )}
                            </td>
                        </tr>
                    </tbody>
                </table>

                <button type="submit" className="compability-btn">XEM</button>
            </form>

            {error && <p style={{ color: "red" }}>{error}</p>}

            {caculationData && (
                <div className="result-container">
                    <h2 className="result-title">Kết Quả Độ Phù Hợp:</h2>
                    {caculationData.yourDestiny && (
                        <p className="destiny">{caculationData.yourDestiny}</p>
                    )}
                    {caculationData.directionExplanation && (
                        <div className="direction">
                            <h3 className="direction-title">Hướng Hồ</h3>
                            {caculationData.directionName && <p className="direction-name">Bạn chọn hướng: {caculationData.directionName}</p>}
                            <p className="direction-explanation"><div className="explain">Biện Giải:</div> {caculationData.directionExplanation}</p>
                            {caculationData.directionScore !== null && (
                                <p className="direction-score">Điểm: {caculationData.directionScore}/5</p>
                            )}
                            {caculationData.directionsAdvice && caculationData.directionsAdvice.size > 0 && (
                                <p className="directions-advice">Những Hướng Tốt Hơn: {Array.from(caculationData.directionsAdvice).join(', ')}</p>
                            )}
                        </div>
                    )}

                    {caculationData.numberExplanation && (
                        <div className="number">
                            <h3 className="number-title">Số Lượng Cá</h3>
                            {caculationData.number && <p className="number">Bạn chọn số: {caculationData.number}</p>}
                            <p className="number-explanation"><div className="explain">Biện Giải:</div> {caculationData.numberExplanation}</p>
                            {caculationData.numberScore !== null && (
                                <p className="number-score">Điểm: {caculationData.numberScore}/5</p>
                            )}
                            {caculationData.numbersAdvice && caculationData.numbersAdvice.size > 0 && (
                                <p className="numbers-advice">Những Số Lượng Tốt Hơn: {Array.from(caculationData.numbersAdvice).join(', ')}</p>
                            )}
                        </div>
                    )}

                    {caculationData.shapeExplanation && (
                        <div className="shape">
                            <h3 className="shape-title">Hình Dáng Hồ</h3>
                            {caculationData.shapeName && <p className="shape-name">Bạn chọn hồ hình: {caculationData.shapeName}</p>}
                            <p className="shape-explanation"><div className="explain">Biện Giải:</div> {caculationData.shapeExplanation}</p>
                            {caculationData.shapeScore !== null && (
                                <p className="shape-score">Điểm: {caculationData.shapeScore}/5</p>
                            )}
                            {caculationData.shapesAdvice && caculationData.shapesAdvice.size > 0 && (
                                <p className="shapes-advice">Những Hình Dáng Tốt Hơn: {Array.from(caculationData.shapesAdvice).join(', ')}</p>
                            )}
                        </div>
                    )}
                    {caculationData.animalCompatibilityResponse && caculationData.animalCompatibilityResponse.length > 0 && (
                        <div className="animal">
                            <h3 className="animal-title">Cá</h3>
                            {caculationData.animalCompatibilityResponse.map((animal, index) => (
                                <div key={index} className="animal-info">
                                    {animal.animalName && <p className="animal-name">Bạn chọn cá: {animal.animalName}</p>}
                                    {animal.animalScore !== null && (
                                        <p className="animal-colors">{animal.animalName} có màu: {animal.animalColors.join(', ')}</p>
                                    )}
                                    {animal.colorCompatibilityResponses && (
                                        <p className="animal-explanation">
                                            <div className="explain">Biện Giải:</div>
                                            <ul className="color-explanations">
                                                {animal.colorCompatibilityResponses.map((response, index) => (
                                                    <li key={index}>Màu {response}</li>
                                                ))}
                                            </ul>
                                        </p>
                                    )}
                                    {animal.animalScore !== null && (
                                        <p className="animal-score">Điểm: {animal.animalScore}/5</p>
                                    )}
                                </div>
                            ))}
                            {caculationData.animalAverageScore !== null && (
                                <p className="animal-average-score">Điểm trung bình: {caculationData.animalAverageScore}/5</p>
                            )}
                            {caculationData.animalAdvice && caculationData.animalAdvice.size > 0 && (
                                <p className="animal-advice">Những Loại Cá Tốt Hơn: {Array.from(caculationData.animalAdvice).join(', ')}</p>
                            )}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default CompatibilityForm;
