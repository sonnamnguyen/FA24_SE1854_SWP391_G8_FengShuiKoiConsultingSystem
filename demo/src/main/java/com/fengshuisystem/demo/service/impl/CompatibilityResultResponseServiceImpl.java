package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.AnimalInputDTO;
import com.fengshuisystem.demo.dto.DestinyDTO;
import com.fengshuisystem.demo.dto.DestinyInputDTO;
import com.fengshuisystem.demo.dto.response.AnimalCompatibilityResponse;
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


    @PreAuthorize("hasRole('USER')")
    @Override
    public String compareDestinyWithExplanation(String userDestiny, String attributeDestiny, String attributeName) {
        int score = 0;
        String explanation = "";

        if (userDestiny.equals(attributeDestiny)) {
            score = 4;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ", same as your element, bringing stability, harmony, and sustainability.";
        }

        else if (destinyServiceImpl.findTuongSinhSau(userDestiny).equals(attributeDestiny)) {
            score = 3;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ". Your element " + userDestiny + " generates the attribute element, "
                    + "bringing development, support, and promotion for " + attributeName + ".";
        }

        else if (destinyServiceImpl.findTuongKhacSau(userDestiny).equals(attributeDestiny)) {
            score = 2;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ". Your element " + userDestiny + " is countered by the attribute element, "
                    + "causing conflict and instability for " + attributeName + ".";
        }

        else if (destinyServiceImpl.findTuongKhacTruoc(userDestiny).equals(attributeDestiny)) {
            score = 1;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ". The attribute element counters your element " + userDestiny + ", "
                    + "causing harm and obstacles for you.";
        }

        else if (destinyServiceImpl.findTuongSinhTruoc(userDestiny).equals(attributeDestiny)) {
            score = 5;
            explanation = attributeName + " is of element " + attributeDestiny
                    + ". The attribute element generates your element " + userDestiny + ", "
                    + "bringing development, support, and promotion for you.";
        }

        return score + ";" + explanation;
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public CompatibilityResultResponse calculateCompatibility(int yearOfBirth, DestinyInputDTO destinyInput) {
        String userDestiny = destinyServiceImpl.getDestinyFromYear(yearOfBirth);

        DestinyDTO directionDestiny = destinyServiceImpl.getDestinyByDirecton(destinyInput.getDirectionId());
        String directionResult = compareDestinyWithExplanation(userDestiny, directionDestiny.getDestiny(), destinyInput.getDirectionName());

        DestinyDTO shapeDestiny = destinyServiceImpl.getDestinyByShape(destinyInput.getShapeId());
        String shapeResult = compareDestinyWithExplanation(userDestiny, shapeDestiny.getDestiny(), destinyInput.getShapeName());

        DestinyDTO numberDestiny = destinyServiceImpl.getDestinyByNumber(destinyInput.getColorId());
        String numberResult = compareDestinyWithExplanation(userDestiny, numberDestiny.getDestiny(), String.valueOf(destinyInput.getNumberName()));

        double totalAnimalScore = 0.0;

        List<AnimalCompatibilityResponse> animalCompatibilityResponses = new ArrayList<>();
        List<AnimalInputDTO> animals = destinyInput.getAnimal();

        // Calculate scores and explanations for each animal
        for (AnimalInputDTO animal : animals) {
            List<DestinyDTO> animalDestinies = destinyServiceImpl.getAllDestinyByAnimal(animal.getAnimalId());

             totalAnimalScore = 0.0;
            StringBuilder animalExplanations = new StringBuilder();

            for (DestinyDTO destiny : animalDestinies) {
                String animalResult = compareDestinyWithExplanation(userDestiny, destiny.getDestiny(), animal.getAnimalName());
                double score = Double.parseDouble(animalResult.split(";")[0]);
                String explanation = animalResult.split(";")[1];

                // Accumulate score and explanation
                totalAnimalScore += score;
                animalExplanations.append(explanation).append("\n");
            }

            // Calculate the average score for the animal
            double averageAnimalScore = !animalDestinies.isEmpty() ? totalAnimalScore / animalDestinies.size() : 0.0;

            // Add to the response list
            animalCompatibilityResponses.add(AnimalCompatibilityResponse.builder()
                    .animalScore(averageAnimalScore)
                    .animalExplanation(animalExplanations.toString())
                    .build());
        }

        // Build and return the final response
        return CompatibilityResultResponse.builder()
                .yourDestiny("Your element is " + userDestiny)
                .directionScore(Double.parseDouble(directionResult.split(";")[0]))
                .directionExplanation(directionResult.split(";")[1])
                .shapeScore(Double.parseDouble(shapeResult.split(";")[0]))
                .shapeExplanation(shapeResult.split(";")[1])
                .numberScore(Double.parseDouble(numberResult.split(";")[0]))
                .numberExplanation(numberResult.split(";")[1])
                .animalCompatibilityResponse(animalCompatibilityResponses)
                .build();
    }
}
