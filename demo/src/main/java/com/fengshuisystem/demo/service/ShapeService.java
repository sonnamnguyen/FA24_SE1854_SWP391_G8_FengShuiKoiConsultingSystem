package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShapeDTO;

public interface ShapeService {
    public ShapeDTO createShape(ShapeDTO shapeDTO);
    public PageResponse<ShapeDTO> getShapeByName(String name, int page, int size);
    public PageResponse<ShapeDTO> getShapes(int page, int size);
    public void deleteShape(Integer id);
    public ShapeDTO updateShape(Integer id, ShapeDTO shapeDTO);
}
