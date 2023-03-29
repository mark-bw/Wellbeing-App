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

public class NeedHelpResourcesActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_directory);
        setTitle(R.string.need_help_resource_page_heading);

        String db = "Replace String for DatabaseHandler";
//        db.openDatabase();

        // create list to hold resource objects - to implement: populate list from database
        List<ResourceModel> resourceList;

        // Set RecyclerView for resources to be a linearlayout
        RecyclerView resourcesRecyclerView = findViewById(R.id.recyclerViewResources);
        resourcesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and bind adapter to recyclerview
        ResourceAdapter resourceAdapter = new ResourceAdapter(db, NeedHelpResourcesActivity.this);
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
        resource1.setTitle("Finance");
        resource1.setDescription("MSD - <a href='https://msd.govt.nz/'> MSD - Ministry of Social Development </a><br><br>" +
                "Bank - <a href='https://www.kiwibank.co.nz/personal-banking/'> Kiwibank – New Zealand's bank </a>");
        resource1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.finance));
        resourceList.add(resource1);

        ResourceModel resource2 = new ResourceModel();
        resource2.setTitle("Health Problem");
        resource2.setDescription("Healthline - <a href='tel:0800611116'> 0800 611 116 </a><br><br>" +
                "Smoking - Quitline <a href='tel:0800778778'> 0800 778 778 </a>");
        resourceList.add(resource2);

        ResourceModel resource3 = new ResourceModel();
        resource3.setTitle("Finding a doctor");
        resource3.setDescription("GP through healthpoint is usually cheaper than A&M<br><br>" +
                "Smoking - Quitline <a href='https://www.healthpoint.co.nz/gps-accident-urgent-medical-care/'> GPs / Accident & Urgent Medical Care • Healthpoint </a>");
        resourceList.add(resource3);

        ResourceModel resource4 = new ResourceModel();
        resource4.setTitle("Need non-health advice");
        resource4.setDescription("Citizen’s advice Bureau - <a href='tel:0800367222'> 0800 367 222 </a><br><br>" +
                "Samaritans - <a href='tel:0800726666/'> 0800 726 666 </a>");
        resourceList.add(resource4);

        return resourceList;
    }
}