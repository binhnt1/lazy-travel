package com.lazytravel.data.remote

import com.lazytravel.data.models.Tour
import com.lazytravel.data.models.TourProvider
import com.lazytravel.data.models.City
import com.lazytravel.data.models.Place
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Debug utilities for testing PocketBase setup and data seeding
 */
object DebugUtils {
    
    /**
     * Test just the connection and authentication
     */
    suspend fun testConnection(): Boolean {
        println("🔍 === TESTING POCKETBASE CONNECTION ===")
        return PocketBaseApi.debugConnection()
    }
    
    /**
     * Test a single model setup (useful for debugging specific models)
     */
    suspend fun testModelSetup(modelName: String) {
        println("🧪 === TESTING $modelName SETUP ===")
        
        when (modelName.lowercase()) {
            "tourprovider" -> {
                val result = TourProvider().setup()
                result.fold(
                    onSuccess = { println("✅ TourProvider setup: $it") },
                    onFailure = { println("❌ TourProvider setup failed: ${it.message}") }
                )
            }
            "city" -> {
                val result = City().setup()
                result.fold(
                    onSuccess = { println("✅ City setup: $it") },
                    onFailure = { println("❌ City setup failed: ${it.message}") }
                )
            }
            "place" -> {
                val result = Place().setup()
                result.fold(
                    onSuccess = { println("✅ Place setup: $it") },
                    onFailure = { println("❌ Place setup failed: ${it.message}") }
                )
            }
            "tour" -> {
                val result = Tour().setup()
                result.fold(
                    onSuccess = { println("✅ Tour setup: $it") },
                    onFailure = { println("❌ Tour setup failed: ${it.message}") }
                )
            }
            else -> {
                println("❌ Unknown model: $modelName")
                println("Available models: tourprovider, city, place, tour")
            }
        }
        
        println("=== END $modelName TEST ===\n")
    }
    
    /**
     * Test dependencies for Tour model
     */
    suspend fun testTourDependencies() {
        println("🔗 === TESTING TOUR DEPENDENCIES ===")
        
        val providerRepo = com.lazytravel.data.base.BaseRepository<TourProvider>()
        val cityRepo = com.lazytravel.data.base.BaseRepository<City>()
        val placeRepo = com.lazytravel.data.base.BaseRepository<Place>()
        
        val providers = providerRepo.getRecords<TourProvider>().getOrNull() ?: emptyList()
        val cities = cityRepo.getRecords<City>().getOrNull() ?: emptyList()
        val places = placeRepo.getRecords<Place>().getOrNull() ?: emptyList()
        
        println("📊 Dependency Status:")
        println("   TourProviders: ${providers.size} records ${if (providers.isEmpty()) "❌ MISSING" else "✅ OK"}")
        println("   Cities: ${cities.size} records ${if (cities.isEmpty()) "❌ MISSING" else "✅ OK"}")
        println("   Places: ${places.size} records ${if (places.isEmpty()) "⚠️ OPTIONAL (empty)" else "✅ OK"}")
        
        if (providers.isNotEmpty()) {
            println("\n📋 Available TourProviders:")
            providers.take(3).forEach { provider ->
                println("   - ${provider.name} (ID: ${provider.id.take(8)}...)")
            }
        }
        
        if (cities.isNotEmpty()) {
            println("\n📋 Available Cities:")
            cities.take(5).forEach { city ->
                println("   - ${city.name} (ID: ${city.id.take(8)}...)")
            }
        }
        
        println("=== END DEPENDENCY TEST ===\n")
    }
    
    /**
     * Quick test to verify Tour seed data generation
     */
    suspend fun testTourSeedData() {
        println("🌱 === TESTING TOUR SEED DATA GENERATION ===")
        
        val tour = Tour()
        val seedData = tour.getSeedData()
        
        println("📊 Seed Data Results:")
        println("   Generated ${seedData.size} tour records")
        
        if (seedData.isNotEmpty()) {
            println("\n📋 Sample Tours:")
            seedData.take(3).forEach { tour ->
                println("   - ${tour.name}")
                println("     Provider: ${tour.tourProviderId.take(8)}...")
                println("     City: ${tour.cityId.take(8)}...")
                println("     Price: ${tour.getFormattedPrice()}")
            }
        } else {
            println("❌ No seed data generated - check dependencies")
        }
        
        println("=== END SEED DATA TEST ===\n")
    }
    
    /**
     * Run all debug tests in sequence
     */
    fun runAllTests() {
        GlobalScope.launch {
            println("🚀 === STARTING COMPREHENSIVE DEBUG TESTS ===\n")
            
            // Test 1: Connection
            val connectionOk = testConnection()
            if (!connectionOk) {
                println("❌ Connection failed - stopping tests")
                return@launch
            }
            
            // Test 2: Dependencies
            testTourDependencies()
            
            // Test 3: Individual model setups
            testModelSetup("tourprovider")
            testModelSetup("city") 
            testModelSetup("place")
            testModelSetup("tour")
            
            // Test 4: Seed data generation
            testTourSeedData()
            
            println("🎉 === ALL DEBUG TESTS COMPLETED ===")
        }
    }
}