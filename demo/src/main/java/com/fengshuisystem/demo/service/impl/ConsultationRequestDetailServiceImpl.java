package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import com.fengshuisystem.demo.entity.ShelterCategory;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.enums.Status;
import com.fengshuisystem.demo.repository.ConsultationRequestDetailRepository;
import com.fengshuisystem.demo.repository.ShelterCategoryRepository;
import com.fengshuisystem.demo.repository.AnimalCategoryRepository;
import com.fengshuisystem.demo.service.ConsultationRequestDetailService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationRequestDetailServiceImpl implements ConsultationRequestDetailService {

    private final ConsultationRequestDetailRepository repository;
    private final ShelterCategoryRepository shelterCategoryRepository;
    private final AnimalCategoryRepository animalCategoryRepository;

    public ConsultationRequestDetailServiceImpl(
            ConsultationRequestDetailRepository repository,
            ShelterCategoryRepository shelterCategoryRepository,
            AnimalCategoryRepository animalCategoryRepository) {
        this.repository = repository;
        this.shelterCategoryRepository = shelterCategoryRepository;
        this.animalCategoryRepository = animalCategoryRepository;
    }

    @Override
    @Transactional
    public ConsultationRequestDetail save(ConsultationRequestDetailDTO dto) {
        // Khởi tạo thực thể mới từ DTO
        ConsultationRequestDetail entity = new ConsultationRequestDetail();
        entity.setDescription(dto.getDescription());
        entity.setStatus(Status.COMPLETED);

        // Lấy các ShelterCategory từ DB theo ID
        List<ShelterCategory> shelterCategories = shelterCategoryRepository
                .findAllById(dto.getShelterCategoryIds());

        // Lấy các AnimalCategory từ DB theo ID
        List<AnimalCategory> animalCategories = animalCategoryRepository
                .findAllById(dto.getAnimalCategoryIds());

        // Thiết lập các danh mục vào thực thể
        entity.setShelterCategories(shelterCategories);
        entity.setAnimalCategories(animalCategories);

        // Lưu thực thể vào DB
        return repository.save(entity);
    }
}
