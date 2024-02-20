package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public int createCredential(Credential credential, User user){
        Credential newCredential = new Credential();
        newCredential.setUrl(credential.getUrl());
        newCredential.setKey(credential.getKey());
        newCredential.setUsername(credential.getUsername());
        newCredential.setPassword(credential.getPassword());
        newCredential.setUserid(user.getUserid());
        return credentialMapper.createCredential(newCredential);
    }

    public List<Credential> getCredentials(User user){
        return credentialMapper.getCredentialsByUserId(user.getUserid());
    }

    public int updateCredential(Credential credential, User user){
        return credentialMapper.updateCredential(credential.getCredentialid(),
                credential.getUsername(),
                credential.getUrl(),
                credential.getKey(),
                credential.getPassword());
    }

    public void deleteCredential(int credentialid) {
        credentialMapper.deleteCredential(credentialid);
    }
}
