package com.a1573595.parkingdemo.parkList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a1573595.parkingdemo.R;
import com.a1573595.parkingdemo.databinding.AdapterParkListBinding;
import com.a1573595.parkingdemo.model.data.Park;

public class ParkListAdapter extends RecyclerView.Adapter<ParkListAdapter.ViewHolder> {
    private ParkListPresenter presenter;

    static class ViewHolder extends RecyclerView.ViewHolder {
        AdapterParkListBinding binding;

        ViewHolder(AdapterParkListBinding binding, ParkListPresenter presenter) {
            super(binding.getRoot());

            this.binding = binding;

            binding.root.setOnClickListener(v -> {
                final int position = getAdapterPosition();

                presenter.onItemClick(position);
            });
        }
    }

    public ParkListAdapter(ParkListPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterParkListBinding binding = AdapterParkListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Park park = presenter.getItem(position);

        holder.binding.tvName.setText(park.name);
        holder.binding.tvAddress.setText(park.address);

        holder.binding.tvTotal.setText(
                holder.binding.tvTotal.getContext().getString(R.string.transportation,
                        park.totalbus, park.totalcar, park.totalmotor, park.totalbike));
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }
}
