package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.*;
import com.fengshuisystem.demo.entity.*;
import com.fengshuisystem.demo.entity.Package;
import com.fengshuisystem.demo.entity.enums.BillStatus;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.BillMapper;
import com.fengshuisystem.demo.repository.*;
import com.fengshuisystem.demo.service.BillService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class BillServiceImpl implements BillService {
    PaymentRepository paymentRepository;
    BillMapper billMapper;
    UserRepository userRepository;
    BillRepository billRepository;
    PackageRepository packageRepository;
    ConsultationRequestRepository consultationRequestRepository;

    @PreAuthorize("hasRole('USER')")
    @Override
    public BillDTO createBill(BillDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Payment payment = paymentRepository.findById(request.getPayment().getId()).orElseThrow(()->new AppException(ErrorCode.PAYMENT_NOT_EXISTED));
        Account account = userRepository.findByUserName(name).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        Set<Package> packages = new HashSet<>();
        if (request.getPackageFields() != null) {
            for (PackageDTO packageDTO : request.getPackageFields()) {
                Package pkg = packageRepository.findById(packageDTO.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_EXISTED));
                packages.add(pkg);
            }
        }
        Bill bill = billMapper.toEntity(request);
        bill.setPackageFields(packages);
        bill.setAccount(account);
        bill.setPayment(payment);
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedBy(name);
        return billMapper.toDto(billRepository.save(bill));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PageResponse<BillDTO> getBills(int page, int size) {
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
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PageResponse<BillDTO> getAllBillsByStatus(BillStatus status, int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = billRepository.findAllByStatus(status, pageable);
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Override
    public PageResponse<BillDTO> getBillsByAccountIdAndStatus(int accountId, BillStatus status, int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = billRepository.findAllByAccount_IdAndStatus(accountId,status,pageable);
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Override
    public void deleteBill(Integer id) {
        var bill = billRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));
        bill.setStatus(BillStatus.CANCELLED);
        billRepository.save(bill);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public BillDTO updateBill(Integer id, BillDTO request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Bill bill = billRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));
        Set<Package> packages = new HashSet<>();
        if (request.getPackageFields() != null) {
            for (PackageDTO packageDTO : request.getPackageFields()) {
                Package pkg = packageRepository.findById(packageDTO.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_EXISTED));
                packages.add(pkg);
            }
        }
        billMapper.update(request, bill);
        bill.setPackageFields(packages);
        bill.setUpdatedBy(name);
        bill.setUpdatedDate(Instant.now());
        return billMapper.toDto(billRepository.save(bill));
    }

    // Phần của Khôi
    @Override
    @PreAuthorize("hasRole('USER')")
    public BillDTO createBillByPaymentAndPackage(BillDTO billRequest, Integer packageId, Integer paymentId) {
        // 1. Lấy email từ JWT principal
        String email = getCurrentUserEmailFromJwt();
        log.info("Fetched email from JWT: {}", email);

        // 2. Lấy tài khoản từ UserRepository bằng email
        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + email));
        log.info("Account found: {}", account.getEmail());


        // Tìm Payment theo ID
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));

        // Tìm Package theo ID và lấy giá
        Package packageEntity = packageRepository.findById(packageId)
                .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_EXISTED));
        BigDecimal subAmount = packageEntity.getPrice();  // Lấy giá từ package

        // Tính toán VAT và tổng tiền
        BigDecimal vat = BigDecimal.valueOf(0.1); // VAT mặc định 10%
        BigDecimal vatAmount = subAmount.multiply(vat);
        BigDecimal totalAmount = subAmount.add(vatAmount);

        // Tạo entity từ BillDTO và gán giá trị liên quan
        Bill bill = billMapper.toEntity(billRequest);
        bill.setAccount(account);
        bill.setPayment(payment);
        bill.setSubAmount(subAmount);
        bill.setVat(vat);
        bill.setVatAmount(vatAmount);
        bill.setTotalAmount(totalAmount);
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedBy(account.getUserName());
        bill.setCreatedDate(Instant.now());

        // Lưu vào cơ sở dữ liệu và trả về DTO
        Bill savedBill = billRepository.save(bill);
        return billMapper.toDto(savedBill);
    }

    // Phương thức lấy email từ JWT
    private String getCurrentUserEmailFromJwt() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaimAsString("sub");  // Lấy email từ claim "email"
        log.info("Extracted email from JWT: {}", email);
        return email;
    }

}
