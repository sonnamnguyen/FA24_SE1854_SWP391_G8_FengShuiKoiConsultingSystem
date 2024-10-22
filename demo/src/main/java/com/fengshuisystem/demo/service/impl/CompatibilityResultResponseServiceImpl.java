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
            explanation = attributeName + " thuộc hành " + attributeDestiny
                    + ", cùng hành với mệnh của bạn, mang lại sự ổn định, hòa hợp và bền vững. Sự tương hợp này giúp cho công việc và cuộc sống của bạn luôn trong trạng thái cân bằng, ít gặp trở ngại.";
        } else if (destinyService.findTuongSinhSau(userDestiny).equals(attributeDestiny)) {
            score = 3;
            explanation = attributeName + " thuộc hành " + attributeDestiny
                    + ". Mệnh của bạn, thuộc hành " + userDestiny + ", sinh ra hành " + attributeDestiny
                    + ", mang lại sự phát triển, hỗ trợ và thăng tiến cho hồ cá của bạn"
                    + ". Điều này giúp yếu tố này trong hồ cá Koi thúc đẩy vận may và năng lượng tích cực cho bạn.";
        } else if (destinyService.findTuongKhacSau(userDestiny).equals(attributeDestiny)) {
            score = 2;
            explanation = attributeName + " thuộc hành " + attributeDestiny
                    + ". Hành " + userDestiny + " của bạn bị hành " + attributeDestiny
                    + " khắc chế, gây ra sự xung đột và mất cân bằng cho cho hồ cá của bạn"
                    + ". Điều này có thể khiến hồ cá Koi của bạn không mang lại sự thuận lợi như mong muốn.";
        } else if (destinyService.findTuongKhacTruoc(userDestiny).equals(attributeDestiny)) {
            score = 1;
            explanation = attributeName + " thuộc hành " + attributeDestiny
                    + ". Hành " + attributeDestiny + " khắc chế mệnh của bạn (" + userDestiny
                    + "), gây ra nhiều khó khăn và cản trở trong cuộc sống và công việc.";
        } else if (destinyService.findTuongSinhTruoc(userDestiny).equals(attributeDestiny)) {
            score = 5;
            explanation = attributeName + " thuộc hành " + attributeDestiny
                    + ". Hành " + attributeDestiny + " sinh ra mệnh của bạn (" + userDestiny
                    + "), mang lại sự phát triển, hỗ trợ và thăng tiến cho bạn. Đây là yếu tố vô cùng tốt cho phong thủy của hồ cá, giúp thu hút tài lộc và năng lượng tích cực.";
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
            directionDestiny = destinyService.getDestinyByDirecton(destinyInput.getDirectionId());
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
            animalAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getAnimals());
            animalAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getAnimals());
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
                    averageAnimalScore = animalColors.isEmpty() ? 0.0 : animalTotalScore / animalColors.size();
                }else{
                    averageAnimalScore = maxScore;
                }
                animalListScore += averageAnimalScore;
                animalCompatibilityResponses.add(AnimalCompatibilityResponse.builder()
                        .animalScore(averageAnimalScore)
                        .colorCompatibilityResponses(colorCompatibilityResponses)
                        .build());
            }
            averageAnimalListScore = animalListScore / animals.size();
            if(averageAnimalListScore < 3){
                animalAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation1().getAnimals());
                animalAdvice.addAll(autoConsultationResponseContainer.autoConsultationResponseContainer(yearOfBirth).getConsultation2().getAnimals());            }
        }

        CompatibilityResultResponse.CompatibilityResultResponseBuilder responseBuilder = CompatibilityResultResponse.builder()
                .yourDestiny("Mệnh của bạn là mệnh " + userDestiny);

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
