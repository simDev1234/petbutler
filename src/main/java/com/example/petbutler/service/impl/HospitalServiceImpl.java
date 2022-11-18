package com.example.petbutler.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HospitalServiceImpl {

  @Value("${map.hospital.key}")
  private String KEY;



}
