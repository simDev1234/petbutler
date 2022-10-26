package com.example.petbutler.utils;

public class EmailSendUtil {

  public static String getWelcomeHTML(String uriPath, String emailAuthKey){
    return "<!DOCTYPE html>\n"
        + "<html lang=\"ko\">\n"
        + "<head>\n"
        + "  <meta charset=\"UTF-8\">\n"
        + "  <style>\n"
        + "    .btn {\n"
        + "      width : 100px;\n"
        + "      height: 30px;\n"
        + "      text-decoration: none;\n"
        + "      display:inline-block;\n"
        + "      background-color : black;\n"
        + "      color:white;\n"
        + "      border-radius:10px 10px 10px 10px;\n"
        + "      text-align: center;\n"
        + "      vertical-align: top;\n"
        + "    }\n"
        + "\n"
        + "    .btn p {\n"
        + "      display:block;\n"
        + "      font-size : 13px;\n"
        + "      margin-top: 5px;\n"
        + "    }\n"
        + "  </style>\n"
        + "</head>\n"
        + "<body>\n"
        + "  <div>\n"
        + "    <img src=\"https://nordicdairycongress.com/sites/default/files/billeder/nyheder/colourbox15033372.jpg\" height=\"200\">\n"
        + "    <p>팻집사의 집사 맴버가 되신 것을 환영합니다!<br>아래 버튼을 통해 이메일 인증을 완료해주세요.</p>\n"
        + "    <a class = 'btn' href = '"
        + uriPath
        +"/users/email-auth?authKey="
        + emailAuthKey
        + "'><p>인증 완료</p></a>\n"
        + "  </div>\n"
        + "</body>\n"
        + "</html>";
  }

}