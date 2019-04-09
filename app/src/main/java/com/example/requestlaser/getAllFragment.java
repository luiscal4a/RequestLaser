package com.example.requestlaser;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link getAllFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link getAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class getAllFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView myText;

    private OnFragmentInteractionListener mListener;

    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public getAllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment getAllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static getAllFragment newInstance(String param1, String param2) {
        getAllFragment fragment = new getAllFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://192.168.1.104:3000/player";


        final ArrayList<String> playerNames = new ArrayList<>();
        final ArrayList<String> playerIds = new ArrayList<>();
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //myText.setText("Response is: "+ response);

                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray message = data.getJSONArray("players");
                            for(int i=0;i<message.length();i++){
                                playerNames.add(message.getJSONObject(i).getString("userName"));
                                playerIds.add(message.getJSONObject(i).getString("_id"));
                            }
                            System.out.println(message);
                        } catch (JSONException e) {
                            System.out.println("flag1");
                        }

                        setupAdapter(playerNames, playerIds);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error is: "+ error);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void setupAdapter(ArrayList<String> data, ArrayList<String> idData){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), data, idData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position, 1) + " on row number " + position, Toast.LENGTH_SHORT).show();
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", adapter.getItem(position, 0));
        clipboard.setPrimaryClip(clip);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_all, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /*myText = (TextView) getView().findViewById(R.id.textGet);
        myText.setTextIsSelectable(true);*/
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");
        animalNames.add("Horse1");
        animalNames.add("Cow1");
        animalNames.add("Camel1");
        animalNames.add("Sheep1");
        animalNames.add("Goat1");
        animalNames.add("Horse2");
        animalNames.add("Cow2");
        animalNames.add("Camel2");
        animalNames.add("Sheep2");
        animalNames.add("Goat2");
        animalNames.add("Horse3");
        animalNames.add("Cow3");
        animalNames.add("Camel3");
        animalNames.add("Sheep3");
        animalNames.add("Goat3");
        animalNames.add("Horse4");
        animalNames.add("Cow4");
        animalNames.add("Camel4");
        animalNames.add("Sheep4");
        animalNames.add("Goat4");

        // set up the RecyclerView
        recyclerView = getView().findViewById(R.id.rvAnimals);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
