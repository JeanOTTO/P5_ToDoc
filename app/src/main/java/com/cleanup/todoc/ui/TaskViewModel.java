package com.cleanup.todoc.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.database.entities.Project;
import com.cleanup.todoc.data.database.entities.Task;
import com.cleanup.todoc.data.database.entities.TaskWithProject;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.di.DI;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Project>> allProjects;
    private MutableLiveData<SortTasks> mutableTaskWithProject = new MutableLiveData<>();

    public LiveData<List<TaskWithProject>> taskWithProjectObs;

    public TaskViewModel(Application application) {
        super(application);

        DI.initialize(application.getApplicationContext());
        taskRepository = DI.getTaskRepository();
        projectRepository = DI.getProjectRepository();
        allProjects = projectRepository.getAllProjects();
        allTasks = taskRepository.getAllTasks();
        taskWithProjectObs = taskRepository.getAllTasksWithProjects();
    }

    @NonNull
    public LiveData<List<TaskWithProject>> getAllTasks() {

        return taskWithProjectObs;
    }


    public LiveData<List<Project>> getAllProjects() {
        return allProjects;
    }

    public void insertTask(Task task) {
        executor.execute(() -> {
            taskRepository.insertTask(task);
        });
    }

    public void deleteTask(TaskWithProject taskWithProject) {
        executor.execute(() -> {
            taskRepository.deleteTask(taskWithProject.task);
        });
    }

    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskAZComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return left.task.getName().compareToIgnoreCase(right.task.getName());
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return right.task.getName().compareToIgnoreCase(left.task.getName());
        }
    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return left.task.getCreatedAt().compareTo(right.task.getCreatedAt());
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return right.task.getCreatedAt().compareTo(left.task.getCreatedAt());
        }
    }


}
