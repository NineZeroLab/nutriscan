package com.mdev.openfoodfacts_client.domain.model

enum class Allergen(val heading: String,val allergenStrings: List<String>) {
    GLUTEN("Gluten", AllergenTypes.GLUTEN_ALLERGENS),
    CRUSTACEANS("Crustaceans", AllergenTypes.CRUSTACEANS_ALLERGENS),
    EGG("Egg", AllergenTypes.EGG_ALLERGENS),
    FISH("Fish", AllergenTypes.FISH_ALLERGENS),
    RED_CAVIAR("Red Caviar", AllergenTypes.RED_CAVIAR_ALLERGENS),
    ORANGE("Orange", AllergenTypes.ORANGE_ALLERGENS),
    KIWI("Kiwi", AllergenTypes.KIWI_ALLERGENS),
    BANANA("Banana", AllergenTypes.BANANA_ALLERGENS),
    PEACH("Peach", AllergenTypes.PEACH_ALLERGENS),
    APPLE("Apple", AllergenTypes.APPLE_ALLERGENS),
    BEEF("Beef", AllergenTypes.BEEF_ALLERGENS),
    PORK("Pork", AllergenTypes.PORK_ALLERGENS),
    CHICKEN("Chicken", AllergenTypes.CHICKEN_ALLERGENS),
    YAMAIMO("Yamaimo", AllergenTypes.YAMAIMO_ALLERGENS),
    GELATIN("Gelatin", AllergenTypes.GELATIN_ALLERGENS),
    MATSUTAKE("Matsutake", AllergenTypes.MATSUTAKE_ALLERGENS),
    PEANUTS("Peanuts", AllergenTypes.PEANUT_ALLERGENS),
    SOYBEANS("Soybeans", AllergenTypes.SOY_ALLERGENS),
    MILK("Milk", AllergenTypes.MILK_ALLERGENS),
    NUTS("Nuts", AllergenTypes.NUTS_ALLERGENS),
    CELERY("Celery", AllergenTypes.CELERY_ALLERGENS),
    MUSTARD("Mustard", AllergenTypes.MUSTARD_ALLERGENS),
    SESAME("Sesame", AllergenTypes.SESAME_ALLERGENS),
    SULPHUR_DIOXIDE_AND_SULPHITES("Sulphur Dioxide and Sulphites", AllergenTypes.SULPHUR_DIOXIDE_AND_SULPHIDE_ALLERGENS),
    LUPIN("Lupin", AllergenTypes.LUPIN_ALLERGENS),
    MOLLUSCS("Molluscs", AllergenTypes.MOLLUSCS_ALLERGENS)
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