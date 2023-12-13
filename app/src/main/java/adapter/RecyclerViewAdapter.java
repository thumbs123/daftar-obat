package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.epotik.R;
import java.util.List;
import model.Data;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Data> itemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RecyclerViewAdapter(Context context, List<Data> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, nama, deskripsi;
        ImageView gambar; // Tambahkan ImageView untuk menampilkan gambar

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            nama = itemView.findViewById(R.id.nama);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            gambar = itemView.findViewById(R.id.gambar); // Inisialisasi ImageView

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemLongClick(position);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_obat, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data data = itemList.get(position);

        if (data != null) {
            holder.id.setText(data.getId());
            holder.nama.setText(data.getNama());

            // Pemeriksaan null sebelum setText
            if (holder.deskripsi != null) {
                holder.deskripsi.setText(data.getDeskripsi());
            }

            // Pemeriksaan null sebelum menampilkan gambar
            if (holder.gambar != null && data.getImagePath() != null) {
                Glide.with(context)
                        .load(data.getImagePath())
                        .into(holder.gambar);
            }
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
