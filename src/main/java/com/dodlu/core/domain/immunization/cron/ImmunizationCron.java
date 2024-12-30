package com.dodlu.core.domain.immunization.cron;

import static com.dodlu.core.domain.utils.Constants.IMMINENT_IMMUNIZATION_SMS_NOTIFICATION;

import com.dodlu.core.app.model.request.notification.SmsRequest;
import com.dodlu.core.domain.notification.sms.SmsService;
import com.dodlu.core.persistence.immunization.entity.Child;
import com.dodlu.core.persistence.immunization.entity.ImmunizationRecord;
import com.dodlu.core.persistence.immunization.entity.Parent;
import com.dodlu.core.persistence.immunization.entity.Vaccine;
import com.dodlu.core.persistence.immunization.repository.ImmunizationRecordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImmunizationCron {
  private final ImmunizationRecordRepository immunizationRecordRepository;
  private final SmsService smsService;

  // @Scheduled(cron = "0 0 0 * * *") // Runs every day at 12 AM
  // @Scheduled(cron = "0 * * * * *") // Runs every minute -- Testing purposes only
  public void notifyParentsOfDueVaccine() {
    log.info("fetching all immunization that are due for today");

    int pageSize = 50;
    int currentPage = 0;
    Pageable pageable = PageRequest.of(currentPage, pageSize);
    Page<ImmunizationRecord> page;

    do {
      page = immunizationRecordRepository.fetchImminentImmunizationRecord(pageable);
      List<ImmunizationRecord> imminentImmunization = page.getContent();
      imminentImmunization.forEach(
          immunizationRecord -> {
            Child child = immunizationRecord.getChild();
            Parent parent = child.getParent();
            Vaccine vaccine = immunizationRecord.getImmunization().getVaccine();

            SmsRequest smsRequest =
                SmsRequest.builder()
                    .number("+2348106723916")
                    .message(
                        String.format(
                            IMMINENT_IMMUNIZATION_SMS_NOTIFICATION,
                            child.getFirstName(),
                            child.getLastName(),
                            vaccine.getType().getName()))
                    .build();
            smsService.send(smsRequest);
          });
    } while (page.hasNext());
    log.info("Cleaning up completed...");
  }
}
