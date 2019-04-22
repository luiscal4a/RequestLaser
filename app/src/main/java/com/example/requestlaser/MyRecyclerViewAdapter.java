package com.example.requestlaser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<String> idData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<String> data, List<String> iddata) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.idData = iddata;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        String id = idData.get(position);
        holder.myTextView.setText(animal);
        holder.idTextView.setText(id);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        TextView idTextView;
        ImageButton btn_delete;
        ImageButton btn_edit;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            idTextView = itemView.findViewById(R.id.idPlayer);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_edit = itemView.findViewById(R.id.btn_edit);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue queue = Volley.newRequestQueue(v.getContext());
                    String url = "http://192.168.1.104:3000/player/"+idTextView.getText();

                    StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    mData.remove(getAdapterPosition());
                                    idData.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(btn_delete.getContext(), "You clicked " + idTextView.getText() + " on row number " , Toast.LENGTH_SHORT).show();
                        }
                    });

                    queue.add(stringRequest);
                    //Toast.makeText(v.getContext(), "You clicked " + idTextView.getText() + " on row number " , Toast.LENGTH_SHORT).show();
                }
            });

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInputDialog(v, idTextView, getAdapterPosition());
                }
            });


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id, int selector) {
        if(selector==0) return idData.get(id);
        else return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    protected void showInputDialog(final View v, final TextView idTextView, final int pos) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());
                        String url ="http://192.168.1.104:3000/player/"+idTextView.getText();

                        StringRequest sr = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(!editText.getText().toString().matches("")) {
                                    mData.remove(pos);
                                    mData.add(pos, editText.getText().toString());
                                }
                                notifyItemChanged(pos);
                                notifyDataSetChanged();
                                Toast.makeText(v.getContext(), "All correct" , Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(v.getContext(), message , Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Toast.makeText(v.getContext(), "Flag 1" , Toast.LENGTH_SHORT).show();
                                } catch (UnsupportedEncodingException errorr) {
                                    Toast.makeText(v.getContext(), "Flag 2" , Toast.LENGTH_SHORT).show();
                                }
                            }
                        }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String, String>();
                                if(!editText.getText().toString().matches("")){
                                    params.put("userName",editText.getText().toString());
                                }
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
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}