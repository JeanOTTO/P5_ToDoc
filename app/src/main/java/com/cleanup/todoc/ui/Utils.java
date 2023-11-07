package com.cleanup.todoc.ui;

import com.cleanup.todoc.data.database.entities.TaskWithProject;

import java.util.Collections;
import java.util.List;

public class Utils {

    public static List<TaskWithProject> sortTasks(List<TaskWithProject> tasksList, SortTasks mSortTasks) {
        //List<TaskWithProject> tasks = taskWithProject.getValue();
        if (tasksList != null) {

            switch (mSortTasks) {
                case ALPHABETICAL:
                    Collections.sort(tasksList, new TaskViewModel.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasksList, new TaskViewModel.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasksList, new TaskViewModel.TaskOldComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasksList, new TaskViewModel.TaskRecentComparator());
                    break;
            }
        }
        return tasksList;
    }
}
