package com.cleanup.todoc.di;

import android.content.Context;

import com.cleanup.todoc.data.database.AppDatabase;
import com.cleanup.todoc.data.database.dao.ProjectDao;
import com.cleanup.todoc.data.database.dao.TaskDao;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;


public class DI {

    private static Context context;
    private static AppDatabase appDatabase;
    private static ProjectDao projectDao;
    private static TaskDao taskDao;
    private static ProjectRepository projectRepository;
    private static TaskRepository taskRepository;

    public static void initialize(Context appContext) {
        context = appContext;
    }

    public static AppDatabase getAppDatabase() {
        if (appDatabase == null) {
            appDatabase = AppDatabase.getInstance(context);
        }
        return appDatabase;
    }
    // a supprimer



    public static ProjectDao getProjectDao() {
        if (projectDao == null) {
            projectDao = getAppDatabase().projectDao();
        }
        return projectDao;
    }

    public static TaskDao getTaskDao() {
        if (taskDao == null) {
            taskDao = getAppDatabase().taskDao();
        }
        return taskDao;
    }

    public static ProjectRepository getProjectRepository() {
        if (projectRepository == null) {
            projectRepository = new ProjectRepository(getProjectDao());
        }
        return projectRepository;
    }

    public static TaskRepository getTaskRepository() {
        if (taskRepository == null) {
            taskRepository = new TaskRepository(getTaskDao());
        }
        return taskRepository;
    }
}
