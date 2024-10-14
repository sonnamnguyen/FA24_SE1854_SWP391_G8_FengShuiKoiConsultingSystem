package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShapeDTO;

import java.util.List;

public interface ShapeService {
     ShapeDTO createShape(ShapeDTO shapeDTO);
     PageResponse<ShapeDTO> getShapeByName(String name, int page, int size);
     PageResponse<ShapeDTO> getShapesByDestiny(int page, int size);
     void deleteShape(Integer id);
     ShapeDTO updateShape(Integer id, ShapeDTO shapeDTO);
     List<ShapeDTO> getShapesByDestiny(Integer destiny);
}
