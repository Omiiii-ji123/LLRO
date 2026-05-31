package com.logistics.routeengine.modern.repository;

import com.logistics.routeengine.modern.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByName(String name);
    boolean existsByName(String name);
}