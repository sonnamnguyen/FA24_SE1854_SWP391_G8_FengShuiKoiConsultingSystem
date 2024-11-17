package com.fengshuisystem.demo.repository;

import com.fengshuisystem.demo.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT b FROM Bill b WHERE b.consultationRequest.id = :consultationRequestId")
    List<Bill> findAllByConsultationRequestId(@Param("consultationRequestId") Integer consultationRequestId);

    @Query("SELECT SUM(b.totalAmount) FROM Bill b " +
            "WHERE b.status = 'PAID' " +
            "AND MONTH(b.createdDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.createdDate) = YEAR(CURRENT_DATE)")
    BigDecimal getTotalIncomeThisMonth();

    @Query("SELECT MONTH(b.createdDate) AS month, SUM(b.totalAmount) AS totalAmount " +
            "FROM Bill b " +
            "WHERE FUNCTION('YEAR', b.createdDate) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY MONTH(b.createdDate) " +
            "ORDER BY MONTH(b.createdDate)")
    List<Object[]> countTotalMoneyPerMonthForCurrentYear();
}

