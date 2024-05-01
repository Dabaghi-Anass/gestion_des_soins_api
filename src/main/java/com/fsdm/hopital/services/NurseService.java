package com.fsdm.hopital.services;

import com.fsdm.hopital.dto.NurseDTO;
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

    @SneakyThrows
    public Nurse updateNurse(NurseDTO nurseDTO) {
        if (nurseDTO == null || nurseDTO.getId() == null) throw new AppException(ProcessingException.INVALID_OPERATON);
        Nurse nurse = nurseRepository.findById(nurseDTO.getId())
                .orElseThrow(() -> new AppException(ProcessingException.USER_NOT_FOUND));
        if (nurseDTO.getQualities() != null) {
            String qual = nurse.getQualities();
            String qualities = String.join(",", nurseDTO.getQualities());
            if(qual == null){
                nurse.setQualities(qualities);
            }else{
                nurse.setQualities(qual + "," + qualities);
            }
        }
        return nurseRepository.saveAndFlush(nurse);
    }
}
