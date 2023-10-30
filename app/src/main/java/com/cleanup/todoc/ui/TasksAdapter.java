package com.cleanup.todoc.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cleanup.todoc.data.database.entities.TaskWithProject;
import com.cleanup.todoc.databinding.ItemTaskBinding;
import java.util.ArrayList;
import java.util.List;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    public interface OnDeleteTaskClickListener {
        void onDeleteTaskClick(TaskWithProject task);
    }

    private List<TaskWithProject> tasks = new ArrayList<>();
    private OnDeleteTaskClickListener onDeleteTaskClickListener;

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
        TaskWithProject currentTask = tasks.get(position);
        holder.bind(currentTask);
        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    TaskWithProject task = tasks.get(position);
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

    public void setTaskList(List<TaskWithProject> taskList) {
        tasks = taskList;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        public TaskViewHolder(ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TaskWithProject item) {
            binding.lblTaskName.setText(item.task.getName());
            binding.imgProject.setColorFilter(item.project.getColor());
            binding.lblProjectName.setText(item.project.getName());

        }

    }
}