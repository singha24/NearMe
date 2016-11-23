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

public class FavActionDialogAdapter extends ArrayAdapter<String> {

    private final Integer[] menuImage;
    private Context c;
    private final String[] values;

    public FavActionDialogAdapter(Context context, String[] values, Integer[] menuImage){
        super(context, R.layout.custom_fav_action, values);

        this.menuImage = menuImage;
        this.values = values;
        this.c = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(getContext());

        View theView = inflater.inflate(R.layout.custom_fav_action, null, true);

        String menu = getItem(position);

        TextView textView = (TextView) theView.findViewById(R.id.fav_action_textView);

        textView.setText(menu);

        ImageView imageView = (ImageView)theView.findViewById(R.id.fav_action_imageView);

        imageView.setImageResource(menuImage[position]);

        return theView;

    }
}
