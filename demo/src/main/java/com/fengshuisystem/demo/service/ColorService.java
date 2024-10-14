package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ColorDTO;
import com.fengshuisystem.demo.dto.PageResponse;

public interface ColorService {
     ColorDTO createColor(ColorDTO colorDTO);
     PageResponse<ColorDTO> getColorByName(String name, int page, int size);
     PageResponse<ColorDTO> getColors(int page, int size);
     void deleteColor(Integer id);
    ColorDTO updateColor(Integer id, ColorDTO colorDTO);
}
