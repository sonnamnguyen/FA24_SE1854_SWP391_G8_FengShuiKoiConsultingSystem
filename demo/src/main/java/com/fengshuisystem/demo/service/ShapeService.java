package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShapeDTO;

import java.util.List;

public interface ShapeService {
    public ShapeDTO createShape(ShapeDTO shapeDTO);
    public PageResponse<ShapeDTO> getShapeByName(String name, int page, int size);
    public PageResponse<ShapeDTO> getShapesByDestiny(int page, int size);
    public void deleteShape(Integer id);
    public ShapeDTO updateShape(Integer id, ShapeDTO shapeDTO);
    public List<ShapeDTO> getShapesByDestiny(Integer destiny);
}
