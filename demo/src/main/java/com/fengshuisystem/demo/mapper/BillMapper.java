package com.fengshuisystem.demo.mapper;
import com.fengshuisystem.demo.dto.BillDTO;
import com.fengshuisystem.demo.entity.Bill;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface BillMapper extends EntityMapper<BillDTO, Bill> {
}