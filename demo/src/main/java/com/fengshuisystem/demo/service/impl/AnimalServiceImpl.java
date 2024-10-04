package com.fengshuisystem.demo.service.impl;
import com.fengshuisystem.demo.dto.AnimalCategoryDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.AnimalMapper;
import com.fengshuisystem.demo.repository.AnimalRepository;
import com.fengshuisystem.demo.service.AnimalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.Instant;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AnimalServiceImpl implements AnimalService {
    AnimalRepository animalRepository;
    AnimalMapper animalMapper;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AnimalCategoryDTO createAnimal(AnimalCategoryDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        if(animalRepository.existsByAnimalCategoryName(request.getAnimalCategoryName())) throw new AppException(ErrorCode.ANIMAL_EXISTED);
        AnimalCategory animalCategory = animalMapper.toEntity(request);
        animalCategory.setStatus(Status.ACTIVE);
        animalCategory.setCreatedBy(name);
        return animalMapper.toDto(animalRepository.save(animalCategory));
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
   public PageResponse<AnimalCategoryDTO> getAnimalsBySearch(AnimalCategoryDTO search, int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
      var pageData = animalRepository.findAllBySearch(search, pageable);
      if(pageData.isEmpty()) {
          throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
      }

        return PageResponse.<AnimalCategoryDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(animalMapper::toDto).toList())
                .build();
   }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<AnimalCategoryDTO> getAnimals(int page, int size) {
        Status status = Status.ACTIVE;
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        var pageData = animalRepository.findAll(pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.ANIMAL_NOT_EXISTED);
        }
        return PageResponse.<AnimalCategoryDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(animalMapper::toDto).toList())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAnimal(Integer id) {
        var animalCategory = animalRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        animalCategory.setStatus(Status.DELETED);
        animalRepository.save(animalCategory);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AnimalCategoryDTO updateAnimal(Integer id, AnimalCategoryDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        AnimalCategory animalCategory = animalRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANIMAL_NOT_EXISTED));
        animalMapper.update(request, animalCategory);
        animalCategory.setUpdatedBy(name);
        animalCategory.setUpdatedDate(Instant.now());
        return animalMapper.toDto(animalRepository.save(animalCategory));
    }
}
