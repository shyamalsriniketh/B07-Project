**SMART AIR Overview**

An Android application designed to support asthma management for young users, offering medication tracking, symptom logging, and educational tools. The system facilitates secure data sharing between families and healthcare professionals through structured reporting.

**Description of Features**

SMART AIR provides a suite of features. These included but not limited to: sign up & login system, onboarding, dashboard, user medical data input, child daily check-in, inventory, motivation, manage Children, Emergency function and alert system.

Details of each feature are described below.

**Sign up & Login System**

* Parents and providers can register using email and password, children may use the account that their parents created for them.
* Firebase Authentication handles secure sign-in, credential recovery, and role-based account creation.
* Follows MVP (Model-View-Presenter) architecture for clean separation of concerns.

**Onboarding**

* First-time users receive a guided introduction explaining medication types, app purpose, and privacy settings.
* Role-specific onboarding flows for children (simplified version), parents, and healthcare providers.

**Dashboard**

* Parents and children view a summary dashboard showing each child’s current asthma zone, recent medication use, and weekly trends.
* Healthcare providers view a read-only summary of data shared by parents for their child.
* Dashboard updates in real-time as new logs or zone changes occur.

**Child Medical Data Input**

* Manual entry for PEF with optional pre- and post-medication values.
* Medication logs separated by rescue and controller types, including dose count, pre/post check, and timestamps.
* Children are able to mark low inventory if they find out there is not enough medicine left.

**Daily Check-In**

* Tracks night waking, activity limits, cough, wheeze and trigger selection.
* Entries are marked as child-entered or parent-entered for clarity.

**Inventory**

* Parents can track inhaler purchase dates, remaining doses, and expiration dates.
* Children can manually flag when an inhaler feels empty.
* Low canister alerts (≤20% remaining) and expiry notifications for parents.

**Motivation**

* Streaks for consecutive days of controller medication use and proper technique practice.
* Badges awarded for achievements.
* Configurable thresholds for badges and streaks in parent settings.

**Manage Children**

* Parents can add, edit, and manage profiles for multiple children.
* Each profile includes name, age, personal best PEF, medicine related data, medicine usage data, action plan for zones in different situations and optional notes.
* Granular sharing toggles per child to control what data is visible to healthcare providers.
* Parents are able to access their child screen through managing children.
* Real-time permission updates with visual “shared” indicators.

**Emergency Function(Triage)**

* One-tap triage flow activated by the “Exclamation mark” button in the middle of the child navigation bar.
* Guides users through red-flag checks and recommends next steps (call emergency or/and follow home plan).
* Includes a safety disclaimer and hidden auto-escalation timer for worsening symptoms.
* Notifies parents immediately when triage is started or escalated.
* Recheck if it is still on the homesteps screen after ten minutes.


**Alert System**

* Real-time notifications for safety events: red-zone days, rapid rescue use, triage escalation, and low inventory.
* Push notifications delivered with low latency targets (<10 seconds).
* In-app alerts for medication reminders, technique practice, and weekly summaries.
* Configurable alert preferences per child and notification type.


**Assumptions:**
- All times are UTC time zones.
- If the parent is viewing the child screen, they will back out before closing the app.
- If a parent decides the child shouldn't take a controller dose today, entry won't be allowed.
- If a parent decides a child shouldn't take controller dose today after they've already entered a dose, the entry will get deleted.
- Children can only take controller medicine once a day.
- Parents will see the alerts whenever they open the dashboard.
- A perfect week badge assumes 7 controller adherence days in a row.
- When users look at technique helpers, we assume they achieve consecutive technique-completion.
- The graphic visualization is the place to display the PEF and zones.
- Triage is logged as an emergency if and only if the red flag categories are ticked. Otherwise logging category is based on PEF inputted in triage, if no PEF is inputted then latest child PEF is considered, if no latest child PEF then PB is used, and categories are Red Green and yellow.
- Ticking a red flag only gives you the option to call the emergency, while having no red flags ticked only allows you to start home steps.
- Worse symptoms are defined as any Red flags present(since this implies either new symptoms or persisting symptoms), or a worse PEF than the previous triage.
- Recheck is defined as the re-triage that is automated if you stay for 10 minutes on the homesteps screen.
- Escalation is defined as Recheck+Worse symptoms.
