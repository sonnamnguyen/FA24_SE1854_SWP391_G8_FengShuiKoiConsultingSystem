package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.BillDTO;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import com.fengshuisystem.demo.entity.enums.Request;

import java.math.BigDecimal;

public interface BillService {
    BillDTO createBillByRequestAndPayment(Integer requestId, Integer paymentId);
    BillDTO getBillById(Integer billId);
    void updateStatusAfterPayment(Integer billId, BillStatus billStatus, Request requestStatus);
    Integer getRequestIdByBillId(Integer billId);
 BigDecimal getTotalIncomeThisMonth();
}
