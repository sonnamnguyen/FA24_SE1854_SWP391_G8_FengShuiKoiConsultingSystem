
package com.fengshuisystem.demo.controller;


import com.fengshuisystem.demo.dto.ApiResponse;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.dto.ShelterCategoryDTO;
import com.fengshuisystem.demo.service.ShelterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shelters")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ShelterController {
  ShelterService shelterService;

  @PostMapping
  public ApiResponse<ShelterCategoryDTO> createShelter(@RequestBody  ShelterCategoryDTO shelterCreationRequest) {
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

  @GetMapping("/{name}")
  public ApiResponse<PageResponse<ShelterCategoryDTO>> getShelterBySearch(
          @PathVariable String name,
          @RequestParam(value = "page", required = false, defaultValue = "1") int page,
          @RequestParam(value = "size", required = false, defaultValue = "10") int size

  ) {
    return ApiResponse.<PageResponse<ShelterCategoryDTO>>builder()
            .result(shelterService.getSheltersBySearch(name, page, size))
            .build();
  }

  @PutMapping("/{id}")
  public ApiResponse<ShelterCategoryDTO> updateShelter(@PathVariable Integer id, @RequestBody  ShelterCategoryDTO shelterCreationRequest) {
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
}
