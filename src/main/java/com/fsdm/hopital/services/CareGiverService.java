package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.CareGiver;
import com.fsdm.hopital.entities.Nurse;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.CareGiverRepository;
import com.fsdm.hopital.repositories.NurseRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CareGiverService {
    private final CareGiverRepository careGiverRepository;
    @SneakyThrows
    public CareGiver createCareGiver(CareGiver careGiver) {
        if (careGiver == null) throw new AppException(ProcessingException.INVALID_OPERATON);
        return careGiverRepository.save(careGiver);
    }
}

