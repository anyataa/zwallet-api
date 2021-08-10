package com.zwallet.zwalletapi.Config;

import java.util.Base64;

import javax.transaction.Transactional;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Encryptor {

    @Autowired
    public BasicTextEncryptor textEncryptor;

    public String encryptInt(Integer id) {
        String encData = textEncryptor.encrypt(id.toString());
        String encode = Base64.getEncoder().encodeToString(encData.getBytes());
        return encode;
    };

    public Integer decryptString(String encodedId) {
        String decodedId = new String(Base64.getDecoder().decode(encodedId));
        Integer decData = Integer.parseInt(textEncryptor.decrypt(decodedId));
        return decData;

    }

}
