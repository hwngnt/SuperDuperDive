package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> getCredentialsByUserId(int userid);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential getCredential(int credentialid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) " +
            "VALUES (#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int createCredential(Credential credential);

    @Update("UPDATE CREDENTIALS " +
            "SET url = #{url}, " +
            "key = #{key}, " +
            "password = #{password}, " +
            "username = #{newUserName} " +
            "WHERE credentialid = #{credentialId}")
    int updateCredential(int credentialId, String newUserName, String url, String key, String password);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    int deleteCredential(int credentialid);
}
