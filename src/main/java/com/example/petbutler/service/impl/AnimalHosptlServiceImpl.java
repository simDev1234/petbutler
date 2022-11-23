package com.example.petbutler.service.impl;

import com.example.petbutler.model.AnimalHosptlApiForm;
import com.example.petbutler.persist.AnimalHosptlRepository;
import com.example.petbutler.persist.entity.AnimalHosptl;
import com.example.petbutler.service.AnimalHosptlService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnimalHosptlServiceImpl implements AnimalHosptlService {

  private final AnimalHosptlRepository animalHosptlRepository;

  @Override
  @Transactional
  public List<AnimalHosptl> getHosptlList(AnimalHosptlApiForm form) {
    
    // 현재 위치 정보
    double currentLat = form.getCurrentLat() == 0? 37.0075201  : form.getCurrentLat();
    double currentLon = form.getCurrentLon() == 0? 126.9754626 : form.getCurrentLon();
    int level = form.getLevel() == 0? 10 : form.getLevel();

    // 반경 몇 km 까지 확인할 것인지
    double distance = getDistanceFromLevel(level);

    // 현재 위치 기준 가장 가까운 동물 병원 10군데 조회
    return animalHosptlRepository.findNearTenHosptls(currentLat, currentLon, distance);
    
  }

  private double getDistanceFromLevel(int level) {

    Map<Integer, Double> map = new HashMap();

    map.put(1, 0.02);
    map.put(2, 0.03);
    map.put(3, 0.05);
    map.put(4, 0.1);
    map.put(5, 0.25);
    map.put(6, 0.5);
    map.put(7, 1.0);
    map.put(8, 2.0);

    return map.getOrDefault(level, 10.0);

  }
}
