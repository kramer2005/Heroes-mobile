package br.ufpr.heroes.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.List;

import br.ufpr.heroes.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        if (position == 0) {
            tv.setText("Nenhuma");
        }
        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Spinner s = (Spinner) parent;
        if(s.getSelectedItemPosition() == 0) {
            parent.setBackground(AppCompatResources.getDrawable(getContext().getApplicationContext(), R.drawable.spinner_background_unselected));
        } else {
            parent.setBackground(AppCompatResources.getDrawable(getContext().getApplicationContext(), R.drawable.spinner_background));
        }
        return super.getView(position, convertView, parent);
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}
