package com.cleanup.todoc.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.data.database.dao.ProjectDao;
import com.cleanup.todoc.data.database.dao.TaskDao;
import com.cleanup.todoc.data.database.entities.Project;
import com.cleanup.todoc.data.database.entities.Task;

import java.util.Date;

@Database(entities = {Project.class, Task.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();

    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            insertDefaultTasks();
        }
    };


    private static void insertDefaultTasks() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                AppDatabase database = instance;

                long projectIdA = database.projectDao().insertProject(new Project("Projet Tartampion", 0xFFEADAD1));
                long projectIdB = database.projectDao().insertProject(new Project("Projet Lucidia", 0xFFB4CDBA));
                long projectIdC = database.projectDao().insertProject(new Project("Projet Circus", 0xFFA3CED2));

                database.taskDao().insertTask(new Task(projectIdA, "Tâche 1 pour Projet A", new Date()));
                database.taskDao().insertTask(new Task(projectIdB, "Tâche 1 pour Projet B", new Date()));
                database.taskDao().insertTask(new Task(projectIdC, "Tâche 1 pour Projet C", new Date()));
                database.taskDao().insertTask(new Task(projectIdB, "Tâche 2 pour Projet B", new Date()));


            }
        });
    }
}
