package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.Profile;
import com.fsdm.hopital.entities.User;
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
    private final UserService userService;
    private boolean isSet(Object field){
        return field != null;
    }
    @SneakyThrows
    public Profile updateProfile(Profile profile){
        Profile profileToSave = profileRepository.findById(profile.getId())
                .orElseThrow(()->new AppException(ProcessingException.INVALID_OPERATON));
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
    @SneakyThrows
    public Profile updateProfileImage(String url,Long id){
        User user = userService.getUserById(id);
        Profile profileToSave = profileRepository.findById(user.getProfile().getId()).orElseThrow(()->new AppException(ProcessingException.INVALID_OPERATON));
        profileToSave.setImageUrl(url);
        return profileRepository.save(profileToSave);
    }
    public Profile createProfile(Profile profile){
       return profileRepository.save(profile);
    }

}
