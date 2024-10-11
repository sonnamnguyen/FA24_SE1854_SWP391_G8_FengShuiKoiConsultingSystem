package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ColorDTO;
import com.fengshuisystem.demo.dto.PageResponse;

import java.util.List;

public interface ColorService {
    public ColorDTO createColor(ColorDTO colorDTO);
    public PageResponse<ColorDTO> getColorByName(String name, int page, int size);
    public PageResponse<ColorDTO> getColors(int page, int size);
    public void deleteColor(Integer id);
    public ColorDTO updateColor(Integer id, ColorDTO colorDTO);
    List<ColorDTO> getColorsByDestiny(Integer destiny);
    List<ColorDTO> getColorsByAnimalId(Integer animalId);
}
