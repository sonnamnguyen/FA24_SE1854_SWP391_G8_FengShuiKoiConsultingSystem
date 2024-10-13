package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.dto.response.AnimalCompatibilityResponse;
import com.fengshuisystem.demo.dto.response.ColorCompatibilityResponse;
import com.fengshuisystem.demo.dto.response.CompatibilityResultResponse;
import com.fengshuisystem.demo.service.CompatibilityResultResponseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompatibilityResultResponseServiceImpl implements CompatibilityResultResponseService {

    DestinyServiceImpl destinyServiceImpl;
    AutoConsultationServiceImpl autoConsultationResponseContainer;
    ColorServiceImpl colorService;

    @PreAuthorize("hasRole('USER')")
    @Override
    public String compareDestinyWithExplanation(String userDestiny, String attributeDestiny, String attributeName) {
        int score = 0;
        String explanation = "";

        if (userDestiny.equals(attributeDestiny)) {
            score = 4;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ", same as your element, bringing stability, harmony, and sustainability.";
        } else if (destinyServiceImpl.findTuongSinhSau(userDestiny).equals(attributeDestiny)) {
            score = 3;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ". Your element " + userDestiny + " generates the attribute element, "
                    + "bringing development, support, and promotion for " + attributeName + ".";
        } else if (destinyServiceImpl.findTuongKhacSau(userDestiny).equals(attributeDestiny)) {
            score = 2;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ". Your element " + userDestiny + " is countered by the attribute element, "
                    + "causing conflict and instability for " + attributeName + ".";
        } else if (destinyServiceImpl.findTuongKhacTruoc(userDestiny).equals(attributeDestiny)) {
            score = 1;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ". The attribute element counters your element " + userDestiny + ", "
                    + "causing harm and obstacles for you.";
        } else if (destinyServiceImpl.findTuongSinhTruoc(userDestiny).equals(attributeDestiny)) {
            score = 5;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ". The attribute element generates your element " + userDestiny + ", "
                    + "bringing development, support, and promotion for you.";
        }

        return score + ";" + explanation;
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public CompatibilityResultResponse calculateCompatibility(int yearOfBirth, DestinyInputDTO destinyInput) {
        String userDestiny = destinyServiceImpl.getDestinyFromYear(yearOfBirth);

        // Handle direction compatibility
        DestinyDTO directionDestiny;
        String directionResult;
        double directionScore;
        List<String> directionsAdvice = null;
        boolean hasDirection = destinyInput.getDirectionId() != null;
        if (hasDirection) {
            directionDestiny = destinyServiceImpl.getDestinyByDirecton(destinyInput.getDirectionId());
            directionResult = compareDestinyWithExplanation(userDestiny, directionDestiny.getDestiny(), destinyInput.getDirectionName());
            directionScore = Double.parseDouble(directionResult.split(";")[0]);
            if (directionScore < 3.0) {
                directionsAdvice = autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getDirections();
            }
        } else {
            directionResult = "0; ";
            directionScore = 0;
            directionsAdvice = autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getDirections();
        }

        // Handle shape compatibility
        DestinyDTO shapeDestiny;
        String shapeResult;
        double shapeScore;
        List<String> shapesAdvice = null;
        boolean hasShape = destinyInput.getShapeId() != null;
        if (hasShape) {
            shapeDestiny = destinyServiceImpl.getDestinyByShape(destinyInput.getShapeId());
            shapeResult = compareDestinyWithExplanation(userDestiny, shapeDestiny.getDestiny(), destinyInput.getShapeName());
            shapeScore = Double.parseDouble(shapeResult.split(";")[0]);
            if (shapeScore < 3.0) {
                shapesAdvice = autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getShapes();
            }
        } else {
            shapeResult = "0; ";
            shapeScore = 0;
            shapesAdvice = autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getShapes();
        }

        // Handle number compatibility
        DestinyDTO numberDestiny;
        String numberResult;
        double numberScore;
        List<Integer> numbersAdvice = null;
        boolean hasNumber = destinyInput.getNumberId() != null;
        if (hasNumber) {
            numberDestiny = destinyServiceImpl.getDestinyByNumber(destinyInput.getNumberId());
            numberResult = compareDestinyWithExplanation(userDestiny, numberDestiny.getDestiny(), String.valueOf(destinyInput.getNumberName()));
            numberScore = Double.parseDouble(numberResult.split(";")[0]);
            if (numberScore < 3.0) {
                numbersAdvice = autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getNumbers();
            }
        } else {
            numberResult = "0; ";
            numberScore = 0;
            numbersAdvice = autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getNumbers();
        }

        // Handle animal compatibility
        List<AnimalCompatibilityResponse> animalCompatibilityResponses = new ArrayList<>();
        List<String> animalAdvice = new ArrayList<>(); // List to collect animal advice
        List<AnimalInputDTO> animals = destinyInput.getAnimal();
        double animalListScore = 0.0;
        double averageAnimalListScore = 0.0;
        boolean hasAnimal = animals.isEmpty();
        if (hasAnimal) {
            animalAdvice = autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getAnimals();
        } else {
            for (AnimalInputDTO animal : animals) {
                List<ColorDTO> animalColors = colorService.getColorsByAnimalId(animal.getAnimalId());
                double animalTotalScore = 0.0;

                List<ColorCompatibilityResponse> colorCompatibilityResponses = new ArrayList<>();
                for (ColorDTO color : animalColors) {
                    String animalResult = compareDestinyWithExplanation(userDestiny, color.getDestiny().getDestiny(), color.getColor());
                    double score = Double.parseDouble(animalResult.split(";")[0]);
                    String explanation = animalResult.split(";")[1];
                    animalTotalScore += score;
                    colorCompatibilityResponses.add(ColorCompatibilityResponse.builder()
                            .colorExplanation(explanation)
                            .build());

                }
                double averageAnimalScore = animalColors.isEmpty() ? 0.0 : animalTotalScore / animalColors.size();

                animalListScore += averageAnimalScore;
                animalCompatibilityResponses.add(AnimalCompatibilityResponse.builder()
                        .animalScore(averageAnimalScore)
                        .colorCompatibilityResponses(colorCompatibilityResponses)
                        .build());
            }
             averageAnimalListScore = animalListScore / animals.size();
            if(averageAnimalListScore < 3){
                animalAdvice = autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getAnimals();
            }
        }

        CompatibilityResultResponse.CompatibilityResultResponseBuilder responseBuilder = CompatibilityResultResponse.builder()
                .yourDestiny("Your element is " + userDestiny);

        if (hasAnimal) {
            responseBuilder.animalAdvice(animalAdvice);
        } else {
            responseBuilder.animalCompatibilityResponse(animalCompatibilityResponses)
                    .animalScore(averageAnimalListScore)
                    .animalAdvice(animalAdvice);
        }

        if (hasDirection) {
            responseBuilder.directionScore(directionScore)
                    .directionExplanation(directionResult.split(";")[1])
                    .directionsAdvice(directionsAdvice);
        } else {
            responseBuilder.directionsAdvice(directionsAdvice);
        }

        if (hasShape) {
            responseBuilder.shapeScore(shapeScore)
                    .shapeExplanation(shapeResult.split(";")[1])
                    .shapesAdvice(shapesAdvice);
        } else {
            responseBuilder.shapesAdvice(shapesAdvice);
        }

        if (hasNumber) {
            responseBuilder.numberScore(numberScore)
                    .numberExplanation(numberResult.split(";")[1])
                    .numbersAdvice(numbersAdvice);
        } else {
            responseBuilder.numbersAdvice(numbersAdvice);
        }
        return responseBuilder.build();
    }
}
