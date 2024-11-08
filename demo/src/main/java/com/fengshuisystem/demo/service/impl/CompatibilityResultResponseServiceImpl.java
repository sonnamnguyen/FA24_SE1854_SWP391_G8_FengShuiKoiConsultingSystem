
package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.dto.request.AnimalRequest;
import com.fengshuisystem.demo.dto.request.DestinyRequest;
import com.fengshuisystem.demo.dto.response.AnimalCompatibilityResponse;
import com.fengshuisystem.demo.dto.response.CompatibilityResultResponse;
import com.fengshuisystem.demo.service.AutoConsultationService;
import com.fengshuisystem.demo.service.ColorService;
import com.fengshuisystem.demo.service.CompatibilityResultResponseService;
import com.fengshuisystem.demo.service.DestinyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompatibilityResultResponseServiceImpl implements CompatibilityResultResponseService {

    DestinyService destinyService;
    AutoConsultationService autoConsultationResponseContainer;
    ColorService colorService;

    @Override
    public String compareDestinyWithExplanation(String userDestiny, String attributeDestiny, String attributeName) {
        int score = 0;
        String explanation = "";

        if (userDestiny.equals(attributeDestiny)) {
            score = 4;
            explanation = attributeName + " belongs to the element " + attributeDestiny
                    + ", the same element as your destiny, bringing stability, harmony, and sustainability."
                    + " This compatibility helps keep your work and life balanced, with fewer obstacles.";
        } else if (destinyService.findTuongSinhSau(userDestiny).equals(attributeDestiny)) {
            score = 3;
            explanation = attributeName + " belongs to the element " + attributeDestiny
                    + ". Your destiny, the element " + userDestiny + ", generates the element " + attributeDestiny
                    + ", bringing growth, support, and advancement to your Koi pond."
                    + " This enhances your Koi pond's ability to attract good fortune and positive energy.";
        } else if (destinyService.findTuongKhacSau(userDestiny).equals(attributeDestiny)) {
            score = 2;
            explanation = attributeName + " belongs to the element " + attributeDestiny
                    + ". The element " + userDestiny + " is restricted by " + attributeDestiny
                    + ", causing conflict and imbalance in your pond."
                    + " This may hinder the Koi pond's ability to bring about the desired benefits.";
        } else if (destinyService.findTuongKhacTruoc(userDestiny).equals(attributeDestiny)) {
            score = 1;
            explanation = attributeName + " belongs to the element " + attributeDestiny
                    + ". The element " + attributeDestiny + " restricts your destiny (" + userDestiny
                    + "), causing challenges and obstacles in life and work.";
        } else if (destinyService.findTuongSinhTruoc(userDestiny).equals(attributeDestiny)) {
            score = 5;
            explanation = attributeName + " belongs to the element " + attributeDestiny
                    + ". The element " + attributeDestiny + " generates your destiny (" + userDestiny
                    + "), bringing growth, support, and advancement to you. This is an extremely favorable element for the pond's feng shui, attracting prosperity and positive energy.";
        }

        return score + ";" + explanation;
    }




    @Override
    public CompatibilityResultResponse calculateCompatibility(int yearOfBirth, DestinyRequest destinyInput) {
        String userDestiny = destinyService.getDestinyFromYear(yearOfBirth);

        // Handle direction compatibility
        DestinyDTO directionDestiny;
        String directionResult;
        double directionScore;
        Set<String> directionsAdvice = new HashSet<>();
        boolean hasDirection = destinyInput.getDirectionId() != null;
        if (hasDirection) {
            directionDestiny = destinyService.getDestinyByDirection(destinyInput.getDirectionId());
            directionResult = compareDestinyWithExplanation(userDestiny, directionDestiny.getDestiny(), destinyInput.getDirectionName());
            directionScore = Double.parseDouble(directionResult.split(";")[0]);
            if (directionScore < 3.0) {
                directionsAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getDirections());
                directionsAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getDirections());
            }
        } else {
            directionResult = "0; ";
            directionScore = 0;
            directionsAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getDirections());
            directionsAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getDirections());
        }

        // Handle shape compatibility
        DestinyDTO shapeDestiny;
        String shapeResult;
        double shapeScore;
        Set<String> shapesAdvice = new HashSet<>();
        boolean hasShape = destinyInput.getShapeId() != null;
        if (hasShape) {
            shapeDestiny = destinyService.getDestinyByShape(destinyInput.getShapeId());
            shapeResult = compareDestinyWithExplanation(userDestiny, shapeDestiny.getDestiny(), destinyInput.getShapeName());
            shapeScore = Double.parseDouble(shapeResult.split(";")[0]);
            if (shapeScore < 3.0) {
                shapesAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getShapes());
                shapesAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getShapes());
            }
        } else {
            shapeResult = "0; ";
            shapeScore = 0;
            shapesAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getShapes());
            shapesAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getShapes());
        }

        // Handle number compatibility
        DestinyDTO numberDestiny;
        String numberResult;
        double numberScore;
        Set<Integer> numbersAdvice = new HashSet<>();
        boolean hasNumber = destinyInput.getNumberId() != null;
        if (hasNumber) {
            numberDestiny = destinyService.getDestinyByNumber(destinyInput.getNumberId());
            numberResult = compareDestinyWithExplanation(userDestiny, numberDestiny.getDestiny(), String.valueOf(destinyInput.getNumberName()));
            numberScore = Double.parseDouble(numberResult.split(";")[0]);
            if (numberScore < 3.0) {
                numbersAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getNumbers());
                numbersAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getNumbers());
            }
        } else {
            numberResult = "0; ";
            numberScore = 0;
            numbersAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getNumbers());
            numbersAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getNumbers());
        }

        // Handle animal compatibility
        List<AnimalCompatibilityResponse> animalCompatibilityResponses = new ArrayList<>();
        Set<String> animalAdvice = new HashSet<>();
        List<AnimalRequest> animals = destinyInput.getAnimal();
        double animalListScore = 0.0;
        double averageAnimalListScore = 0.0;
        boolean hasAnimal = animals.isEmpty();
        if (hasAnimal) {
            animalAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getAnimals().stream().map(AnimalCategoryDTO::getAnimalCategoryName).toList());
            animalAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getAnimals().stream().map(AnimalCategoryDTO::getAnimalCategoryName).toList());
        } else {
            for (AnimalRequest animal : animals) {
                List<ColorDTO> animalColors = colorService.getColorsByAnimalId(animal.getAnimalId());
                double animalTotalScore = 0.0;
                double maxScore = 0.0;
                int minCount = 0;
                double averageAnimalScore;
                List<String> colorCompatibilityResponses = new ArrayList<>();
                for (ColorDTO color : animalColors) {
                    String animalResult = compareDestinyWithExplanation(userDestiny, color.getDestiny().getDestiny(), color.getColor());
                    double score = Double.parseDouble(animalResult.split(";")[0]);
                    if(maxScore < score){
                        maxScore = score;
                    }
                    if(score == 2 || score == 1){
                        minCount++;
                    }
                    String explanation = animalResult.split(";")[1];
                    animalTotalScore += score;
                    colorCompatibilityResponses.add(explanation);

                }
                if(minCount > 0) {
                    averageAnimalScore = animalColors.isEmpty() ? 0.0 : Math.round(animalTotalScore / animalColors.size()* 100.0) / 100.0;
                }else{
                    averageAnimalScore = maxScore;
                }
                animalListScore +=  averageAnimalScore;
                animalCompatibilityResponses.add(AnimalCompatibilityResponse.builder()
                        .animalName(animal.getAnimalName())
                        .animalScore(averageAnimalScore)
                        .animalColors(animalColors.stream().map(ColorDTO::getColor).toList())
                        .colorCompatibilityResponses(colorCompatibilityResponses)
                        .build());
            }
            averageAnimalListScore = Math.round(animalListScore / animals.size()* 100.0) / 100.0;;
            if(averageAnimalListScore < 3){
                animalAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getAnimals().stream().map(AnimalCategoryDTO::getAnimalCategoryName).toList());
                animalAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getAnimals().stream().map(AnimalCategoryDTO::getAnimalCategoryName).toList());
            }
        }

        CompatibilityResultResponse.CompatibilityResultResponseBuilder responseBuilder = CompatibilityResultResponse.builder()
                .yourDestiny("Your destiny is the element " + userDestiny);

        if (hasAnimal) {
            responseBuilder.animalAdvice(animalAdvice);
        } else {
            responseBuilder.animalCompatibilityResponse(animalCompatibilityResponses)
                    .animalAverageScore(averageAnimalListScore)
                    .animalAdvice(animalAdvice);
        }

        if (hasDirection) {
            responseBuilder.directionScore(directionScore)
                    .directionName(destinyInput.getDirectionName())
                    .directionExplanation(directionResult.split(";")[1])
                    .directionsAdvice(directionsAdvice);
        } else {
            responseBuilder.directionsAdvice(directionsAdvice);
        }

        if (hasShape) {
            responseBuilder.shapeScore(shapeScore)
                    .shapeName(destinyInput.getShapeName())
                    .shapeExplanation(shapeResult.split(";")[1])
                    .shapesAdvice(shapesAdvice);
        } else {
            responseBuilder.shapesAdvice(shapesAdvice);
        }
        if (hasNumber) {
            responseBuilder.numberScore(numberScore)
                    .number(destinyInput.getNumberName())
                    .numberExplanation(numberResult.split(";")[1])
                    .numbersAdvice(numbersAdvice);
        } else {
            responseBuilder.numbersAdvice(numbersAdvice);
        }
        return responseBuilder.build();
    }
}
