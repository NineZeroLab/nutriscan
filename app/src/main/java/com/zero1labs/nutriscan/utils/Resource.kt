package com.zero1labs.nutriscan.utils

import android.util.Log
import com.zero1labs.nutriscan.models.data.NutrientPreference
import com.zero1labs.nutriscan.models.data.NutrientPreferenceType
import com.zero1labs.nutriscan.utils.DietaryRestriction.*
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data,message)
}


object FirebaseCollection{
    const val USERS = "users"
    const val SEARCH = "search"
}
object AppResources{
    const val BASE_URL : String = "https://world.openfoodfacts.org/api/v2/"
    const val TAG = "logger"
    private const val DEMO_ITEM_ID = "3017620422003"
    private const val BHUJJIA = "011433157933"
    private const val SEASONED_PEANUTS = "059966050932"
    private const val ALOO_LACHHA = "8904004402858"
    private const val COCA_COLA_CAN = "5449000256805"

    const val PRODUCT_NOT_FOUND = "Product Not Found In the Database"
    const val UNABLE_TO_ESTABLISH_CONNECTION = "Unable to establish connection"
    const val UNKNOWN_ERROR = "Unknown Error Occurred"
    const val CONNECTION_TIMEOUT = "Connection Timed Out"

    const val INVALID_USERNAME_PASSWORD = "Invalid Username or Password. Try Again."

    const val USER_DETAILS_UPDATED = "User Details Updated Successfully"
    const val USER_DETAILS_UPDATE_FAILURE = "Update failed "

    const val USERS = "users"

    private val ALLERGEN_FREE = listOf(
        "en:none" ,
        "en:allergens-free",
        "en:without allergens",
        "en:without",
        "en:n",
        "en:no",
        "en:no allergens",
        "en:0"
    )
    val GLUTEN_ALLERGENS = listOf(
        "en:gluten",
        "en:cereals containing gluten",
        "en:other cereals containing gluten",
        "en:barley",
        "en:barley malt flour",
        "en:malted barley",
        "en:malted barley extract",
        "en:malted barley flour",
        "en:kamut",
        "en:rye",
        "en:rye flour",
        "en:spelt",
        "en:speltflour",
        "en:wheat",
        "en:wheat flour",
        "en:wheatflour",
        "en:wheat semolina",
        "en:oats",
        "en:oat fiber",
    )
    val CRUSTACEANS_ALLERGENS = listOf(
        "en:crustaceans",
        "en:crab",
        "en:lobster",
        "en:crayfish",
        "en:prawn",
        "en:shrimp",
    )
    val EGG_ALLERGENS = listOf(
        "en:eggs",
        "en:egg",
        "en:barn egg",
        "en:egg whites",
        "en:egg white",
        "en:egg yolks",
        "en:egg yolk",
        "en:whole eggs",
        "en:whole egg"
    )
    val FISH_ALLERGENS = listOf(
        "en:fish",
        "en:fishes",
        "en:cod",
        "en:mackerel",
        "en:flounder",
        "en:halibut",
        "en:turbot",
        "en:haddock",
        "en:salmon",
        "en:sole",
        "en:trout",
        "en:tuna",
        "en:sardine",
        "en:sardines"
    )
    val PEANUT_ALLERGENS = listOf(
        "en:peanuts",
        "en:peanut",
        "en:arachis hypogaea",
    )
    val SOY_ALLERGENS = listOf(
        "en:soybeans",
        "en:soya",
        "en:soja",
        "en:soia",
        "en:soy",
        "en:soya bean",
        "en:soy flour",
        "en:soya flour",
        "en:soy lecithin",
        "en:soy lecithins",
        "en:soya lecithin",
        "en:soya lecithins",
        "en:soy lecithines",
        "en:soy protein isolate",
        "en:soya products"
    )
    val MILK_ALLERGENS = listOf(
        "en:milk",
        "en:lactose",
        "en:whey",
        "en:dairy",
        "en:butter",
        "en:buttermilk",
        "en:cream",
        "en:yogurt",
        "en:cheese",
        "en:yoghurt",
        "en:parmigiano reggiano",
        "en:grana padano",
        "en:milk chocolate coating",
        "en:milk powder",
        "en:milk protein",
    )
    val NUTS_ALLERGENS = listOf(
        "en:nuts",
        "en:almonds",
        "en:hazelnuts",
        "en:walnuts",
        "en:cashews",
        "en:cashew",
        "en:pecan nuts",
        "en:pecan",
        "en:Brazil nuts",
        "en:pistachio nuts",
        "en:pistachio",
        "en:macadamia",
        "en:Macadamia nuts",
        "en:Queensland nuts",
        "en:tree nuts",
        "en:treenuts",
        "en:other nuts",
        "en:other tree nuts"
    )
    val CELERY_ALLERGENS = listOf(
        "en:celery",
        "en:celeriac"
    )
    val MUSTARD_ALLERGENS = listOf(
        "en:mustard",
        "en:brassica"
    )
    val SESAME_ALLERGENS = listOf(
        "en:sesame seeds",
        "en:sesame"
    )
    val SULPHUR_DIOXIDE_AND_SULPHIDE_ALLERGENS = listOf(
        "en:sulphur dioxide and sulphites",
        "en:sulphur dioxide",
        "en:sulphites",
        "en:sulfites"
    )
    val LUPIN_ALLERGENS = listOf(
        "en:lupin",
        "en:lupine",
    )
    val MOLLUSCS_ALLERGENS = listOf(
        "en:molluscs",
        "en:mollusc",
        "en:mollusks",
        "en:mollusk",
        "en:squid",
        "en:cuttlefish",
        "en:oysters",
        "en:oyster",
        "en:mussels",
        "en:mussel",
        "en:clams",
        "en:clam",
        "en:scallops",
        "en:scallop",
    )
    val RED_CAVIAR_ALLERGENS = listOf(
        "en:red caviar"
    )
    val ORANGE_ALLERGENS = listOf(
        "en:orange"
    )
    val KIWI_ALLERGENS = listOf(
        "en:kiwi"
    )
    val BANANA_ALLERGENS = listOf(
        "en:banana"
    )
    val PEACH_ALLERGENS = listOf(
        "en:peach"
    )
    val APPLE_ALLERGENS = listOf(
        "en:apple"
    )
    val BEEF_ALLERGENS = listOf(
        "en:beef"
    )
    val PORK_ALLERGENS = listOf(
        "en:pork"
    )
    val CHICKEN_ALLERGENS = listOf(
        "en:chicken"
    )
    val GELATIN_ALLERGENS = listOf(
        "en:gelatin"
    )
    val YAMAIMO_ALLERGENS = listOf(
        "en:yamaimo"
    )
    val MATSUTAKE_ALLERGENS = listOf(
        "en:matsutake"
    )

    fun getRandomItem(): String{
        return listOf(
            DEMO_ITEM_ID,
            BHUJJIA,
            SEASONED_PEANUTS,
            ALOO_LACHHA,
            COCA_COLA_CAN,
            "40822099",
            "0049000042559",
            "1123"
        ).random()
    }

    fun getDietaryRestrictions(ingredientsHierarchy: List<String>?): List<DietaryRestriction>{
        Log.d(TAG,"restrictions: ${ingredientsHierarchy.toString()}")
        val restrictionsList = mutableListOf<DietaryRestriction>()
        if (ingredientsHierarchy == null) restrictionsList

        DietaryRestriction.values().forEach {restriction ->
            if (ingredientsHierarchy?.contains(restriction.response) == true){
                restrictionsList.add(restriction)
            }
        }
        return restrictionsList
    }

    fun getDietaryRestrictionConclusion(productRestrictions: List<DietaryRestriction>, userRestrictions: List<DietaryRestriction>?): String{
        if (productRestrictions.isEmpty()){
            return "Not enough data for calculating restrictions"
        }
        val veganRestrictions = listOf(
            VEGAN,
            NON_VEGAN,
            VEGAN_STATUS_UNKNOWN
        )
        val palmOilRestrictions = listOf(
            PALM_OIL,
            PALM_OIL_FREE,
            PALM_OIL_STATUS_UNKNOWN
        )
        val vegetarianRestrictions = listOf(
            VEGETARIAN,
            NON_VEGETARIAN,
            VEGETARIAN_STATUS_UNKNOWN
        )

        val matchedRestrictions = mutableListOf<DietaryRestriction>()
        productRestrictions.forEach {productRestriction ->
            userRestrictions?.forEach { userRestriction ->
                if (userRestriction == VEGAN && productRestriction in veganRestrictions){
                    matchedRestrictions.add(productRestriction)
                }else if (userRestriction == VEGETARIAN && productRestriction in vegetarianRestrictions){
                    matchedRestrictions.add(productRestriction)
                }else if (userRestriction == PALM_OIL_FREE && productRestriction in palmOilRestrictions){
                    matchedRestrictions.add(productRestriction)
                }
            }
        }


        return matchedRestrictions.joinToString(", "){ restriction ->
            restriction.conclusionString
        }
    }
    fun getAllergenConclusion(productAllergens: List<Allergen>, userAllergens: List<Allergen>?): String{
        if (productAllergens.isEmpty() || userAllergens == null || userAllergens?.isEmpty() == true){
            return ""
        }
        val commonAllergens = mutableListOf<Allergen>()
        productAllergens.forEach {productAllergen ->
            if (productAllergen in userAllergens){
                commonAllergens.add(productAllergen)
            }
        }
        if (commonAllergens.isEmpty()){
            return "This product is allergen-free, as per your selection."
        }
        return "This product contains ${commonAllergens.size} allergen(s) as per your selection."
    }
    fun getAllergens(allergensHierarchy: List<String>?): List<Allergen>{
        if (allergensHierarchy == null){
            return mutableListOf()
        }
        val allergensList = mutableListOf<Allergen>()
        allergensHierarchy.forEach { allergenString ->
            for(allergen in Allergen.entries){
                if (allergen.allergenStrings.contains(allergenString)){
                    if (!allergensList.contains(allergen)){
                        allergensList.add(allergen)
                    }
                    break
                }
            }
        }
        return allergensList
    }


    fun getProductType(categories: List<String>?): ProductType {
        if(categories == null) return ProductType.UNKNOWN
        if ("en:beverages" in categories) return ProductType.BEVERAGE
        return ProductType.FOOD
    }

    fun getServingTextFromProductType(productType: ProductType): String{
        return when(productType){
            ProductType.BEVERAGE -> "Per Serving 100ml"
            ProductType.FOOD -> "Per Serving 100gm"
            ProductType.UNKNOWN -> "Per Serving 100gm"
        }
    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return emailRegex.matches(email)
    }
    fun isValidPassword(password: String): Pair<Boolean, String> {
        // Minimum 8 characters, at least one letter and one number
        val symbols = "~`!@#$%^&*()_-+={[}]|:;'\"\\<,>.?/"
        if (password.length < 8) return Pair(false, "should be at least 8 letters")
        val containsSymbol = password.any { char ->
            symbols.contains(char)
        }
        if (!containsSymbol) return Pair(false, "should contain at least 1 symbol")
        val containsUpperCase = password.any { char ->
            char.isUpperCase()
        }
        if (!containsUpperCase) return Pair(false, "should contain at least 1 uppercase letter")

        val containsDigit = password.any { char ->
            char.isDigit()
        }
        if (!containsDigit) return Pair(false, "should contain at least 1 digit")
        return Pair(true, "valid password")
    }

    fun getDietaryPreferenceConclusion(productPreferences: List<NutrientPreference>, userPreferences: List<NutrientPreference>?): String {
        Log.d(TAG,"user Preferences: $userPreferences")
        //filtering user preference to remove null values in nutrientPreference type stored in firebase
        val filteredUserPreference = userPreferences?.filter { nutrientPreference ->
            nutrientPreference.nutrientPreferenceType != null
        }
        if (productPreferences.isEmpty()){
            return "Not enough data available to calculate Dietary Preferences."
        }
        if (filteredUserPreference.isNullOrEmpty()){
            return ""
        }
        val commonPreferences = mutableListOf<NutrientPreference>()

        productPreferences.forEach { productPreference ->
            filteredUserPreference.forEach { nutrientPreference ->
                if (productPreference == nutrientPreference){
                    commonPreferences.add(nutrientPreference)
                }
            }
        }
        if (commonPreferences.isEmpty()){
            return "None of the nutrients match your preference"
        }
        if (commonPreferences.size == filteredUserPreference.size){
            return "All the nutrients match your preference"
        }

        return "Some of the nutrients match your preference"
    }
}