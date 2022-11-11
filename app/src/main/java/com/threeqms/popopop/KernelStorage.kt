package com.threeqms.popopop

class KernelStorage {
    var kernels : MutableList<KernelLedger> = mutableListOf()

    fun initialize(){
        kernels.clear()
        for(i in 0 until KernelData.KernelTypes.size){
            if(i == 0) {
                kernels.add(i, KernelLedger(i, 5, 5))
            }
            else{
                kernels.add(i, KernelLedger(i, 0, 0))
            }
        }
    }
}
data class KernelLedger(val id : Int, val numOwned : Int, val numAdded : Int){

}