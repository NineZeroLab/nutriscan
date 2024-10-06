package com.mdev.core.domain.model


enum class Allergen(val heading: String,val allergenStrings: List<String>) {
    GLUTEN("Gluten", com.mdev.core.utils.AppResources.GLUTEN_ALLERGENS),
    CRUSTACEANS("Crustaceans", com.mdev.core.utils.AppResources.CRUSTACEANS_ALLERGENS),
    EGG("Egg", com.mdev.core.utils.AppResources.EGG_ALLERGENS),
    FISH("Fish", com.mdev.core.utils.AppResources.FISH_ALLERGENS),
    RED_CAVIAR("Red Caviar", com.mdev.core.utils.AppResources.RED_CAVIAR_ALLERGENS),
    ORANGE("Orange", com.mdev.core.utils.AppResources.ORANGE_ALLERGENS),
    KIWI("Kiwi", com.mdev.core.utils.AppResources.KIWI_ALLERGENS),
    BANANA("Banana", com.mdev.core.utils.AppResources.BANANA_ALLERGENS),
    PEACH("Peach", com.mdev.core.utils.AppResources.PEACH_ALLERGENS),
    APPLE("Apple", com.mdev.core.utils.AppResources.APPLE_ALLERGENS),
    BEEF("Beef", com.mdev.core.utils.AppResources.BEEF_ALLERGENS),
    PORK("Pork", com.mdev.core.utils.AppResources.PORK_ALLERGENS),
    CHICKEN("Chicken", com.mdev.core.utils.AppResources.CHICKEN_ALLERGENS),
    YAMAIMO("Yamaimo", com.mdev.core.utils.AppResources.YAMAIMO_ALLERGENS),
    GELATIN("Gelatin", com.mdev.core.utils.AppResources.GELATIN_ALLERGENS),
    MATSUTAKE("Matsutake", com.mdev.core.utils.AppResources.MATSUTAKE_ALLERGENS),
    PEANUTS("Peanuts", com.mdev.core.utils.AppResources.PEANUT_ALLERGENS),
    SOYBEANS("Soybeans", com.mdev.core.utils.AppResources.SOY_ALLERGENS),
    MILK("Milk", com.mdev.core.utils.AppResources.MILK_ALLERGENS),
    NUTS("Nuts", com.mdev.core.utils.AppResources.NUTS_ALLERGENS),
    CELERY("Celery", com.mdev.core.utils.AppResources.CELERY_ALLERGENS),
    MUSTARD("Mustard", com.mdev.core.utils.AppResources.MUSTARD_ALLERGENS),
    SESAME("Sesame", com.mdev.core.utils.AppResources.SESAME_ALLERGENS),
    SULPHUR_DIOXIDE_AND_SULPHITES("Sulphur Dioxide and Sulphites", com.mdev.core.utils.AppResources.SULPHUR_DIOXIDE_AND_SULPHIDE_ALLERGENS),
    LUPIN("Lupin", com.mdev.core.utils.AppResources.LUPIN_ALLERGENS),
    MOLLUSCS("Molluscs", com.mdev.core.utils.AppResources.MOLLUSCS_ALLERGENS)
}



fun getAllergenConclusion(productAllergens: List<Allergen>, userAllergens: List<Allergen>?): String{
    if (productAllergens.isEmpty() || userAllergens == null ){
        return ""
    }
    if (userAllergens.isEmpty()){
        return "No Allergens preference."
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