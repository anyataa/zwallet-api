package com.zwallet.zwalletapi;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zwallet.zwalletapi.Property.FileStorageProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = FileStorageProperties.class)
public class ZwalletApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwalletApiApplication.class, args);
	}

}
