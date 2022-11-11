package com.threeqms.popopop

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class KernelStorage(a : Activity) {
    var kernels : MutableList<KernelLedger> = mutableListOf()
    val activityContext : Activity = a

    fun initialize(){
        kernels.clear()

        val prefs : SharedPreferences = activityContext.getPreferences(Context.MODE_PRIVATE)
        val ledger : String? = prefs.getString(activityContext.getString(R.string.kernel_count_key), null)
        if(ledger != null){
            val gson = Gson()
            val arrayKernelLedgersType = object : TypeToken<Array<KernelLedger>>() {}.type

            try {
                var kernelLegerArray: Array<KernelLedger> =
                    gson.fromJson(ledger, arrayKernelLedgersType)
                kernelLegerArray.forEachIndexed  { idx, kernel -> kernels.add(kernel) }
                while(kernels.size > KernelData.KernelTypes.size){
                    kernels.removeAt(kernels.size - 1)
                }
                while(kernels.size < KernelData.KernelTypes.size){
                    kernels.add(KernelLedger(kernels.size - 1, 0, 0))
                }
            }
            catch(e : JsonSyntaxException){
                resetKernels()
            }
        }
        else {
            resetKernels()
        }
    }
    fun saveKernelsToPrefs(){
        val gson : Gson = Gson()
        val ledgerString : String = gson.toJson(kernels)

        val sharedPref = activityContext?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(activityContext.getString(R.string.kernel_count_key), ledgerString)
            apply()
        }
    }
    fun resetKernels(){
        for (i in 0 until KernelData.KernelTypes.size) {
            if (i == 0) {
                kernels.add(i, KernelLedger(i, 5, 3))
            } else {
                kernels.add(i, KernelLedger(i, 0, 0))
            }
        }
        saveKernelsToPrefs()
    }
}
data class KernelLedger(val id : Int, var numOwned : Int, var numAdded : Int){

}
class KernelLedgers{
    var kernelLedgers : MutableList<KernelLedger> = mutableListOf()
}