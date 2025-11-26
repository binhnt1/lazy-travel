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
import com.lazytravel.data.remote.DebugUtils
import com.lazytravel.data.remote.PocketBaseApi
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
            // DEBUG MODE - Set to true to run comprehensive debug tests
            val DEBUG_MODE = true
            
            if (DEBUG_MODE) {
                println("🐛 DEBUG MODE ENABLED - Running comprehensive tests...")
                DebugUtils.runAllTests()
                println("\n" + "=".repeat(50) + "\n")
            }
            
            // Debug connection first
            println("🔍 === POCKETBASE CONNECTION DEBUG ===")
            val connectionOk = PocketBaseApi.debugConnection()
            if (!connectionOk) {
                println("❌ Connection debug failed - setup may not work properly")
            } else {
                println("✅ Connection debug passed - proceeding with setup")
            }
            println("=== END CONNECTION DEBUG ===\n")

            // Setup with enhanced logging
            println("🚀 === STARTING DATA SETUP ===")
            
            // Foundation data (no dependencies)
            println("\n📍 Setting up location data...")
            Country().setup()
            City().setup()
            Place().setup(true)

            println("\n👥 Setting up user data...")
            User().setup()
            Stat().setup()
            UseCase().setup()
            Feature().setup()

            println("\n📝 Setting up post data...")
            Post().setup()
            PostLike().setup()
            PostMedia().setup()
            PostShare().setup()
            PostComment().setup()

            println("\n⭐ Setting up review data...")
            Review().setup()
            ReviewMedia().setup()
            ReviewLike().setup()
            ReviewComment().setup()

            println("\n🤝 Setting up buddy data...")
            Buddy().setup()
            BuddyReview().setup()
            BuddyParticipant().setup()

            println("\n📰 Setting up blog data...")
            BlogCategory().setup()
            BlogPost().setup()

            println("\n✈️ Setting up provider data...")
            FlightProvider().setup()     // Flight provider first
            TourProvider().setup()
            InsuranceProvider().setup()
            VisaProvider().setup()
            InsurancePackage().setup()   // Package after provider

            println("\n🎯 Setting up tour data (depends on providers)...")
            Tour().setup()               // Tour last (depends on providers)
            TourReview().setup()
            TourParticipant().setup()

            println("\nℹ️ Setting up how it works...")
            HowItWork().setup()
            
            println("🎉 === DATA SETUP COMPLETED ===")
        }

        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}
