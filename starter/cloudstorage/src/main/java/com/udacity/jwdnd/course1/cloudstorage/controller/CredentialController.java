package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/home/credentials")
public class CredentialController {
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;
    private final CredentialService credentialService;

    public CredentialController(UserMapper userMapper, EncryptionService encryptionService, CredentialService credentialService) {
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
        this.credentialService = credentialService;
    }

    @PostMapping
    public String createCredentialAndUpdateCredential(Authentication authentication,
                                                      CredentialForm newCredential,
                                                      Model model){
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(loggedInUserName);
        Integer userId = user.getUserid();
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(newCredential.getPassword(), encodedKey);
        Credential credential = new Credential(newCredential.getCredentialid(),
                newCredential.getUrl(),
                newCredential.getUsername(),
                encodedKey,
                encryptedPassword,
                userId);
        if (newCredential.getCredentialid() != null) {
            credentialService.updateCredential(credential, user);
        } else {
            credentialService.createCredential(credential, user);
        }
        model.addAttribute("result", "success");
        return "result";
    }

    @GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential(Authentication authentication,
                                   @PathVariable Integer credentialId,
                                   @ModelAttribute("newCredential") CredentialForm newCredential,
                                   Model model) {
        credentialService.deleteCredential(credentialId);
        String loggedInUserName = (String) authentication.getPrincipal();
//        User user = userMapper.getUserByName(loggedInUserName);
//        model.addAttribute("credentials", credentialService.getCredentials(user));
        model.addAttribute("result", "success");
        return "result";
    }
}
