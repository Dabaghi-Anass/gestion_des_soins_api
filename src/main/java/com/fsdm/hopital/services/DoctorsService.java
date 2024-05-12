package com.fsdm.hopital.services;

import com.fsdm.hopital.dto.DoctorDTO;
import com.fsdm.hopital.entities.Doctor;
import com.fsdm.hopital.entities.Speciality;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.DoctorRepository;
import com.fsdm.hopital.repositories.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DoctorsService {
    private final DoctorRepository doctorRepository;
    private final SpecialityRepository specialityRepository;
    private final UserService userService;
    @SneakyThrows
    public Doctor createDoctor(Doctor doctor){
        if(doctor == null) throw new AppException(ProcessingException.INVALID_USER_DETAILS);
        return doctorRepository.save(doctor);
    }
    @SneakyThrows
    public Doctor getDoctorById(Long id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if(doctorOptional.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        return doctorOptional.get();
    }
    @SneakyThrows
    public Doctor updateDoctor(DoctorDTO doctorDTO) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorDTO.getId());
        if(doctorOptional.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        Doctor doctor = doctorOptional.get();
        if(doctorDTO.getSpecialities() != null){
            final List<Speciality> specialityList = doctor.getSpecialities() == null ?
                    new ArrayList<>() :
                    doctor.getSpecialities();
            List<Speciality> specialityListFromDTO =
                    doctorDTO
                    .getSpecialities()
                    .stream()
                    .filter(s ->
                     specialityList.stream().noneMatch(sp -> sp.getCategory().equals(s)))
                    .map(s ->  Speciality.builder().category(s).build()).toList();

            List<Speciality> specs = specialityRepository.saveAllAndFlush(specialityListFromDTO);
            specialityList.addAll(specs);
            doctor.setSpecialities(specialityList);
        }
        return doctorRepository.save(doctor);
    }
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

}