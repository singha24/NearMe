package assasingh.nearmev2.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import assasingh.nearmev2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {

    private ImageView image;
    private TextView count;


    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        image = (ImageView) view.findViewById(R.id.trendingImageFrag);
        count = (TextView) view.findViewById(R.id.trendingCount);

        Bundle bundle = getArguments();
        String message = Integer.toString(bundle.getInt("count"));

        count.setText(message);

        return view;
    }

}
