package assasingh.nearmev2.Adaptors;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import assasingh.nearmev2.R;

/**
 * Created by Assa Chana on 23/11/2016.
 */

public class ListViewAdapter extends ArrayAdapter<String> {

private final Integer[] menuImage;

    private final String[] values;

    public ListViewAdapter(Context context, String[] values, Integer[] menuImage){
        super(context, R.layout.row_layout, values);
        this.menuImage = menuImage;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View theView = inflater.inflate(R.layout.row_layout, null, true);

        String menu = getItem(position);

        TextView textView = (TextView) theView.findViewById(R.id.textView3);

        textView.setText(menu);

        ImageView imageView = (ImageView)theView.findViewById(R.id.imageView1);

        imageView.setImageResource(menuImage[position]);

        return theView;

    }
}
