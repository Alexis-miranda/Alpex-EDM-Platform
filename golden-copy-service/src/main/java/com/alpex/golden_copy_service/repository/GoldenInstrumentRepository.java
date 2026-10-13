package com.alpex.golden_copy_service.repository;

import com.alpex.golden_copy_service.model.GoldenInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoldenInstrumentRepository extends JpaRepository<GoldenInstrument, String> {
    //  CRUD operations from JpaRepository
}
