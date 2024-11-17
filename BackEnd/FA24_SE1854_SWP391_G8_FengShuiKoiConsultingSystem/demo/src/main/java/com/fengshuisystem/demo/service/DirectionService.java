package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.DirectionDTO;


import java.util.List;

public interface DirectionService {
    List<DirectionDTO> getDirections(Integer destiny);
    List<DirectionDTO> getAllDirections();

}
