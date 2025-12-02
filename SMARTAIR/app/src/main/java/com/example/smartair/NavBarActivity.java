package com.example.smartair;

import android.content.Context;
import android.content.Intent;

public class NavBarActivity {
    public static void parentDashboard(Context context) {
        Intent intent = new Intent(context, ParentDashboardActivity.class);
        context.startActivity(intent);
    }

    public static void parentProfile(Context context) {
        Intent intent = new Intent(context, SignOut.class);
        context.startActivity(intent);
    }

    public static void myChildren(Context context) {
        Intent intent = new Intent(context, ManageChildrenActivity.class);
        context.startActivity(intent);
    }
    public static void childDashboard(Context context) {
        Intent intent = new Intent(context, ChildDashboardActivity.class);
        context.startActivity(intent);
    }

    public static void childProfile(Context context) {
        Intent intent = new Intent(context, SignOut.class);
        context.startActivity(intent);
    }

    public static void triage(Context context) {
        Intent intent = new Intent(context, ParentOnboardingFragment1.class);// replace with triage
        context.startActivity(intent);
    }

    public void parentNav(Context context, int itemid) {
        if (itemid == R.id.home) {
            parentDashboard(context);
        } else if (itemid == R.id.mychild) {
            myChildren(context);
        } else if (itemid == R.id.profile) {
            parentProfile(context);
        }
    }
    public void childNav(Context context, int itemid) {
        if (itemid == R.id.childhome) {
            childDashboard(context);
        } else if (itemid == R.id.triage) {
            triage(context);
        } else if (itemid == R.id.childprofile) {
            childProfile(context);
        }

    }
}
