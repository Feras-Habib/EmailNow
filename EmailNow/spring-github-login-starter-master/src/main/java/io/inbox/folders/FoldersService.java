package io.inbox.folders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoldersService {
    @Autowired
    private UnreadEmailStatusRepository unreadEmailStatusRepository;

    public List<Folder> fetchDefaultFoders(String userId) {
        return Arrays.asList(
                new Folder(userId, "Inbox", "white"),
                new Folder(userId, "Sent Items", "green"),
                new Folder(userId, "Important", "red"));

    }

    public Map<String, Integer> mapCountToLabels(String userId) {
        List<UnreadEmailStatus> status = unreadEmailStatusRepository.findAllById(userId);
        return status.stream()
                .collect(Collectors.toMap(UnreadEmailStatus::getLabel, UnreadEmailStatus::getUnreadCount));
    }

}
