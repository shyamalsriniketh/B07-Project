package com.example.smartair;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

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

    public void parentNav(Context context, String itemTitle) {
        switch(itemTitle) {
            case "My Children":
                myChildren(context);
                break;
            case "Sign out":
                signOut(context);
                break;
            case "Dashboard":
                parentDashboard(context);
                break;
        }
    }
    public void childNav(Context context, String itemTitle, Intent intent) {
        switch (itemTitle) {
            case "Dashboard":
                childDashboard(context, intent);
                break;
            case "Input":
                input(context, intent);
                break;
            case "Having Trouble Breathing?":
                triage(context, intent);
                break;
            case "Motivation":
                motivation(context, intent);
                break;
            case "Sign out":
                signOut(context);
                break;
        }
    }
}
