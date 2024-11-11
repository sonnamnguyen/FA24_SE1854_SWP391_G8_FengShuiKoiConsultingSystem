package com.fengshuisystem.demo.service.impl;

import com.fengshuisystem.demo.dto.PaymentDTO;
import com.fengshuisystem.demo.exception.AppException;
import com.fengshuisystem.demo.exception.ErrorCode;
import com.fengshuisystem.demo.mapper.PaymentMapper;
import com.fengshuisystem.demo.repository.PaymentRepository;
import com.fengshuisystem.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDTO getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .map(paymentMapper::toDto)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));
    }

    @Override
    public List<PaymentDTO> getAll() {
        List<PaymentDTO> paymentDTOS= paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toDto)
                .toList();
        if (paymentDTOS.isEmpty()) {
            throw new AppException(ErrorCode.PAYMENT_NOT_EXISTED);
        }
        return paymentDTOS;
    }
}
