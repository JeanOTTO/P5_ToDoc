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

                //Projects
                long project1 = database.projectDao().insertProject(new Project(ProjectsName.PROJET_1.getName(), ProjectsName.PROJET_1.getColor()));
                long project2 = database.projectDao().insertProject(new Project(ProjectsName.PROJET_2.getName(), ProjectsName.PROJET_2.getColor()));
                long project3 = database.projectDao().insertProject(new Project(ProjectsName.PROJET_3.getName(), ProjectsName.PROJET_3.getColor()));

                //Tasks examples
                database.taskDao().insertTask(new Task(project1, "Balayer", new Date()));
                database.taskDao().insertTask(new Task(project2, "Nettoyer", new Date()));
                database.taskDao().insertTask(new Task(project3, "Lustrer", new Date()));
                database.taskDao().insertTask(new Task(project2, "Aspirer", new Date()));
            }
        });
    }
}
