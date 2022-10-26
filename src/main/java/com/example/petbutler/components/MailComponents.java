package com.example.petbutler.components;

import com.example.petbutler.exception.ButlerException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailComponents {

  private final JavaMailSender javaMailSender;

  public void sendMail(String receiverMail, String subject, String contents) throws MailSendException {

    MimeMessagePreparator msg = new MimeMessagePreparator() {
      @Override
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(receiverMail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(contents, true);
      }
    };

    javaMailSender.send(msg);
  }

}
