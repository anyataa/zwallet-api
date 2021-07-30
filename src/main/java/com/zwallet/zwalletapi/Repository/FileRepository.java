package com.zwallet.zwalletapi.Repository;

import com.zwallet.zwalletapi.Model.Entity.FileEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {

}
