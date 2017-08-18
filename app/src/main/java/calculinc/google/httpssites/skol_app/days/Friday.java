package calculinc.google.httpssites.skol_app.days;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import calculinc.google.httpssites.skol_app.Matsedel.FirstFragment;
import calculinc.google.httpssites.skol_app.R;

/**
 * Created by Ruboss on 8/15/2017.
 */

public class Friday extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friday_frag, container, false);


        return v;
    }

    public static FirstFragment newInstance(String text) {

        FirstFragment f = new FirstFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
