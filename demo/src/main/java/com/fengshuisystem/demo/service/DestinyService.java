package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.DestinyDTO;

import java.util.List;

public interface DestinyService {
    public String getDestinyFromYear(int yearOfBirth);
    public String findTuongSinhTruoc(String destiny);
    public String findTuongSinhSau(String destiny);
    public String findTuongKhacTruoc(String destiny);
    public String findTuongKhacSau(String destiny);
    public DestinyDTO getDestinyId(String destiny);
    public List<DestinyDTO> getAllDestinyByAnimal(int animalId);
}
