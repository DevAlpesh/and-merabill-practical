# Peabody Soft (MeraBills) - Practical


## Main Features 
1. Click on 'Add payment' to add new payments.
2. Add payment dialog: Enter the amount, Select the Payment Type, and provide other inputs to save the payment.
3. Display the added payment on the main screen. Then click on the 'Save' button, and it will be saved into a file in JSON format.
4. By clicking the close icon on the added payment item, you can delete the payment and click on 'Save' for an update.

## Files Structure 

1. Data -> Module - Modules
2. Data -> Repository - Repositories
3. Presentation - UI files
4. Utils -> Supported Helper files

## Requirements 

- Android Studio Version: Ladybug Feature Drop | 2024.2.2 Patch 1 
- Java Development Kit (JDK) 11 or higher
- Minimum SDK: 21 (Android 5.0 Lollipop)
- Target SDK: 34


## Used concept

- ViewModel & Repository architecture for data fetching and file storage.
- SavedInstanceState & ViewModel to manage data across activity lifecycle events.

Note: ViewModel alone can be used for lifecycle-aware data management.


   
