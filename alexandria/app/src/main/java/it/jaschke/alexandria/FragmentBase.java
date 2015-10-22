package it.jaschke.alexandria;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FragmentBase extends Fragment {

    protected FragmentBaseListener mFragmentBaseListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public interface FragmentBaseListener {
        void setToolBar(CharSequence title);
    }
}
