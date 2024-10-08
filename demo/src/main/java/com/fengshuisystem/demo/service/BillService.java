package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.entity.Bill;

import java.util.List;
import java.util.Optional;

public interface BillService {
    List<Bill> getAllBills();
    Bill getBillById(Integer id);
    Bill createBill(Bill bill);
    Bill updateBill(Integer id, Bill bill);
    void deleteBill(Integer id);
}
