package com.cleanup.todoc.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.cleanup.todoc.data.database.entity.Task;
import com.cleanup.todoc.data.database.entity.TaskWithProject;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM tasks")
    void deleteAllTask();

    @Query("SELECT * FROM tasks WHERE projectId = :projectId")
    LiveData<List<Task>> getTasksForProject(int projectId);
    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    @Transaction
    @Query("SELECT * FROM tasks")
    LiveData<List<TaskWithProject>> getTasksWithProjects();
}
