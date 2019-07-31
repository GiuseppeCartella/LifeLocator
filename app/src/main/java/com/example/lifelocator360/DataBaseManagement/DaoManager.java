package com.example.lifelocator360.DataBaseManagement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoManager {


    //Contatti
    @Insert
    public void addContact(Contact contact);

    @Query("select * from contact")
    public List<Contact> getContacts();

    @Delete
    public void deleteContact(Contact contact);

    @Query("delete from contact")
    public void deleteAllContacts();
    @Update
    public void updateContact(Contact contact);

    //Note
    @Insert
    public void addNote(Note note);

    @Query("select * from note")
    public List<Note> getNote();

    @Delete
    public void deleteNote(Note note);

    @Query("delete from note")
    public void deleteAllNotes();

    @Update
    public void updateNote(Note note);


    @Query ("select contact_id from contact")
    public Integer[] reciveContactsIds();

    @Query ("select note_id from note")
    public Integer[] reciveNotesIds();

}