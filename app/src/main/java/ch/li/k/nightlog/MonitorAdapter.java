package ch.li.k.nightlog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.li.k.nightlog.databinding.MonitorItemBinding;

public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder> {

    public int selectedRow = RecyclerView.NO_POSITION;
    private List<StatsModel> monitorItemsList;// = new ArrayList<>();

    @NonNull
    @Override
    public MonitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MonitorItemBinding binding = MonitorItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MonitorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MonitorViewHolder holder, int position) {
        holder.bind(monitorItemsList.get(position));
        holder.itemView.setSelected(selectedRow == position);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                notifyItemChanged(selectedRow);
//                if (selectedRow == position) {
//                    selectedRow = RecyclerView.NO_POSITION;
//                    notifyItemChanged(selectedRow);
//                    return;
//                }
//                selectedRow = holder.getLayoutPosition();
//                notifyItemChanged(selectedRow);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return monitorItemsList == null ? 100 : monitorItemsList.size();
    }


    public void setMonitorItemsList(List<StatsModel> monitorItemsList) {
        this.monitorItemsList = monitorItemsList;
        notifyDataSetChanged();
    }

    public class MonitorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MonitorItemBinding binding;

        public MonitorViewHolder(MonitorItemBinding binding) {
            super(binding.getRoot());

            binding.getRoot().setOnClickListener(this);
            this.binding = binding;
        }

        public void bind(StatsModel model) {
            binding.setModel(model);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            itemView.toString();

            notifyItemChanged(selectedRow);
            if (selectedRow == getAdapterPosition()) {
                selectedRow = RecyclerView.NO_POSITION;
                notifyItemChanged(selectedRow);
                return;
            }
            selectedRow = getLayoutPosition();
            notifyItemChanged(selectedRow);
        }
    }
}
