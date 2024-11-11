package com.fengshuisystem.demo.mapper;
import com.fengshuisystem.demo.dto.PaymentDTO;
import com.fengshuisystem.demo.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Override
    Payment toEntity(PaymentDTO dto);

    @Override
    PaymentDTO toDto(Payment payment);
}