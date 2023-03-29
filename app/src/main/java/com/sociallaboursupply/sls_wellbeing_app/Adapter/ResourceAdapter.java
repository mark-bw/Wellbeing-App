package com.sociallaboursupply.sls_wellbeing_app.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sociallaboursupply.sls_wellbeing_app.Model.ResourceModel;
import com.sociallaboursupply.sls_wellbeing_app.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {

    private List<ResourceModel> resourceList;
    private final Activity activity;

    // Todo: implement database for resource list - replace String with a Database handler for courses
    private final String db;
//    private final DatabaseHandler db;

    public ResourceAdapter(String db, Activity activity){
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resource_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position){

//        db.openDatabase();

        // Get the resource at current position
        final ResourceModel resource = resourceList.get(position);

        // Set the text and image for resource
        holder.title.setText(resource.getTitle());
        holder.description.setClickable(true);
        holder.description.setMovementMethod(LinkMovementMethod.getInstance());
        holder.description.setText(Html.fromHtml(resource.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        Bitmap image = resource.getImage();
        if(image == null){
            holder.resourceImage.setVisibility(View.GONE);
        } else {
            holder.resourceImage.setImageBitmap(image);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setResources(List<ResourceModel> resourceList){
        this.resourceList = resourceList;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return resourceList.size();
    }

    public Context getContext(){
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView resourceImage;
        TextView title;
        TextView description;

        ViewHolder(View view){
            super(view);
            resourceImage = view.findViewById(R.id.imageViewResourceImage);
            title = view.findViewById(R.id.textViewResourceTitle);
            description = view.findViewById(R.id.textViewResourceInformation);
        }
    }
}