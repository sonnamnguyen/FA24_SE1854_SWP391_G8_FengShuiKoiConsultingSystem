package com.fengshuisystem.demo.service;
import com.fengshuisystem.demo.dto.BillDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.enums.BillStatus;


public interface BillService {
    public BillDTO createBill(BillDTO billDTO);
    public PageResponse<BillDTO> getBills(int page, int size);
    public PageResponse<BillDTO> getAllBillsByStatus(BillStatus status, int page, int size);
    public PageResponse<BillDTO> getBillsByAccountIdAndStatus(int accountId, BillStatus status,int page, int size);
    public void deleteBill(Integer id);
    public BillDTO updateBill(Integer id, BillDTO billDTO);
}
