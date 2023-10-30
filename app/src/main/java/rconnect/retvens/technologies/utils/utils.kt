package rconnect.retvens.technologies.utils

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import rconnect.retvens.technologies.R
import java.util.TimeZone


// ------------ Display the Location Suggestions ---------
 fun autoFillLocationSuggestion(context:Context, countryList : ArrayList<String>, countryText : AutoCompleteTextView, stateText : AutoCompleteTextView, cityText : AutoCompleteTextView) {

    // Sample list of country names
    val countries = arrayOf(
        "United States", "United Kingdom", "Canada", "Australia", "Germany", "France", "Japan", "India"
        // Add more country names as needed
    )
    // Sample list of state names
    val states = arrayOf(
        "Madhya Pradesh", "Maharastra", "Utter Pradesh", "Kannada", "Tamil Nadu"
        // Add more states names as needed
    )
    // Sample list of city names
    val cities = arrayOf(
        "Indore", "outdoor", "Kanpur Nagar", "Kanpur Dehat", "Lucknow", "Banglore"
        // Add more states names as needed
    )

    // Create an ArrayAdapter with the country names and set it to the AutoCompleteTextView
    val adapterCountries = ArrayAdapter(context, R.layout.simple_dropdown_item_1line, countryList)
    adapterCountries.notifyDataSetChanged()
    val adapterStates = ArrayAdapter(context, R.layout.simple_dropdown_item_1line, states)
    val adapterCities = ArrayAdapter(context, R.layout.simple_dropdown_item_1line, cities)
    countryText.setAdapter(adapterCountries)
    stateText.setAdapter(adapterStates)
    cityText.setAdapter(adapterCities)

}


// ------------ Display the country name using timezone---------
 fun fetchCountryName() : String {
//        Get the default time zone
    val timeZone = TimeZone.getDefault()

    // Get the ID of the time zone
    val timeZoneId = timeZone.id

    // Get the country (locale) associated with the time zone
    val country = getCountryForTimeZone(timeZoneId)


    Log.d("TimeZone", "Time Zone ID: $timeZoneId")
    Log.d("TimeZone", "Country: $country")

     // Display the country name
     return country
}
private fun getCountryForTimeZone(timeZoneId: String): String {
    // Define a mapping of common time zones to countries
    val timeZoneToCountry = mapOf(
        "America/New_York" to "United States",
        "America/Los_Angeles" to "United States",
        "America/Chicago" to "United States",
        "America/Denver" to "United States",
        "America/Phoenix" to "United States",
        "America/Anchorage" to "United States",
        "America/Honolulu" to "United States (Hawaii)",
        "Europe/London" to "United Kingdom",
        "Europe/Paris" to "France",
        "Europe/Berlin" to "Germany",
        "Asia/Tokyo" to "Japan",
        "Asia/Shanghai" to "China",
        "Asia/Dubai" to "United Arab Emirates",
        "Asia/Kolkata" to "India",
        "Australia/Sydney" to "Australia",
        "Pacific/Auckland" to "New Zealand",
        "Africa/Cairo" to "Egypt",
        "Africa/Johannesburg" to "South Africa",
        "Asia/Singapore" to "Singapore",
        "Asia/Hong_Kong" to "Hong Kong",
        "America/Toronto" to "Canada",
        "Europe/Moscow" to "Russia",
        "America/Mexico_City" to "Mexico",
        "America/Buenos_Aires" to "Argentina",
        "Europe/Istanbul" to "Turkey"
        // Add more mappings as needed
    )

    // Look up the country for the given time zone
    val country = timeZoneToCountry[timeZoneId]

    // Return the country if found, or "Unknown" if not found
    return country ?: "Unknown"
}
