package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.dto.ConsultationRequestDetailDTO;

public interface ConsultationRequestDetailService {

    // Retrieve a ConsultationRequestDetail by its ID
    ConsultationRequestDetailDTO getRequestDetailById(Integer requestDetailId);

    // Create or update a ConsultationRequestDetail with a given packageId
    ConsultationRequestDetailDTO createOrUpdateDetail(ConsultationRequestDetailDTO detailDTO, Integer packageId);

    // Delete a ConsultationRequestDetail by ID
    void  cancelRequestDetailById(Integer requestDetailId);
}
