package com.fsdm.hopital.controllers;

import com.fsdm.hopital.entities.Profile;
import com.fsdm.hopital.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping("/create")
    public Profile createProfile(@RequestBody Profile profile){
        return profileService.createProfile(profile);
    }
}
