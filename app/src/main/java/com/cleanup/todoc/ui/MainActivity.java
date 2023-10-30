package com.cleanup.todoc.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cleanup.todoc.R;
import com.cleanup.todoc.data.database.entities.Project;
import com.cleanup.todoc.data.database.entities.Task;
import com.cleanup.todoc.data.database.entities.TaskWithProject;
import com.cleanup.todoc.databinding.ActivityMainBinding;
import com.cleanup.todoc.databinding.DialogAddTaskBinding;
import com.cleanup.todoc.di.DI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TasksAdapter taskAdapter;
    private TaskViewModel taskViewModel;
    private android.app.AlertDialog dialog;
    private DialogAddTaskBinding binding;
    private ActivityMainBinding viewBinding;
    private List<TaskWithProject> mTasks;
    @NonNull
    private SortTasks mSortTasks = SortTasks.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskAdapter = new TasksAdapter(new TasksAdapter.OnDeleteTaskClickListener() {
            @Override
            public void onDeleteTaskClick(TaskWithProject task) {
                taskViewModel.deleteTask(task);
            }
        });
        viewBinding.listTasks.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.listTasks.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        viewBinding.listTasks.setAdapter(taskAdapter);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            mTasks=tasks;
            if (tasks.size() == 0) {
                viewBinding.lblNoTask.setVisibility(View.VISIBLE);
                viewBinding.listTasks.setVisibility(View.GONE);
            } else {
                viewBinding.lblNoTask.setVisibility(View.GONE);
                viewBinding.listTasks.setVisibility(View.VISIBLE);
                taskAdapter.setTaskList(mTasks);}
        });

        viewBinding.fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });

    }




    private void showAddTaskDialog() {
        dialogAddTask();
    }

    public void dialogAddTask() {
        LayoutInflater inflater = LayoutInflater.from(this);
        binding = DialogAddTaskBinding.inflate(inflater);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(binding.getRoot());
        builder.setTitle(R.string.add_task);
        builder.setPositiveButton(R.string.add, null);
        dialog = builder.create();
        populateProjectSpinner();
        show();
    }

    private void onPositiveButtonClick(){
        String taskName = binding.txtTaskName.getText().toString().trim();
        int projectId = ((Project) binding.projectSpinner.getSelectedItem()).getId();

        if (!taskName.isEmpty()) {
            Task task = new Task(projectId,taskName,new Date());
            taskViewModel.insertTask(task);
            dismiss();
        } else {
            binding.txtTaskName.setError("Veuillez saisir un nom de tâche valide");
        }
    }
    private void populateProjectSpinner() {
        taskViewModel.getAllProjects().observe((LifecycleOwner) this, projects -> {

            ProjectNameAdapter projectNameAdapter = new ProjectNameAdapter(this, projects);

            binding.projectSpinner.setAdapter(projectNameAdapter);
        });
    }

    public void show() {
        dialog.show();
        Button button = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(view -> onPositiveButtonClick());
    }

    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            mSortTasks = SortTasks.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            mSortTasks = SortTasks.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            mSortTasks = SortTasks.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            mSortTasks = SortTasks.RECENT_FIRST;
        }

        updateTasks(mTasks);

        return super.onOptionsItemSelected(item);
    }



    private void updateTasks(List<TaskWithProject> tasks) {

        if (tasks.size() == 0) {
            viewBinding.lblNoTask.setVisibility(View.VISIBLE);
            viewBinding.listTasks.setVisibility(View.GONE);
        } else {
            viewBinding.lblNoTask.setVisibility(View.GONE);
            viewBinding.listTasks.setVisibility(View.VISIBLE);



            taskAdapter.setTaskList(taskViewModel.sortTasks(mTasks, mSortTasks));

        }
    }

}

