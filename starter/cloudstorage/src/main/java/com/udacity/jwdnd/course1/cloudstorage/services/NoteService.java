package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note, User user) {
        Note newNote = new Note();
        newNote.setNotetitle(note.getNotetitle());
        newNote.setNotedescription(note.getNotedescription());
        newNote.setUserid(user.getUserid());
        return noteMapper.createNote(newNote);
    }

    public int updateNote(Note note){
        return noteMapper.updateNote(note);
    }

    public void deleteNote(int noteid){
        noteMapper.deleteNote(noteid);
    }

    public List<Note> getListNotes(User user) {
        return noteMapper.getNotes(user.getUserid());
    }
}
