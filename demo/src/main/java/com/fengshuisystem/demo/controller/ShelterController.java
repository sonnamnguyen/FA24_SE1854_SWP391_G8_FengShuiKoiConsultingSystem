package com.fengshuisystem.demo.controller;

import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShelterCategoryDTO;
import com.fengshuisystem.demo.service.ShelterService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelters")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ShelterController {
  ShelterService shelterService;

  @PostMapping
  public ApiResponse<ShelterCategoryDTO> createShelter(@Valid @RequestBody  ShelterCategoryDTO shelterCreationRequest) {
    return ApiResponse.<ShelterCategoryDTO>builder()
            .result(shelterService.createShelter(shelterCreationRequest))
            .build();
  }

  @GetMapping
  public ApiResponse<PageResponse<ShelterCategoryDTO>> getAllShelters(
          @RequestParam(value = "page", required = false, defaultValue = "1") int page,
          @RequestParam(value = "size", required = false, defaultValue = "10") int size
  ) {

    return ApiResponse.<PageResponse<ShelterCategoryDTO>>builder()
            .result(shelterService.getAllShelters(page, size))
            .build();
  }

  @GetMapping("/search-name")
  public ApiResponse<PageResponse<ShelterCategoryDTO>> getShelterBySearch(
          @RequestParam String name,
          @RequestParam(value = "page", required = false, defaultValue = "1") int page,
          @RequestParam(value = "size", required = false, defaultValue = "10") int size

  ) {
    return ApiResponse.<PageResponse<ShelterCategoryDTO>>builder()
            .result(shelterService.getSheltersBySearch(name, page, size))
            .build();
  }
  @GetMapping("/search-destiny")
  public ApiResponse<PageResponse<ShelterCategoryDTO>> getShelterByDestiny(
          @RequestParam List<String> destiny,
          @RequestParam(value = "page", required = false, defaultValue = "1") int page,
          @RequestParam(value = "size", required = false, defaultValue = "10") int size

  ) {
    return ApiResponse.<PageResponse<ShelterCategoryDTO>>builder()
            .result(shelterService.getSheltersByDestiny(destiny, page, size))
            .build();
  }
  @PutMapping("/{id}")
  public ApiResponse<ShelterCategoryDTO> updateShelter(@PathVariable Integer id, @Valid @RequestBody ShelterCategoryDTO shelterCreationRequest) {
    return ApiResponse.<ShelterCategoryDTO>builder()
            .result(shelterService.updateShelter(id, shelterCreationRequest))
            .build();
  }

  @DeleteMapping("/{id}")
  public ApiResponse<String> deleteShelter(@PathVariable Integer id) {
    shelterService.deleteShelter(id);
    return ApiResponse.<String>builder()
            .result("The shelter has been deleted")
            .build();
  }

  @GetMapping("/{id}")
  public ApiResponse<ShelterCategoryDTO> getShelterById(@PathVariable Integer id) {
    return ApiResponse.<ShelterCategoryDTO>builder()
            .result(shelterService.getShelterById(id))
            .build();
  }

  @GetMapping("/shelterCategory")
  public ApiResponse<List<ShelterCategoryDTO>> getAllShelters() {
    return ApiResponse.<List<ShelterCategoryDTO>>builder()
            .result(shelterService.getAllShelterCategory())
            .build();
  }
}
