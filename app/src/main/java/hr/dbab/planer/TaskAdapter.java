package hr.dbab.planer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    private CustomOnItemClickListener listener;
    private CustomLongOnItemClickListener longListener;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.tvTitle.setText(currentTask.getTitle());
        holder.tvTime.setText(currentTask.getTime());

    }
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    //metoda za ažuriranje RecyclerView-a
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }
    //metoda za swipe u lijevo
    public void moveItemLeft(int currentPosition, int newPosition) {
        Task currentTask = tasks.get(currentPosition);
        tasks.remove(currentPosition);
        notifyItemRemoved(currentPosition);
        tasks.add(newPosition, currentTask);
        notifyItemInserted(newPosition);
    }
    //metoda za swipe u desno
    public void moveItemRight(int currentPosition, int newPosition) {
        Task currentTask = tasks.get(currentPosition);
        tasks.remove(currentPosition);
        notifyItemRemoved(currentPosition);
        tasks.add(newPosition, currentTask);
        notifyItemInserted(newPosition);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvTime;


        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.customOnItemClick(tasks.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (longListener != null && position != RecyclerView.NO_POSITION) {
                        longListener.customLongOnItemClickListener(tasks.get(position));
                    }
                    return true;
                }
            });
        }
    }

    public interface CustomOnItemClickListener {
        void customOnItemClick(Task task);
    }

    public void setCustomOnItemClickListener(CustomOnItemClickListener listener) {
        this.listener = listener;
    }

    public interface CustomLongOnItemClickListener{
        void customLongOnItemClickListener(Task task);
    }

    public void setCustomLongOnItemClickListener(CustomLongOnItemClickListener longListener){
        this.longListener = longListener;
    }
}
