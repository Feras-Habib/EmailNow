package io.inbox.emailslist;

import java.util.List;


import org.springframework.data.cassandra.repository.CassandraRepository;

public interface EmailListItemRepository extends CassandraRepository<EmailListItem, EmailsListPrimaryKey>  {
    List<EmailListItem> findAllById_UserIdAndId_Label(String userId, String label); 
}
