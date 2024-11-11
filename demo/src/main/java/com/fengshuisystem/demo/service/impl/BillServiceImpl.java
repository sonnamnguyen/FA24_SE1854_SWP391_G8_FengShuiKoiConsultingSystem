
package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.BillDTO;
import com.fengshuisystem.demo.dto.ConsultationResultDTO;
import com.fengshuisystem.demo.dto.PageResponse;
import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.Bill;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.Payment;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import com.fengshuisystem.demo.entity.enums.Request;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.BillMapper;
import com.fengshuisystem.demo.repository.BillRepository;
import com.fengshuisystem.demo.repository.ConsultationRequestRepository;
import com.fengshuisystem.demo.repository.PaymentRepository;
import com.fengshuisystem.demo.repository.UserRepository;
import com.fengshuisystem.demo.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final BillMapper billMapper;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @PreAuthorize("hasRole('USER')")
    @Override
    public BillDTO createBillByRequestAndPayment(Integer requestId, Integer paymentId) {
        String email = getCurrentUserEmailFromJwt();
        log.info("Fetched email from JWT: {}", email);

        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + email));
        log.info("Account found: {}", account.getEmail());

        ConsultationRequest consultationRequest = consultationRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
        log.info("ConsultationRequest found for ID: {}", requestId);

        // Retrieve Payment entity
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        log.info("Payment found for ID: {}", paymentId);

        BigDecimal subAmount = consultationRequest.getPackageId().getPrice();
        BigDecimal vat = BigDecimal.valueOf(0.1);
        BigDecimal vatAmount = subAmount.multiply(vat);
        BigDecimal totalAmount = subAmount.add(vatAmount);

        Bill bill = new Bill();
        bill.setAccount(account);
        bill.setConsultationRequest(consultationRequest);
        bill.setPayment(payment); // Set the Payment here
        bill.setSubAmount(subAmount);
        bill.setVat(vat);
        bill.setVatAmount(vatAmount);
        bill.setTotalAmount(totalAmount);
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedDate(Instant.now());

        log.info("Creating Bill for Account: {} with total amount: {}", account.getEmail(), totalAmount);
        return billMapper.toDto(billRepository.save(bill));
    }

    @Override
    public BillDTO getBillById(Integer billId) {
        return billRepository.findById(billId)
                .map(billMapper::toDto)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public void updateStatusAfterPayment(Integer billId, BillStatus billStatus, Request requestStatus) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));

        ConsultationRequest consultationRequest = bill.getConsultationRequest();
        if (consultationRequest == null) {
            throw new AppException(ErrorCode.REQUEST_NOT_FOUND);
        }

        bill.setStatus(billStatus);
        consultationRequest.setStatus(requestStatus);

        billRepository.save(bill);
        consultationRequestRepository.save(consultationRequest);
    }

    @Override
    public Integer getRequestIdByBillId(Integer billId) {
        return billRepository.findById(billId)
                .map(bill -> bill.getConsultationRequest().getId())
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));
    }

    private String getCurrentUserEmailFromJwt() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaimAsString("sub");
        log.info("Extracted email from JWT: {}", email);
        return email;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public BigDecimal getTotalIncomeThisMonth() {
        return billRepository.getTotalIncomeThisMonth();
    }

    @Override
    public PageResponse<BillDTO> getAllBills(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = billRepository.findAll(pageable);
        if(pageData.isEmpty()) {
            throw new AppException(ErrorCode.BILL_NOT_EXISTED);
        }
        return PageResponse.<BillDTO>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(billMapper::toDto).toList())
                .build();
    }

    @Override
    public List<BillDTO> getAll() {
        List<BillDTO> billDTOS= billRepository.findAll()
                .stream()
                .map(billMapper::toDto)
                .toList();
        if (billDTOS.isEmpty()) {
            throw new AppException(ErrorCode.BILL_NOT_EXISTED);
        }
        return billDTOS;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<BillDTO> searchBills(BillStatus status, String createdBy, BigDecimal minTotalAmount, BigDecimal maxTotalAmount, String paymentMethod) {
        List<Bill> bills = billRepository.findAll();

        // Lọc theo trạng thái
        if (status != null) {
            bills = bills.stream().filter(bill -> bill.getStatus().equals(status)).collect(Collectors.toList());
        }

        // Lọc theo người tạo
        if (createdBy != null && !createdBy.isEmpty()) {
            bills = bills.stream().filter(bill -> bill.getCreatedBy().toLowerCase().contains(createdBy.toLowerCase())).collect(Collectors.toList());
        }

        // Lọc theo khoảng giá trị của TotalAmount
        if (minTotalAmount != null || maxTotalAmount != null) {
            if (minTotalAmount != null && maxTotalAmount != null) {
                // Khi cả min và max đều có giá trị
                bills = bills.stream()
                        .filter(bill -> bill.getTotalAmount().compareTo(minTotalAmount) >= 0 && bill.getTotalAmount().compareTo(maxTotalAmount) <= 0)
                        .collect(Collectors.toList());
            } else if (minTotalAmount != null) {
                // Khi chỉ có minTotalAmount
                bills = bills.stream()
                        .filter(bill -> bill.getTotalAmount().compareTo(minTotalAmount) >= 0)
                        .collect(Collectors.toList());
            } else if (maxTotalAmount != null) {
                // Khi chỉ có maxTotalAmount
                bills = bills.stream()
                        .filter(bill -> bill.getTotalAmount().compareTo(maxTotalAmount) <= 0)
                        .collect(Collectors.toList());
            }
        }

        // Lọc theo phương thức thanh toán
        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            bills = bills.stream()
                    .filter(bill -> bill.getPayment().getPaymentMethod().toLowerCase().contains(paymentMethod.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Trả về danh sách hóa đơn đã được chuyển đổi thành DTO
        return bills.stream().map(billMapper::toDto).collect(Collectors.toList());
    }
}
