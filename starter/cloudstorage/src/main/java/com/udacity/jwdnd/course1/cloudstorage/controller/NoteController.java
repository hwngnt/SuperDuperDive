package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/home/notes")
public class NoteController {
    private final UserMapper userMapper;
    private final NoteService noteService;

    public NoteController(UserMapper userMapper, NoteService noteService) {
        this.userMapper = userMapper;
        this.noteService = noteService;
    }

    @PostMapping
    public String createNoteAndUpdateNote(Authentication authentication,
                                          NoteForm newNote,
                                          Model model){
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(loggedInUserName);
        Integer userId = user.getUserid();
        Note note = new Note(newNote.getNoteid(), newNote.getNotetitle(), newNote.getNotedescription(), userId);
        if (newNote.getNoteid() != null) {
            noteService.updateNote(note);
        } else {
            noteService.createNote(note, user);
        }
        model.addAttribute("result", "success");
        return "result";
    }

    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(Authentication authentication, @PathVariable Integer noteId, @ModelAttribute("newNote") NoteForm newNote,
                             Model model) {
        noteService.deleteNote(noteId);
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(loggedInUserName);
        model.addAttribute("notes", noteService.getListNotes(user));
        model.addAttribute("result", "success");
        return "result";
    }
}
