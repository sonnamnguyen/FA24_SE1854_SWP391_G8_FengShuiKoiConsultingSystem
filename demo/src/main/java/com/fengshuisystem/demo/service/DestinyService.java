package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.DestinyDTO;

import java.util.List;

public interface DestinyService {
    public List<DestinyDTO> getAllDestiny();
    public DestinyDTO getAllDestinyTuongSinhAndTuongKhac(String destinyName);
    }
