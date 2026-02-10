package com.freightfox.weather.repository;

import com.freightfox.weather.model.Pincode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PincodeRepository extends JpaRepository<Pincode, String> {
}
