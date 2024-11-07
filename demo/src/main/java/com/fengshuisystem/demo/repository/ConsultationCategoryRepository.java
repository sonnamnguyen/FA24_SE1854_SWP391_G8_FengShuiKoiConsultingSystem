package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.ConsultationCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationCategoryRepository extends JpaRepository<ConsultationCategory, Integer> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
}
