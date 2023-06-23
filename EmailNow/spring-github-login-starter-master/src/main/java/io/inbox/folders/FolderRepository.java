package io.inbox.folders;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;



public interface FolderRepository extends CassandraRepository<Folder, String> {

    List<Folder> findAllById(String id, Pageable pageRequest);
    List<Folder> findAllById(String id);

}

