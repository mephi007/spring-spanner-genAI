package com.example.demo;

// import org.springframework.cloud.gcp.data.spanner.repository.*;
import org.springframework.stereotype.Repository;

import com.google.cloud.spring.data.spanner.repository.SpannerRepository;

@Repository
public interface YogaRepository extends SpannerRepository<Yoga, String>{
    
}
