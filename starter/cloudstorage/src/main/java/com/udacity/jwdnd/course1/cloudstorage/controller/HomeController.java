package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final NoteService noteService;
    private final UserMapper userMapper;
    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final FileService fileService;

    public HomeController(NoteService noteService, UserMapper userMapper, UserService userService, CredentialService credentialService, EncryptionService encryptionService, FileService fileService) {
        this.noteService = noteService;
        this.userMapper = userMapper;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    @GetMapping
    public String getHome(Authentication authentication, Model model,
                          @ModelAttribute("newFile") FileForm newFile,
                          @ModelAttribute("newNote") NoteForm newNote,
                          @ModelAttribute("newCredential")CredentialForm newCredential){
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(loggedInUserName);
        model.addAttribute("files", fileService.getFiles(user));
        model.addAttribute("notes", noteService.getListNotes(user));
        model.addAttribute("credentials", credentialService.getCredentials(user));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping
    public String addFile(Authentication authentication,
                          @RequestParam("fileUpload") MultipartFile fileUpload,
                          Model model) throws IOException {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(loggedInUserName);
        List<String> fileNames = fileService.getFiles(user);
        String fileName = fileUpload.getOriginalFilename();
        boolean isDuplicate = false;
        for (String name : fileNames) {
            if (name.equals(fileName)) {
                isDuplicate = true;
                break;
            }
        }
        if (isDuplicate) {
            model.addAttribute("result", "error");
            model.addAttribute("message", "Duplicated file!");
        } else {
            fileService.addFile(fileUpload, user);
            model.addAttribute("result", "success");
        }
//        model.addAttribute("files", fileService.getFiles(user));
        return "result";
    }

    @GetMapping(
            value = "/file/{filename}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    byte[] getFile(@PathVariable String filename) {
        return fileService.getFile(filename).getFiledata();
    }

    @GetMapping("/delete-file/{filename}")
    public String deleteFile(Authentication authentication,
                             @PathVariable String filename,
                             @ModelAttribute("newFile") FileForm newFile,
                             Model model) {
        fileService.deleteFile(filename);
        model.addAttribute("result", "success");
        return "result";
    }

    @PostMapping("/logout")
    public String logout(){
        return "login";
    }
}
