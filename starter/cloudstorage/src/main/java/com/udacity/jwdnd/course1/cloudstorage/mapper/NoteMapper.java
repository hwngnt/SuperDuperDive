package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    List<Note> getNotes(int userid);

    @Insert("INSERT INTO NOTES(notetitle, notedescription, userid)" +
            "VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int createNote(Note note);

    @Update("UPDATE NOTES " +
            "SET notetitle = #{notetitle}, " +
            "notedescription = #{notedescription}" +
            "WHERE noteid = #{noteid}")
    int updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    void deleteNote(Integer noteid);

}
