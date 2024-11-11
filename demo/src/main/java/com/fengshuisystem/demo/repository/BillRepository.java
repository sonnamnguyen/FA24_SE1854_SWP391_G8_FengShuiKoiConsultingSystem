package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Bill;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT b FROM Bill b WHERE b.consultationRequest.id = :consultationRequestId")
    List<Bill> findAllByConsultationRequestId(@Param("consultationRequestId") Integer consultationRequestId);

    @Query("SELECT SUM(b.totalAmount) FROM Bill b " +
            "WHERE b.status = 'PAID' " +
            "AND MONTH(b.createdDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.createdDate) = YEAR(CURRENT_DATE)")
    BigDecimal getTotalIncomeThisMonth();

    List<Bill> findByStatus(BillStatus status);

    List<Bill> findByCreatedByContainingIgnoreCase(String createdBy);

    @Query("SELECT b FROM Bill b WHERE b.totalAmount BETWEEN :minTotalAmount AND :maxTotalAmount")
    List<Bill> findByTotalAmountBetween(@Param("minTotalAmount") BigDecimal minTotalAmount, @Param("maxTotalAmount") BigDecimal maxTotalAmount);

    @Query("SELECT b FROM Bill b WHERE LOWER(b.payment.paymentMethod) LIKE LOWER(CONCAT('%', :paymentMethod, '%'))")
    List<Bill> findByPaymentMethodContainingIgnoreCase(@Param("paymentMethod") String paymentMethod);

    List<Bill> findByTotalAmountGreaterThanEqual(BigDecimal minTotalAmount);

    List<Bill> findByTotalAmountLessThanEqual(BigDecimal maxTotalAmount);
}

