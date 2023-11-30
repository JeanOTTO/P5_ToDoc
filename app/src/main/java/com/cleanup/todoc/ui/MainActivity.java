package com.cleanup.todoc.ui;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.cleanup.todoc.R;
import com.cleanup.todoc.data.database.entities.Project;
import com.cleanup.todoc.data.database.entities.Task;
import com.cleanup.todoc.databinding.ActivityMainBinding;
import com.cleanup.todoc.databinding.DialogAddTaskBinding;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TasksAdapter taskAdapter;
    private TaskViewModel taskViewModel;
    private android.app.AlertDialog addListdialog;
    private DialogAddTaskBinding binding;
    private ActivityMainBinding viewBinding;
    private List<Task> mTasks;
    private List<Project> mProject;
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
            public void onDeleteTaskClick(Task task) {
                taskViewModel.deleteTask(task);
            }
        });

        viewBinding.listTasks.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.listTasks.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        viewBinding.listTasks.setAdapter(taskAdapter);

        //LiveData
        taskViewModel.getAllTasks().observe(this, this::updateTasks);
        taskViewModel.getAllProjects().observe(this, this::updateProjects);

        viewBinding.fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });
    }
    private void showAddTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        binding = DialogAddTaskBinding.inflate(inflater);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(binding.getRoot());
        builder.setTitle(R.string.add_task);
        builder.setPositiveButton(R.string.add, null);
        addListdialog = builder.create();
        populateProjectSpinner();
        addListDialogShow();
    }
    private void onPositiveButtonClick(){
        String taskName = binding.txtTaskName.getText().toString().trim();
        int projectId = ((Project) binding.projectSpinner.getSelectedItem()).getId();

        if (!taskName.isEmpty()) {
            Task task = new Task(projectId, taskName, new Date());
            taskViewModel.insertTask(task);
            addListDialogDismiss();
        } else {
            binding.txtTaskName.setError(getString(R.string.empty_task_name));
        }
    }
    private void populateProjectSpinner() {
        ProjectNameAdapter projectNameAdapter = new ProjectNameAdapter(this, mProject);
        binding.projectSpinner.setAdapter(projectNameAdapter);
    }

    public void addListDialogShow() {
        addListdialog.show();
        Button button = addListdialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(view -> onPositiveButtonClick());
    }

    public void addListDialogDismiss() {
        addListdialog.dismiss();
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

    private void updateTasks(List<Task> tasks) {
        mTasks = tasks;
        if (tasks.size() == 0) {
            viewBinding.lblNoTask.setVisibility(View.VISIBLE);
            viewBinding.listTasks.setVisibility(View.GONE);
        } else {
            viewBinding.lblNoTask.setVisibility(View.GONE);
            viewBinding.listTasks.setVisibility(View.VISIBLE);

            taskAdapter.setTaskList(Utils.sortTasks(tasks, mSortTasks));
        }
    }

    private void updateProjects(List<Project> projects) {
        mProject = projects;
        taskAdapter.setProjects(projects);
    }
}

