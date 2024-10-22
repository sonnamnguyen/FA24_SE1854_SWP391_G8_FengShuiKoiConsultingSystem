package com.fengshuisystem.demo.mapper;
import com.fengshuisystem.demo.dto.PackageDTO;
import com.fengshuisystem.demo.entity.Package;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class

)
public interface PackageMapper extends EntityMapper<PackageDTO, Package> {
}
