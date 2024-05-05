package com.fsdm.hopital.services;

import com.fsdm.hopital.dto.CareActivityDTO;
import com.fsdm.hopital.dto.EntityToDto;
import com.fsdm.hopital.entities.CareActivity;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.CareActivityRepository;
import com.fsdm.hopital.types.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class CareActivityService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CareActivityRepository careActivityRepository;
    private final UserService userService;
    private final PatientsService patientsService;
    public static boolean containsOverlap(List<CareActivity> careActivities, CareActivityDTO careActivity) {
        if (careActivity.getDate() == null) return true;
        LocalDateTime newCareActivityStart = careActivity.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        var hours = (int) Math.floor(careActivity.getDuration());
        var minutes = (int)(careActivity.getDuration() - hours) * 60;
        LocalDateTime newCareActivityEnd = newCareActivityStart.plusHours(hours).plusMinutes(minutes);
        for (CareActivity existingCareActivity : careActivities) {
            var ehours = (int) Math.floor(existingCareActivity.getDuration());
            var eminutes = (int)(existingCareActivity.getDuration() - hours) * 60;
            LocalDateTime existingCareActivityStart = existingCareActivity.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime existingCareActivityEnd = existingCareActivityStart.plusHours(ehours).plusMinutes(eminutes);
            if (newCareActivityStart.isBefore(existingCareActivityEnd) && newCareActivityEnd.isAfter(existingCareActivityStart)) {
                if(existingCareActivity.isAccepted()) return true;
            }
        }
        return false;
    }
    public static List<CareActivity> containsOverlap(List<CareActivity> careActivities, CareActivity careActivity) {
        List<CareActivity> overlaps = new ArrayList<>();
        LocalDateTime newCareActivityStart = careActivity.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        var hours = (int) Math.floor(careActivity.getDuration());
        var minutes = (int)((careActivity.getDuration() - hours) * 60);
        LocalDateTime newCareActivityEnd = newCareActivityStart.plusHours(hours).plusMinutes(minutes);
        for (CareActivity existingCareActivity : careActivities) {
            if(existingCareActivity.getId() == careActivity.getId()) continue;
            var ehours = (int) Math.floor(existingCareActivity.getDuration());
            var eminutes = (int)((existingCareActivity.getDuration() - hours) * 60);
            LocalDateTime existingCareActivityStart = existingCareActivity.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime existingCareActivityEnd = existingCareActivityStart.plusHours(ehours).plusMinutes(eminutes);
            if (newCareActivityStart.isBefore(existingCareActivityEnd) && newCareActivityEnd.isAfter(existingCareActivityStart)) {
                if(existingCareActivity.isAccepted()) overlaps.add(careActivity);
                else overlaps.add(existingCareActivity);
            }
        }
        return overlaps;
    }
    boolean careActivityTaken(CareActivityDTO careActivity){
        Long userId = careActivity.getCaregiverId();
        Long  patientId = careActivity.getPatientId();
        List<CareActivity> userCareActivitys = getAllUserCareActivitys(userId, 0,0);
        List<CareActivity> patientCareActivitys = getAllUserCareActivitys(patientId, 0,0);
        if(userCareActivitys != null)
            if(containsOverlap(userCareActivitys, careActivity)) return true;
        if(patientCareActivitys != null)
            if(containsOverlap(patientCareActivitys, careActivity)) return true;
        return false;
    }
    @SneakyThrows
    public CareActivity createCareActivity(CareActivityDTO careActivity) {
        if(careActivityTaken(careActivity)) throw new AppException(ProcessingException.USER_NOT_AVAILABLE_AT_THIS_TIME);
        CareActivity careActivity1 = EntityToDto.careActivityDTOToCareActivity(careActivity,patientsService, userService);
        return careActivityRepository.save(careActivity1);
    }
    @SneakyThrows
    public CareActivity updateCareActivity(CareActivityDTO careActivity){
        if(careActivityTaken(careActivity)) throw new AppException(ProcessingException.USER_NOT_AVAILABLE_AT_THIS_TIME);
        if (careActivity == null) throw new AppException(ProcessingException.CARE_ACTIVITY_NOT_FOUND);
        CareActivity careActivity1 =
                careActivityRepository
                        .findById(careActivity.getId())
                        .orElseThrow(() -> new AppException(ProcessingException.CARE_ACTIVITY_NOT_FOUND));
        if(careActivity.getDate() != null) careActivity1.setDate(careActivity.getDate());
        if(careActivity.getStatus() != null) careActivity1.setStatus(careActivity.getStatus());
        if(careActivity.getType() != null) careActivity1.setType(careActivity.getType());
        if(careActivity.getDescription() != null) careActivity1.setDescription(careActivity.getDescription());
        return careActivityRepository.save(careActivity1);
    }
    public List<Date> getSchedule(Long id){
        return getAllUserCareActivitys(id, 0,0).stream().map(CareActivity::getDate).toList();
    }
    public void deleteCareActivity(Long id){
        careActivityRepository.deleteById(id);
    }
    @SneakyThrows
    public CareActivity getCareActivityById(Long id){
        return careActivityRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ProcessingException.CARE_ACTIVITY_NOT_FOUND));
    }
    class CareActivityDateComparator implements Comparator<CareActivity> {
        @Override
        public int compare(CareActivity careActivity1, CareActivity careActivity2) {
            return careActivity2.getDate().compareTo(careActivity1.getDate());
        }
    }
    public List<CareActivity> getAllUserCareActivitys(Long user_id, int offset, int limit){
        AtomicBoolean mutatedArray = new AtomicBoolean(false);
        List<CareActivity> careActivities;
        if(limit == 0 && offset == 0) {
            careActivities =  careActivityRepository.findAllByUserId(user_id);
        }else {
            Pageable page = PageRequest.of(offset,limit);
            careActivities =  careActivityRepository.findAllByUserId(user_id, page);
            careActivities.sort(new CareActivityService.CareActivityDateComparator());
        }
        careActivities.forEach(careActivity -> {
            if(careActivity.getDate().before(new Date()) && !careActivity.isAccepted()){
                careActivity.setStatus(Status.DENIED);
                mutatedArray.set(true);
            }else if(careActivity.getDate().before(new Date()) && careActivity.isAccepted()){
                careActivity.setStatus(Status.DONE);
                mutatedArray.set(true);
            }
        });
        if(mutatedArray.get()) return careActivityRepository.saveAllAndFlush(careActivities);
        return careActivities;
    }

    public void deleteAllUserCareActivitys(Long user_id) {
        careActivityRepository.deleteAllByUserId(user_id);
    }

    public CareActivity acceptCareActivity(Long id) {
        CareActivity careActivity = getCareActivityById(id);
        careActivity.setAccepted(true);
        careActivity.setStatus(Status.SCHEDULED);
        CareActivity savedCareActivity = careActivityRepository.saveAndFlush(careActivity);
        List<CareActivity> userCareActivitys = getAllUserCareActivitys(savedCareActivity.getCaregiver().getId(), 0,0); //doctor or nurse
        List<CareActivity> patientCareActivitys = getAllUserCareActivitys(savedCareActivity.getPatient().getId(), 0,0); //patient
        if(userCareActivitys != null){
            List<CareActivity> overlaps = containsOverlap(userCareActivitys, savedCareActivity);
            System.out.println(overlaps);
            if(overlaps.size() > 0){
                overlaps.forEach(careActivity1 -> {
                    if(careActivity1.isAccepted()) {
                        careActivity.setAccepted(false);
                        careActivity.setStatus(Status.DENIED);
                    }else{
                        careActivity1.setStatus(Status.DENIED);
                        careActivity1.setAccepted(false);
                    }
                });
                careActivityRepository.saveAllAndFlush(overlaps);
            }
        }
        if(patientCareActivitys != null){
            List<CareActivity> overlaps = containsOverlap(patientCareActivitys, savedCareActivity);
            System.out.println(overlaps);
            if(overlaps.size() > 0){
                overlaps.forEach(careActivity1 -> {
                    if(careActivity1.isAccepted()) {
                        careActivity.setAccepted(false);
                        careActivity.setStatus(Status.DENIED);
                    }else{
                        careActivity1.setStatus(Status.DENIED);
                        careActivity1.setAccepted(false);
                    }
                });
                careActivityRepository.saveAllAndFlush(overlaps);
            }
        }
        return savedCareActivity;
    }
    public CareActivity rejectCareActivity(Long id) {
        CareActivity careActivity = getCareActivityById(id);
        careActivity.setAccepted(false);
        careActivity.setStatus(Status.DENIED);
        return  careActivityRepository.save(careActivity);
    }
    public CareActivity cancelCareActivity(Long id) {
        CareActivity careActivity = getCareActivityById(id);
        careActivity.setStatus(Status.CANCELED);
        return careActivityRepository.save(careActivity);
    }

    public CareActivity completeCareActivity(Long id) {
        CareActivity careActivity = getCareActivityById(id);
        careActivity.setStatus(Status.DONE);
        return careActivityRepository.save(careActivity);
    }

    public CareActivity unCompleteCareActivity(Long id) {
        CareActivity careActivity = getCareActivityById(id);
        careActivity.setStatus(Status.SCHEDULED);
        return careActivityRepository.save(careActivity);
    }
}
