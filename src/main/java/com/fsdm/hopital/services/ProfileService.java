package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.Profile;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private boolean isSet(Object field){
        return field != null;
    }
    @SneakyThrows
    public Profile updateProfile(Profile profile){
        Profile profileToSave = new Profile();
        if(!isSet(profile.getId()))
            throw new AppException(ProcessingException.INVALID_OPERATON);
        else profileToSave.setId(profile.getId());
        if(isSet(profile.getAddress()) && !profile.getAddress().isEmpty())
            profileToSave.setAddress(profile.getAddress());
        if(isSet(profile.getImageUrl()) && !profile.getImageUrl().isEmpty())
            profileToSave.setImageUrl(profile.getImageUrl());
        if(isSet(profile.getGender()))
             profileToSave.setGender(profile.getGender());
        if(isSet(profile.getBirthDate()))
             profileToSave.setBirthDate(profile.getBirthDate());
        if(isSet(profile.getPhoneNumber()))
             profileToSave.setPhoneNumber(profile.getPhoneNumber());
        return profileRepository.save(profileToSave);
    }
    public Profile createProfile(Profile profile){
       return profileRepository.save(profile);
    }

}
