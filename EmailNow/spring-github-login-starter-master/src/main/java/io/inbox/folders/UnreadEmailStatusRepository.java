package io.inbox.folders;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface UnreadEmailStatusRepository extends CassandraRepository<UnreadEmailStatus, String> {
    List<UnreadEmailStatus> findAllById(String id);

    @Query("update unread_email_status set unreadcount=unreadcount+1 where user_id=?0 and label=?1")
    public void incrementUreadCount(String userId, String label);

    @Query("update unread_email_status set unreadcount=unreadcount-1 where user_id=?0 and label=?1")
    public void decrementUreadCount(String userId, String label);
}
