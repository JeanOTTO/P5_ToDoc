package com.cleanup.todoc.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.cleanup.todoc.data.database.entities.Project;
import com.cleanup.todoc.data.database.entities.Task;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.di.DI;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Project>> allProjects;

    public TaskViewModel(Application application) {
        super(application);

        DI.initialize(application.getApplicationContext());
        taskRepository = DI.getTaskRepository();
        projectRepository = DI.getProjectRepository();
        allProjects = projectRepository.getAllProjects();
        allTasks = taskRepository.getAllTasks();
    }

    @NonNull
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
    @NonNull
    public LiveData<List<Project>> getAllProjects() {
        return allProjects;
    }

    public void insertTask(Task task) {
        executor.execute(() -> {
            taskRepository.insertTask(task);
        });
    }

    public void deleteTask(Task task) {
        executor.execute(() -> {
            taskRepository.deleteTask(task);
        });
    }

}
