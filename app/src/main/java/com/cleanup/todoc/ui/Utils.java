package com.cleanup.todoc.ui;

import com.cleanup.todoc.data.database.entities.TaskWithProject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {

    public static List<TaskWithProject> sortTasks(List<TaskWithProject> tasksList, SortTasks mSortTasks) {
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
