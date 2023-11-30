package com.cleanup.todoc.ui;

import static android.provider.Settings.System.getString;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.database.entities.Project;
import com.cleanup.todoc.data.database.entities.Task;
import com.cleanup.todoc.databinding.ItemTaskBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    public interface OnDeleteTaskClickListener {
        void onDeleteTaskClick(Task task);
    }
    private List<Task> tasks = new ArrayList<>();
    private static List<Project> projects = new ArrayList<>();
    private OnDeleteTaskClickListener onDeleteTaskClickListener;

    public TasksAdapter() {
        tasks = new ArrayList<>();
        projects = new ArrayList<>();
    }
    public TasksAdapter(OnDeleteTaskClickListener listener) {
        this.onDeleteTaskClickListener = listener;
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.bind(currentTask);
        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = tasks.get(position);
                    if (onDeleteTaskClickListener != null) {
                        onDeleteTaskClickListener.onDeleteTaskClick(task);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTaskList(List<Task> taskList) {
        tasks = taskList;
        notifyDataSetChanged();
    }

    public void setProjects(List<Project> projects) {
        TasksAdapter.projects = projects;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        public TaskViewHolder(ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        private Project getProjectById(int projectId) {
            for (Project project : projects) {
                if (project.getId() == projectId) {
                    return project;
                }
            }
            return null;
        }
        public void bind(Task item) {
            binding.lblTaskName.setText(item.getName());
            binding.imgProject.setColorFilter(
                    Objects.requireNonNull(getProjectById((int) item.getProjectId())).getColor());

            String concatenate = this.itemView.getContext().getString(R.string.project) + " " +
                    Objects.requireNonNull(getProjectById((int) item.getProjectId())).getName();
            binding.lblProjectName.setText(concatenate);
        }

    }
}