# Translation Files

## File Structure

Each language has its own JSON file:
```
lang_vi.json  - Vietnamese (Tiếng Việt)
lang_en.json  - English
lang_fr.json  - French (if added)
lang_es.json  - Spanish (if added)
...
```

## JSON Format

```json
{
  "key": "translation",
  "another_key": "another translation"
}
```

**Example:**
```json
{
  "app_name": "LazyTravel",
  "hero_title": "Plan your trip easily with friends",
  "login": "Login"
}
```

## How to Add a New Language

### Step 1: Create JSON file

1. Copy `lang_en.json` to `lang_{code}.json`
   - Example: `lang_fr.json` for French
2. Translate all values (keep keys unchanged!)
   ```json
   {
     "app_name": "LazyTravel",        // Keep as is
     "hero_title": "Planifiez votre voyage facilement",  // Translate
     "login": "Connexion"             // Translate
   }
   ```

### Step 2: Update Language enum

Edit: `shared/src/commonMain/kotlin/com/lazytravel/core/i18n/Language.kt`

Add new enum value:
```kotlin
enum class Language(val code: String, val displayName: String, val jsonFile: String) {
    VIETNAMESE("vi", "Tiếng Việt", "lang_vi.json"),
    ENGLISH("en", "English", "lang_en.json"),
    FRENCH("fr", "Français", "lang_fr.json"),  // Add this
}
```

### Step 3: Add translations to LocalizationManager

Edit: `shared/src/commonMain/kotlin/com/lazytravel/core/i18n/LocalizationManager.kt`

In `getHardcodedTranslations()` function, add:
```kotlin
Language.FRENCH -> mapOf(
    "app_name" to "LazyTravel",
    "hero_title" to "Planifiez votre voyage facilement",
    // ... copy from lang_fr.json
)
```

### Step 4: Test

1. Rebuild app
2. Language switcher will show new language option
3. Switch to new language and verify translations

## Translation Keys

### App General
- `app_name` - Application name

### Hero Section
- `hero_title` - Main hero title
- `hero_subtitle` - Hero subtitle
- `get_started_free` - CTA button
- `login` - Login button

### Stats Bar
- `stats_users` - Users label
- `stats_trips` - Trips label
- `stats_destinations` - Destinations label
- `stats_rating` - Rating label

### Features Section
- `features_title` - Section title
- `feature_voting` - Democratic Voting title
- `feature_voting_desc` - Democratic Voting description
- `feature_cost_splitting` - Cost Splitting title
- `feature_cost_splitting_desc` - Cost Splitting description
- `feature_ai_itinerary` - AI Itinerary title
- `feature_ai_itinerary_desc` - AI Itinerary description
- `feature_shared_album` - Shared Album title
- `feature_shared_album_desc` - Shared Album description

## Notes

- **Keep keys consistent** across all language files
- **Don't translate keys**, only values
- Keys use `snake_case` format
- Current implementation uses hardcoded maps (temporary)
- Future: Will load directly from JSON files using expect/actual pattern
