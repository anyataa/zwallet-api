package com.zwallet.zwalletapi.Model.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;

    private String fileName;

    private String fileType;

    public FileEntity(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }
}