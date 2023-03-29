package com.sociallaboursupply.sls_wellbeing_app;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.sociallaboursupply.sls_wellbeing_app.Adapter.ResourceAdapter;
import com.sociallaboursupply.sls_wellbeing_app.Model.ResourceModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.SubActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MentalHealthResourcesActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_directory);
        setTitle(R.string.mental_health_resource_page_heading);

        String db = "Replace String for DatabaseHandler";
//        db.openDatabase();

        // create list to hold resource objects - to implement: populate list from database
        List<ResourceModel> resourceList;

        // Set RecyclerView for resources to be a linearlayout
        RecyclerView resourcesRecyclerView = findViewById(R.id.recyclerViewResources);
        resourcesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and bind adapter to recyclerview
        ResourceAdapter resourceAdapter = new ResourceAdapter(db, MentalHealthResourcesActivity.this);
        resourcesRecyclerView.setAdapter(resourceAdapter);

        // Create resources - method call will be replaced by database
        resourceList = createResources();

        // Pass resources onto the adapter to be displayed
        resourceAdapter.setResources(resourceList);
    }

    // Hardcoded resource material - Method will be replaced by database
    private ArrayList<ResourceModel> createResources(){
        ArrayList<ResourceModel> resourceList = new ArrayList<>();

        ResourceModel resource1 = new ResourceModel();
        resource1.setTitle("Mental health");
        resource1.setDescription("Depression Helpline – <a href='tel:0800111757'> 0800 111 757 </a><br><br>" +
                        "Over 26 - Lifeline – <a href='tel:0800543354'> 0800 543 354 </a><br><br>" +
                        "Under 25 – Youthline - <a href='tel:0800376633'> 0800 376 633 </a>, free text 234<br><br>" +
                "Need to talk? Free call or text <a href='tel:1737'> 1737 </a>");
        resource1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.mental_health_directory));
        resourceList.add(resource1);

        ResourceModel resource2 = new ResourceModel();
        resource2.setTitle("Drugs/Alcohol");
        resource2.setDescription("Alcohol Drug helpline – <a href='tel:0800787797'> 0800 787 797 </a>");
        resource2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.drugs_directory));
        resourceList.add(resource2);

        ResourceModel resource3 = new ResourceModel();
        resource3.setTitle("Gambling");
        resource3.setDescription("Gambling helpline – <a href='tel:0800 654 655'> 0800 654 655 </a>");
        resource3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.gambling_directory));
        resourceList.add(resource3);

        return resourceList;
    }
}