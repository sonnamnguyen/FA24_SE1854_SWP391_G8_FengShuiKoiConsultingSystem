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
    DestinyDTO getDestinyByDirection(int directionId);
    DestinyDTO getDestinyByShape(int shapeId);
    DestinyDTO getDestinyByNumber(int numberId);
    List<String> getAnimalNames(Integer destinyId, String tuongKhacTruoc, String tuongKhacSau);
    List<String> getShelterNames(Integer destinyId);
    //Task Huy
    public List<DestinyDTO> getAllDestiny();
    public DestinyDTO getAllDestinyTuongSinhAndTuongKhac(String destinyName);
}