package com.cleanup.todoc.data.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.database.dao.TaskDao;
import com.cleanup.todoc.data.database.entities.Task;
import com.cleanup.todoc.data.database.entities.TaskWithProject;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;

    public TaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public long insertTask(Task task) {
        return taskDao.insertTask(task);
    }

    public void updateTask(Task task) {
        taskDao.updateTask(task);
    }

    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }

    public LiveData<List<Task>> getTasksForProject(int projectId) {
        return taskDao.getTasksForProject(projectId);
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    public LiveData<List<TaskWithProject>> getAllTasksWithProjects() {
        return taskDao.getTasksWithProjects();
    }
}
