package com.cleanup.todoc;

import com.cleanup.todoc.data.database.dao.TaskDao;
import com.cleanup.todoc.data.database.entities.Project;
import com.cleanup.todoc.data.database.entities.Task;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.ui.SortTasks;
import com.cleanup.todoc.ui.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TaskUnitTest {
    @Mock
    TaskDao taskDao;
    TaskRepository repository;
    final ArrayList<Task> tasks = new ArrayList<>();
    Task task1, task2, task3;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        repository = new TaskRepository(taskDao);

        //Create TaskWithProject
        Date currentDate = new Date();
        task1 = new Task(1,  "aaa", new Date(currentDate.getTime()));
        task2 = new Task(2,  "zzz", new Date(currentDate.getTime()+10));
        task3 = new Task(3,  "hhh", new Date(currentDate.getTime()+100));
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
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

    /*@Test
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
        assertSame(tasks.get(0), task1);  //aaa
        assertSame(tasks.get(1), task3);  //hhh
        assertSame(tasks.get(2), task2);  //zzz
    }

    @Test
    public void test_za_comparator() {
        Utils.sortTasks(tasks, SortTasks.ALPHABETICAL_INVERTED);
        assertSame(tasks.get(0), task2);  //zzz
        assertSame(tasks.get(1), task3);  //hhh
        assertSame(tasks.get(2), task1);  //aaa
    }

    @Test
    public void test_recent_comparator() {
        //Collections.sort(tasks, new Utils.TaskRecentComparator());
        Utils.sortTasks(tasks, SortTasks.RECENT_FIRST);
        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_old_comparator() {
        Utils.sortTasks(tasks, SortTasks.OLD_FIRST);
        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }
}