package com.cleanup.todoc;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;

import com.cleanup.todoc.data.database.AppDatabase;
import com.cleanup.todoc.data.database.dao.TaskDao;
import com.cleanup.todoc.data.database.entities.Project;
import com.cleanup.todoc.data.database.entities.Task;
import com.cleanup.todoc.data.database.entities.TaskWithProject;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.di.DI;
import com.cleanup.todoc.ui.SortTasks;
import com.cleanup.todoc.ui.TaskViewModel;
import com.cleanup.todoc.ui.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;

import android.content.Context;

@RunWith(MockitoJUnitRunner.class)
public class TaskUnitTest {
    @Mock
    TaskDao taskDao;
    TaskRepository repository;
    final ArrayList<TaskWithProject> tasks = new ArrayList<>();
    final TaskWithProject tp1 = new TaskWithProject();
    final TaskWithProject tp2 = new TaskWithProject();
    final TaskWithProject tp3 = new TaskWithProject();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        repository = new TaskRepository(taskDao);

        //Create TaskWithProject
        Date currentDate = new Date();
        final Task task1 = new Task(1,  "aaa", new Date(currentDate.getTime()));
        final Task task2 = new Task(2,  "zzz", new Date(currentDate.getTime()+10));
        final Task task3 = new Task(3,  "hhh", new Date(currentDate.getTime()+100));
        final Project project = new Project("Projet Tartampion", 0xFFEADAD1);
        tp1.task = task1;
        tp2.task = task2;
        tp3.task = task3;
        tp1.project = project;
        tp2.project = project;
        tp3.project = project;
        tasks.add(tp1);
        tasks.add(tp2);
        tasks.add(tp3);
    }
    @Test
    public void insertTask(){
        //TaskRepository taskrepo = new TaskRepository(taskDao);
        Task task1 = new Task(1, "aaa", new Date());
        Mockito.when(taskDao.insertTask(task1)).thenReturn(1L);
        long id = repository.insertTask(task1);
        assertEquals(id, 1L);
    }



    @Test
    public void deleteTask(){
        Task task1 = new Task(1, "aaa", new Date());
        repository.deleteTask(task1);

        // Vérifiez que la méthode delete du DAO a été appelée avec la tâche spécifiée
        verify(taskDao).deleteTask(task1);
    }

/*    @Test
    public void test_projects() {
        final Task task1 = new Task(1, 1, "task 1", new Date().getTime());
        final Task task2 = new Task(2, 2, "task 2", new Date().getTime());
        final Task task3 = new Task(3, 3, "task 3", new Date().getTime());
        final Task task4 = new Task(4, 4, "task 4", new Date().getTime());

        assertEquals("Projet Tartampion", task1.getProject().getName());
        assertEquals("Projet Lucidia", task2.getProject().getName());
        assertEquals("Projet Circus", task3.getProject().getName());
        assertNull(task4.getProject());
    }*/

    @Test
    public void test_az_comparator() {
        Utils.sortTasks(tasks, SortTasks.ALPHABETICAL);
        assertSame(tasks.get(0), tp1);  //aaa
        assertSame(tasks.get(1), tp3);  //hhh
        assertSame(tasks.get(2), tp2);  //zzz
    }

    @Test
    public void test_za_comparator() {
        Utils.sortTasks(tasks, SortTasks.ALPHABETICAL_INVERTED);
        assertSame(tasks.get(0), tp2);  //zzz
        assertSame(tasks.get(1), tp3);  //hhh
        assertSame(tasks.get(2), tp1);  //aaa
    }

    @Test
    public void test_recent_comparator() {
        //Collections.sort(tasks, new Utils.TaskRecentComparator());
        Utils.sortTasks(tasks, SortTasks.RECENT_FIRST);
        assertSame(tasks.get(0), tp3);
        assertSame(tasks.get(1), tp2);
        assertSame(tasks.get(2), tp1);
    }

    @Test
    public void test_old_comparator() {
        Utils.sortTasks(tasks, SortTasks.OLD_FIRST);
        assertSame(tasks.get(0), tp1);
        assertSame(tasks.get(1), tp2);
        assertSame(tasks.get(2), tp3);
    }
}