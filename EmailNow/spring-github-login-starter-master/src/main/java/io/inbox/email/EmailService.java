package io.inbox.email;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.inbox.emailslist.EmailListItem;
import io.inbox.emailslist.EmailListItemRepository;
import io.inbox.emailslist.EmailsListPrimaryKey;
import io.inbox.folders.UnreadEmailStatusRepository;

@Service
public class EmailService {

  @Autowired
  private EmailRepository emailRepository;
  @Autowired
  private EmailListItemRepository emailListItemRepository;
  @Autowired
  UnreadEmailStatusRepository unreadEmailStatusRepository;

  public void SendEmail(String from, List<String> to, String subject, String body) {

    Email email = new Email();
    email.setTo(to);
    email.setBody(body);
    email.setFrom(from);
    email.setSubject(subject);
    email.setId(Uuids.timeBased());
    emailRepository.save(email);

    to.forEach(toId -> {
      EmailListItem item = createEmailListItem(to, subject, email, toId, "Inbox");
      emailListItemRepository.save(item);
      unreadEmailStatusRepository.incrementUreadCount(toId, "Inbox");
    });

    EmailListItem sentItemsEntry = createEmailListItem(to, subject, email, from, "Sent Items");
    sentItemsEntry.setUnread(false);
    emailListItemRepository.save(sentItemsEntry);
  }

  private EmailListItem createEmailListItem(List<String> to, String subject, Email email, String itemOwner,
      String folder) {
    EmailsListPrimaryKey key = new EmailsListPrimaryKey();
    key.setUserId(itemOwner);
    key.setTimeId(email.getId());
    key.setLabel(folder);

    EmailListItem item = new EmailListItem();
    item.setId(key);
    item.setSubject(subject);
    item.setUnread(true);

    return item;
  }
}
