package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.epotik.R;

import model.Data;

import java.util.List;

public class Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items;

    public Adapter(Activity activity, List<Data> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_obat, null);

        TextView id = convertView.findViewById(R.id.id);
        TextView name = convertView.findViewById(R.id.nama);
        TextView deskripsi = convertView.findViewById(R.id.deskripsi);
        ImageView imagePath = convertView.findViewById(R.id.imagePath);

        Data data = items.get(position);

        id.setText(data.getId());
        name.setText(data.getNama());
        deskripsi.setText(data.getDeskripsi());

        if (imagePath != null && data.getImagePath() != null) {
            Glide.with(activity)
                    .load(data.getImagePath())
                    .into(imagePath);
        }
        return convertView;
    }
}
