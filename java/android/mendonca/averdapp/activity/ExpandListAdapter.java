package android.mendonca.averdapp.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.fragment.HomeFragment;
import android.mendonca.averdapp.model.Child;
import android.mendonca.averdapp.model.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by johnson on 10-05-2018.
 */

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Group> listDataHeader; //Header titles
   // private HashMap<String, ArrayList<String>> listDataChild;


    public ExpandListAdapter(Context context, ArrayList<Group> listDataHeader) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        //this.listDataChild = listDataChild;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Child> chList = listDataHeader.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // final String childText = (String) getChild(groupPosition, childPosition);
        Child child=(Child) getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item,null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lbllistitem);

        txtListChild.setText(child.getName().toString());
        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> chList = listDataHeader.get(groupPosition).getItems();
        return chList.size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }



    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //String headtitle= (String) getGroup(groupPosition);
        Group group=(Group)getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group,null);
        }

        TextView lbllistheader = (TextView) convertView.findViewById(R.id.lbllistheader);
        lbllistheader.setTypeface(null, Typeface.BOLD);
        lbllistheader.setText(group.getName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
