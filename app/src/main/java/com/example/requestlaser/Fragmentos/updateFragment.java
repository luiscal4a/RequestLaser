package com.example.requestlaser.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.requestlaser.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link updateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link updateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class updateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public updateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment updateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static updateFragment newInstance(String param1, String param2) {
        updateFragment fragment = new updateFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final TextView myText = (TextView) view.findViewById(R.id.textUpdate);

        Button button = (Button) view.findViewById(R.id.button2);
        final EditText update_id = (EditText) view.findViewById(R.id.update_id);
        final EditText nameField = (EditText) view.findViewById(R.id.name);
        final EditText idField = (EditText) view.findViewById(R.id.idPlayer);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url ="http://192.168.1.104:3000/player/"+update_id.getText();

                StringRequest sr = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myText.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        //myText.setText(error.toString()+" "+networkResponse.statusCode);
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            String message = data.getString("message");
                            myText.setText(message);
                        } catch (JSONException e) {
                            myText.setText("flag1");
                        } catch (UnsupportedEncodingException errorr) {
                            myText.setText("flag2");
                        }
                    }
                }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        if(!nameField.getText().toString().matches(""))
                            params.put("userName",nameField.getText().toString());
                        if(!idField.getText().toString().matches(""))
                            params.put("idPlayer",idField.getText().toString());

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded");
                        return params;
                    }
                };

                queue.add(sr);
            }
        });
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
