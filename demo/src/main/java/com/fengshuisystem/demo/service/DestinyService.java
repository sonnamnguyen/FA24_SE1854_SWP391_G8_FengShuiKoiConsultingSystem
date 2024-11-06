package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.DestinyDTO;

import java.util.List;

public interface DestinyService {
     List<DestinyDTO> getAllDestiny();
    String getDestinyFromYear(int yearOfBirth);
    String findTuongSinhTruoc(String destiny);
    String findTuongSinhSau(String destiny);
    String findTuongKhacTruoc(String destiny);
    String findTuongKhacSau(String destiny);
    DestinyDTO getDestinyId(String destiny);
    List<DestinyDTO> getAllDestinyByAnimal(int animalId);
    DestinyDTO getDestinyByDirection(int directionId);
    DestinyDTO getDestinyByShape(int shapeId);
    DestinyDTO getDestinyByNumber(int numberId);
    List<String> getAnimalNames(Integer destinyId, String tuongKhacTruoc, String tuongKhacSau);
    List<String> getShelterNames(Integer destinyId);
    public DestinyDTO getAllDestinyTuongSinhAndTuongKhac(String destinyName);
}
