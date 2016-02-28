package sweng.epfl.ch.gotcha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishListFragment extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView wListView;
    private List<Wish> myWishList = new ArrayList<>();
    WishAdapter wishAdapter;
    private View myWishView;
    NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());

    public WishListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber shows the section selected
     * @return A new instance of fragment MyMessageFragment.
     */

    public static WishListFragment newInstance(int sectionNumber) {
        WishListFragment fragment = new WishListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_my_message, container, false);

        myWishView = inflater.inflate(R.layout.fragment_wish_list, container, false);


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                new getWishesTask().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return myWishView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void addWish(Wish wish) {
        myWishList.add(wish);
    }
    private class getWishesTask extends AsyncTask<Void, Void, List<Wish>> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            this.pDialog  = new ProgressDialog(getContext());
            this.pDialog.setMessage("Please wait..");
            this.pDialog.setIndeterminate(true);
            this.pDialog.setCancelable(false);
            this.pDialog.show();
        }

        @Override
        protected List<Wish> doInBackground(Void... voids) {
            // params comes from the execute() call: params[0] is the url.
            List<Wish> wishList = null;

            try {
                wishList = networkGotchaClient.getWishList();
                return wishList;
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(List<Wish> WishList)
        {

            super.onPostExecute(WishList);
            this.pDialog.dismiss();
            if(WishList!=null) {
                for (Wish element : WishList) {
                    Wish myMessage = new Wish(element.getWishId(), element.getKeyword());
                    addWish(myMessage);
                }
                wishAdapter = new WishAdapter(getActivity(), R.layout.wish_row, myWishList);
                wListView = (ListView) myWishView.findViewById(R.id.wListView);
                Button addWishB=(Button)myWishView.findViewById(R.id.addWishButton);
                final EditText newWish=(EditText) myWishView.findViewById(R.id.newWish);
                wListView.setAdapter(wishAdapter);
                addWishB.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            networkGotchaClient.postWish(newWish.getText().toString());
                            Fragment frg = null;
                            android.support.v4.app.Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            myWishList.clear();
                            ft.detach(currentFragment);
                            ft.attach(currentFragment);
                            ft.commit();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error. Wish was not added", Toast.LENGTH_LONG);
                        }
                    }
                });
            }else{
                Toast.makeText(getContext(),"You have to log in to see your Wish List",Toast.LENGTH_LONG);
            }
        }
    }

}

