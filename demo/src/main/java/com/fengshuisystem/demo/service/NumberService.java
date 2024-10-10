package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.NumberDTO;

import java.util.List;

public interface NumberService {
    List<NumberDTO> getNumbers(Integer destiny);
    NumberDTO updateNumber(Integer numberId, NumberDTO number);
}
