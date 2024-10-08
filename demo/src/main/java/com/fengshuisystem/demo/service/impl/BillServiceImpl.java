    package com.fengshuisystem.demo.service.impl;
    import com.fengshuisystem.demo.entity.Bill;
    import com.fengshuisystem.demo.exception.AppException;
    import com.fengshuisystem.demo.exception.ErrorCode;
    import com.fengshuisystem.demo.repository.BillRepository;
    import com.fengshuisystem.demo.service.BillService;
    import org.springframework.stereotype.Service;
    import java.util.List;
    import java.util.Optional;
    @Service

    public class BillServiceImpl implements BillService {
        private final BillRepository billRepository;

        public BillServiceImpl(BillRepository billRepository) {
            this.billRepository = billRepository;
        }

        @Override
        public List<Bill> getAllBills() {
            return billRepository.findAll();
        }

        @Override
        public Bill getBillById(Integer id) {
            return billRepository.findById(id)
                    .orElseThrow(()->new AppException(ErrorCode.BILL_NOT_EXISTED));
        }

        @Override
        public Bill createBill(Bill bill) {
            return billRepository.save(bill);
        }

        @Override
        public Bill updateBill(Integer id, Bill bill) {
            Optional<Bill> existingBill = billRepository.findById(id);
            if (existingBill.isPresent()) {
                Bill updatedBill = existingBill.get();
                updatedBill.setSubAmount(bill.getSubAmount());
                updatedBill.setVATAmount(bill.getVATAmount());
                updatedBill.setTotalAmount(bill.getTotalAmount());
                updatedBill.setCreateDate(bill.getCreateDate());
                updatedBill.setUpdateDate(bill.getUpdateDate());
                updatedBill.setCreateBy(bill.getCreateBy());
                updatedBill.setUpdateBy(bill.getUpdateBy());
                updatedBill.setPayment(bill.getPayment());
                updatedBill.setPackageSet(bill.getPackageSet());
                updatedBill.setUser(bill.getUser());
                return billRepository.save(updatedBill);
            } else {
                throw new AppException(ErrorCode.BILL_NOT_EXISTED);
            }
        }

        @Override
        public void deleteBill(Integer id) {
            Optional<Bill> existingBill = billRepository.findById(id);
            if (existingBill.isPresent()) {
                billRepository.deleteById(id);}
            else {
                throw new AppException(ErrorCode.BILL_NOT_EXISTED);
            }
        }
    }
