package com.example.petbutler.utils;

import com.example.petbutler.exception.ButlerApiException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.model.AnimalHosptlApiForm;
import com.example.petbutler.persist.AnimalHosptlRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HospitalApiProvider {

  @Value("${map.hospital.key}")
  public String hospitalApiKey;

  private final JSONParser jsonParser;

  private final AnimalHosptlRepository animalHosptlRepository;

  public long getTotalCount(){

    String apiUrl = String.format("https://openapi.gg.go.kr/Animalhosptl?"
        + "KEY=%s&Type=json&pIndex=%d&pSize=%d", hospitalApiKey, 1, 1);

    try {

      JSONObject jsonObject = (JSONObject) jsonParser.parse(getResultAsString(apiUrl));
      JSONObject mainData   = (JSONObject) ((JSONArray) jsonObject.get("Animalhosptl")).get(0);
      JSONObject headData   = (JSONObject) ((JSONArray) mainData.get("head")).get(0);
      String totalCount     = headData.get("list_total_count").toString();
      return Long.parseLong(totalCount);

    } catch(ParseException e){

      throw new ButlerApiException(ErrorCode.API_JSON_PARSE_EXCEPTION);

    }
  }

  public List<AnimalHosptlApiForm> getResultList(long pIndex, long pSize){

    String apiUrl = String.format("https://openapi.gg.go.kr/Animalhosptl?"
        + "KEY=%s&Type=json&pIndex=%d&pSize=%d", hospitalApiKey, pIndex, pSize);

    String     jsonString = getResultAsString(apiUrl);
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;

    try {

      jsonObject = (JSONObject) jsonParser.parse(jsonString);

    } catch(ParseException e){

      throw new ButlerApiException(ErrorCode.API_JSON_PARSE_EXCEPTION);

    }

    JSONObject mainData = (JSONObject) ((JSONArray) jsonObject.get("Animalhosptl")).get(1);
    JSONArray  rowsData = (JSONArray) mainData.get("row");

    List<AnimalHosptlApiForm> forms = new ArrayList();

    for (int i = 0; i < rowsData.size(); i++) {

      try {

        JSONObject rowData = (JSONObject) rowsData.get(i);

        String address    = rowData.get("REFINE_ROADNM_ADDR").toString();
        String status     = rowData.get("BSN_STATE_DIV_CD").toString();

        validateHospitalData(address, status);

        String hosptlName = rowData.get("BIZPLC_NM").toString();
        String sigun      = rowData.get("SIGUN_NM").toString();
        String phone      = rowData.get("LOCPLC_FACLT_TELNO").toString();
        double lat        = Double.parseDouble(rowData.get("REFINE_WGS84_LAT").toString());
        double lon        = Double.parseDouble(rowData.get("REFINE_WGS84_LOGT").toString());

        AnimalHosptlApiForm form = AnimalHosptlApiForm.builder()
            .sigun(sigun).hosptlName(hosptlName).phone(phone).address(address).lat(lat).lon(lon).build();

        forms.add(form);

      } catch (NullPointerException | ButlerApiException e) {

        log.info(e.getMessage());

      }

    }

    return forms;

  }

  private void validateHospitalData(String address, String status) {
    
    // 이미 데이터가 있는 경우
    if (animalHosptlRepository.existsByAddress(address)) {
      throw new ButlerApiException(ErrorCode.API_DATA_ALREADY_EXIST);
    }

    // 병원이 폐쇄된 경우
    if ("0000".equals(status)) {
      throw new ButlerApiException(ErrorCode.API_DATA_HOSPITAL_SHUTDOWN);
    }
    
  }

  private String getResultAsString(String apiUrl){

    try {

      URL url = new URL(apiUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();

      BufferedReader br;

      if (responseCode == 200) {
        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      } else {
        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
      }

      String inputLine;
      StringBuilder response = new StringBuilder();

      while((inputLine = br.readLine()) != null) {
        response.append(inputLine);
      }
      br.close();

      return response.toString();

    } catch(Exception e) {

      return "failed to get response";

    }
  }

}
