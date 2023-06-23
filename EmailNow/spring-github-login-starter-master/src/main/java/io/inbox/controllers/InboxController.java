package io.inbox.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.inbox.emailslist.EmailListItem;
import io.inbox.emailslist.EmailListItemRepository;
import io.inbox.folders.Folder;
import io.inbox.folders.FolderRepository;
import io.inbox.folders.FoldersService;
import io.inbox.folders.UnreadEmailStatus;
import io.inbox.folders.UnreadEmailStatusRepository;

@Controller
public class InboxController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FoldersService folderService;
    @Autowired
    private EmailListItemRepository emailListItemRepository;
   

    @GetMapping(value = "/")
    public String homePage(
        @RequestParam(required=false) String folder,
            @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }
        // fetch folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFoders(userId);
        model.addAttribute("defaultFolders", defaultFolders);
        //add unread folder status to model
        model.addAttribute("stats", folderService.mapCountToLabels(userId));
        

        // fetch messages
        if(!StringUtils.hasText(folder)){
            folder="Inbox";
        }
        List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label(userId, folder);

        PrettyTime p = new PrettyTime();


        emailList.stream().forEach(emailItem-> {
            UUID timeUuid= emailItem.getId().getTimeId();
            Date emailDateTime=new Date(Uuids.unixTimestamp(timeUuid));
            emailItem.setAgoTimeString( p.format(emailDateTime));
        });

        model.addAttribute("emailList", emailList);
        model.addAttribute("folderName", folder);
        return "inbox-page";

    }
}
