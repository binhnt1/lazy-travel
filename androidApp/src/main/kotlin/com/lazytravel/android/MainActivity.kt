package com.lazytravel.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.lazytravel.data.models.BlogCategory
import com.lazytravel.data.models.BlogPost
import com.lazytravel.data.models.Buddy
import com.lazytravel.data.models.BuddyReview
import com.lazytravel.data.models.City
import com.lazytravel.data.models.Country
import com.lazytravel.data.models.Place
import com.lazytravel.data.models.HowItWork
import com.lazytravel.data.models.Post
import com.lazytravel.data.models.PostComment
import com.lazytravel.data.models.PostLike
import com.lazytravel.data.models.PostMedia
import com.lazytravel.data.models.PostShare
import com.lazytravel.data.models.Review
import com.lazytravel.data.models.ReviewComment
import com.lazytravel.data.models.ReviewLike
import com.lazytravel.data.models.ReviewMedia
import com.lazytravel.data.models.Stat
import com.lazytravel.data.models.Tour
import com.lazytravel.data.models.TourProvider
import com.lazytravel.data.models.FlightProvider
import com.lazytravel.data.models.InsuranceProvider
import com.lazytravel.data.models.InsurancePackage
import com.lazytravel.data.models.VisaProvider
import com.lazytravel.data.models.UseCase
import com.lazytravel.data.models.User
import com.lazytravel.data.models.BuddyParticipant
import com.lazytravel.data.models.Feature
import com.lazytravel.data.models.TourParticipant
import com.lazytravel.data.models.TourReview
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.ui.navigation.AppNavigation
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PocketBaseClient.initialize()
        GlobalScope.launch {
            Country().setup()
            City().setup()
            Place().setup(true)

            User().setup()
            Stat().setup()
            UseCase().setup()
            Feature().setup()

            Post().setup()
            PostLike().setup()
            PostMedia().setup()
            PostShare().setup()
            PostComment().setup()

            // Review models
            Review().setup()
            ReviewMedia().setup()
            ReviewLike().setup()
            ReviewComment().setup()

            // Travel Buddy models
            Buddy().setup()
            BuddyReview().setup()
            BuddyParticipant().setup()

            // Blog models
            BlogCategory().setup()
            BlogPost().setup()

            // Tour and Provider models
            FlightProvider().setup()     // Flight provider first
            TourProvider().setup()
            InsuranceProvider().setup()
            VisaProvider().setup()
            InsurancePackage().setup()   // Package after provider

            Tour().setup()               // Tour last (depends on providers)
            TourReview().setup()
            TourParticipant().setup()

            HowItWork().setup()
        }

        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}
