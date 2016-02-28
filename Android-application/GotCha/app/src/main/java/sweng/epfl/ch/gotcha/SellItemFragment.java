package sweng.epfl.ch.gotcha;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link SellItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellItemFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final int PICK_PICTURE_REQUEST = 1;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    private EditText title;
    private EditText price;
    private EditText description;
    private EditText location;
    NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());
    private String categorySelected;
    private String pictureLoaded;
    private View rootView = null ;
    private String cookie;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SellItemFragment newInstance(int sectionNumber) {
        SellItemFragment fragment = new SellItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContextOfApplication());
        cookie = prefs.getString("Cookie", "");

        if (cookie.isEmpty()){
            new AlertDialog.Builder(this.getContext())
                    .setTitle(getResources().getString(R.string.loginAlert))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .show();
        }


        this.context = this.getContext();
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final Button nextButton = (Button) rootView.findViewById(R.id.button_next);
        final Button imageButton = (Button) rootView.findViewById(R.id.image_button);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.category_spinner);

        final SpinnerLine category = new SpinnerLine(R.drawable.category_icon, "Categories");
        SpinnerLine books = new SpinnerLine(R.drawable.book_icon2, "Books");
        SpinnerLine clothes = new SpinnerLine(R.drawable.clothes, "Clothes");
        SpinnerLine household = new SpinnerLine(R.drawable.home_icon, "Household");
        SpinnerLine musical = new SpinnerLine(R.drawable.guitar_icon, "Electronics");
        SpinnerLine sport = new SpinnerLine(R.drawable.sports_icon, "Sports");
        SpinnerLine other = new SpinnerLine(R.drawable.other_icon, "Others");
        final List<SpinnerLine> list_spinner = new ArrayList<>();
        list_spinner.add(category);
        list_spinner.add(books);
        list_spinner.add(clothes);
        list_spinner.add(household);
        list_spinner.add(musical);
        list_spinner.add(sport);
        list_spinner.add(other);
        SpinnerAdapter adapter = new SpinnerAdapter(context,
                R.layout.category_row, list_spinner);
        spinner.setAdapter(adapter);

        // Add listener to spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                categorySelected = list_spinner.get(position).getName();
                if (categorySelected.equals("Sports")) {
                    categorySelected = "SPORT";
                } else if (categorySelected.equals("Books")) {
                    categorySelected = "BOOK";
                } else if (categorySelected.equals("Clothes")) {
                    categorySelected = "CLOTHING";
                } else if (categorySelected.equals("Electronics")) {
                    categorySelected = "ELECTRONIC";
                } else if (categorySelected.equals("Household")) {
                    categorySelected = "HOUSE";
                } else if (categorySelected.equals("Others")) {
                    categorySelected = "OTHER";
                } else {
                    categorySelected = "FALSE";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nothing happens
            }

        });


        title = (EditText) rootView.findViewById(R.id.title_item_et);
        price = (EditText) rootView.findViewById(R.id.price_item_et);
        description = (EditText) rootView.findViewById(R.id.description_item_et);
        location = (EditText) rootView.findViewById(R.id.specific_location_et);

        // Alert user when the false category was chosen
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Alert user when the false category was chosen
                String title_name = String.valueOf(title.getText());
                String item_price = String.valueOf(price.getText());
                String item_description = String.valueOf(description.getText());

                // Build the URI for the item
                final String NAME = "name";
                final String DESCRIPTION = "description";
                final String PRICE = "price";
                final String USERID = "userId";
                final String CATEGORY = "category";
                final String IMAGE = "image";
                Uri builtUri = Uri.parse("").buildUpon().appendQueryParameter(NAME, title_name).appendQueryParameter(DESCRIPTION, item_description).appendQueryParameter(PRICE, item_price).appendQueryParameter(USERID, String.valueOf(1)).appendQueryParameter(CATEGORY, categorySelected).appendQueryParameter(IMAGE, pictureLoaded).build();
                // Alert if no category chosen
                if (categorySelected.equals("FALSE")) {
                    new AlertDialog.Builder(context)
                            .setTitle(getResources().getString(R.string.category_alert))
                            .setMessage(getResources().getString(R.string.category_message))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .show();
                } else if (item_price.isEmpty() || title_name.isEmpty() || item_description.isEmpty())
                    Alert(context, title_name, item_price, item_description);
                else {
                    new PostItemTask().execute(builtUri.toString().substring(1));
                }
            }
        });
        addListener(imageButton);

        //disable input fields if user is not signed in
        if (cookie.isEmpty()){
            nextButton.setEnabled(false);
            imageButton.setEnabled(false);
            spinner.setEnabled(false);

            title.setEnabled(false);
            description.setEnabled(false);
            price.setEnabled(false);
            location.setEnabled(false);

        }
        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PICTURE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String returnedResult = data.getData().toString();
                InputStream inputStream = null;//You can get an inputStream using any IO API
                try {
                    inputStream = new FileInputStream(returnedResult);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                try {
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap immagex = BitmapFactory.decodeFile(returnedResult);
                ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView1);
                imageView.setImageBitmap(immagex);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                pictureLoaded = Base64.encodeToString(b, Base64.DEFAULT);

            }
        }

    }


    // Add Listioner to button
    public void addListener(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage(view);
            }
        });
    }

    // Get Image from Gallery
    public void getImage(View view) {
        Intent intent = new Intent(this.getContext(), GetFromGalleryActivity.class);
        startActivityForResult(intent, PICK_PICTURE_REQUEST);
    }

    // Alert user when they didn't fill in name and price field
    public void Alert(Context context, String title_name, String item_price, String description) {
        String message = "";
        if (title_name.isEmpty() && !item_price.isEmpty() && !description.isEmpty()) {
            message = getResources().getString(R.string.miss_name);
        } else if (!title_name.isEmpty() && item_price.isEmpty() && !description.isEmpty()) {
            message = getResources().getString(R.string.miss_price);
        } else if (!title_name.isEmpty() && !item_price.isEmpty() && description.isEmpty()) {
            message = getResources().getString(R.string.miss_description);
        } else {
            message = getResources().getString(R.string.miss_name_price);
        }
        new AlertDialog.Builder(context)
                .setTitle(message)
                .setMessage(getResources().getString(R.string.alert_fail))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private class PostItemTask extends AsyncTask<String, Void, Exception> {
        private final ProgressDialog dialog = new ProgressDialog(context);

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Posting item to server");
            dialog.show();
        }

        @Override
        protected Exception doInBackground(String... urls) {
            try {
                networkGotchaClient.postItem(urls[0]);
                return null;
            } catch (IOException e) {
                Log.d("Sell item ", "IOException");
                return e;
            } catch (ClientException e) {
                Log.d("Sell item ", "ClientException");
               return e;
            }

        }

        protected void onPostExecute(Exception exception) {
            dialog.dismiss();
            if(exception == null) {
                new AlertDialog.Builder(context)
                        .setMessage(getResources().getString(R.string.alert_success))
                        .setTitle("Success")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //go to home page
                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.container, CategoryFragment.newInstance(1))
                                        .commit();
                            }
                        })
                        .setIcon(R.drawable.happyface_icon)
                        .show();
            }else  {
                new AlertDialog.Builder(context)
                        .setTitle(getResources().getString(R.string.network_failure_title))
                                .setMessage(getResources().getString(R.string.alert_failure_network))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
            }


        }

    }
}
