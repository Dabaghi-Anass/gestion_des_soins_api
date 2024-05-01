package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.Nurse;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.NurseRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NurseService {
    private final NurseRepository nurseRepository;

    @SneakyThrows
    public Nurse createNurse(Nurse nurse) {
        if (nurse == null) throw new AppException(ProcessingException.INVALID_OPERATON);
        return nurseRepository.save(nurse);
    }
}
