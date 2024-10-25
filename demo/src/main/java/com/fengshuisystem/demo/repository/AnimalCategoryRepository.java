package com.fengshuisystem.demo.repository;
import com.fengshuisystem.demo.entity.AnimalCategory;
import com.fengshuisystem.demo.entity.ConsultationRequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AnimalCategoryRepository extends JpaRepository<AnimalCategory, Integer> {
    @Query("SELECT a FROM AnimalCategory a WHERE a.id IN :ids")
    List<AnimalCategory> findAllByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT ac FROM AnimalCategory ac JOIN ac.consultationRequestDetails crd WHERE crd.id = :requestDetailId")
    List<AnimalCategory> findByConsultationRequestDetailsId(@Param("requestDetailId") Integer requestDetailId);
}