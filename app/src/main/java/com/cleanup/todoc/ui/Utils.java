package com.cleanup.todoc.ui;

import com.cleanup.todoc.data.database.entities.Task;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {

    public static List<Task> sortTasks(List<Task> tasksList, SortTasks mSortTasks) {
        //List<TaskWithProject> tasks = taskWithProject.getValue();
        if (tasksList != null) {

            switch (mSortTasks) {
                case ALPHABETICAL:
                    Collections.sort(tasksList, new TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasksList, new TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasksList, new TaskOldComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasksList, new TaskRecentComparator());
                    break;
            }
        }
        return tasksList;
    }

    public static class TaskAZComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.getName().compareToIgnoreCase(right.getName());
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.getName().compareToIgnoreCase(left.getName());
        }
    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.getCreatedAt().compareTo(right.getCreatedAt());
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.getCreatedAt().compareTo(left.getCreatedAt());
        }
    }
}
