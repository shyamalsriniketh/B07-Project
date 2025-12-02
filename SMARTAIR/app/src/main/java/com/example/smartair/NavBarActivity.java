package com.example.smartair;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

public class NavBarActivity {
    public static void parentDashboard(Context context) {
        Intent intent = new Intent(context, ParentDashboardActivity.class);
        context.startActivity(intent);
    }

    public static void signOut(Context context) {
        Intent intent = new Intent(context, SignOut.class);
        context.startActivity(intent);
    }

    public static void myChildren(Context context) {
        Intent intent = new Intent(context, ManageChildrenActivity.class);
        context.startActivity(intent);
    }
    public static void childDashboard(Context context, Intent i) {
        Intent intent = new Intent(context, ChildDashboardActivity.class);
        if (i.hasExtra("PARENT_VIEW")) {
            intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
        }
        context.startActivity(intent);
    }

    public static void input(Context context, Intent i) {
        Intent intent = new Intent(context, Child_Input.class);
        if (i.hasExtra("PARENT_VIEW")) {
            intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
        }
        context.startActivity(intent);
    }

    public static void triage(Context context, Intent i) {
        Intent intent = new Intent(context, triageActivity.class);
        if (i.hasExtra("PARENT_VIEW")) {
            intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
        }
        context.startActivity(intent);
    }

    public static void motivation(Context context, Intent i) {
        Intent intent = new Intent(context, Child_Motivation.class);
        if (i.hasExtra("PARENT_VIEW")) {
            intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
        }
        context.startActivity(intent);
    }

    public void parentNav(Context context, int itemId) {
        switch(itemId) {
            case R.id.my_children:
                myChildren(context);
                break;
            case R.id.sign_out:
                signOut(context);
                break;
            case R.id.dashboard:
                parentDashboard(context);
                break;
        }
    }
    public void childNav(Context context, int itemId, Intent intent) {
        switch (itemId) {
            case R.id.child_dashboard:
                childDashboard(context, intent);
                break;
            case R.id.input:
                input(context, intent);
                break;
            case R.id.triage:
                triage(context, intent);
                break;
            case R.id.motivation:
                motivation(context, intent);
                break;
            case R.id.child_sign_out:
                signOut(context);
                break;
        }
    }
}
