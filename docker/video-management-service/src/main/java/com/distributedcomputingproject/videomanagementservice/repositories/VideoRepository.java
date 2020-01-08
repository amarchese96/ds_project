package com.distributedcomputingproject.videomanagementservice.repositories;

import com.distributedcomputingproject.videomanagementservice.entities.Video;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<Video, Integer> {
    boolean existsByName(String name);
}
