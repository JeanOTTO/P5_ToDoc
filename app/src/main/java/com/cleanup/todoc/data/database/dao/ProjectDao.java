package com.cleanup.todoc.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.data.database.entities.Project;

import java.util.List;

@Dao
public interface ProjectDao {
    @Insert
    long insertProject(Project project);

    @Update
    void updateProject(Project project);

    @Delete
    void deleteProject(Project project);

    @Query("SELECT * FROM projects")
    LiveData<List<Project>> getAllProjects();

    @Query("SELECT * FROM projects WHERE name = :projectName")
    LiveData<Project> getProjectByName(String projectName);
}
