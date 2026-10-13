package com.alpex.golden_copy_service.repository;

import com.alpex.golden_copy_service.model.DataExceptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataExceptionRepository extends JpaRepository<DataExceptionEntity, Integer> {

}
