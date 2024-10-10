package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.DestinyDTO;

import java.util.List;

public interface DestinyService {
    String getDestinyFromYear(int yearOfBirth);
    String findTuongSinhTruoc(String destiny);
    String findTuongSinhSau(String destiny);
    String findTuongKhacTruoc(String destiny);
    String findTuongKhacSau(String destiny);
    DestinyDTO getDestinyId(String destiny);
    List<DestinyDTO> getAllDestinyByAnimal(int animalId);
    List<String> getShapeNames(Integer destinyId);
    List<String> getColorNames(Integer destinyId);
    List<String> getDirectionNames(Integer destinyId);
    List<Integer> getNumberNames(Integer destinyId);
    List<String> getAnimalNames(Integer destinyId, String tuongKhacTruoc, String tuongKhacSau);
    List<String> getShelterNames(Integer destinyId);
}
