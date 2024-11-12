package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.BillDTO;
import com.fengshuisystem.demo.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultConfigMapper.class)
public interface BillMapper extends EntityMapper<BillDTO, Bill> {

    @Override
    @Mapping(target = "subAmount", ignore = true)
    @Mapping(target = "vat", ignore = true)
    @Mapping(target = "vatAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    Bill toEntity(BillDTO dto);

    @Override
    BillDTO toDto(Bill bill);
}
