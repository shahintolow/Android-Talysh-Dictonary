package com.com.talyshdictionary.managers

import android.app.Activity
import android.util.ArrayMap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*

object BillingManager {
    private const val tag = "billing_manager"
    private val skuList = arrayListOf("year_all_content")
    private lateinit var billingClient: BillingClient

    private val listSubs = ArrayMap<String, Purchase>() //key - SKU
    private val listSkuDetail = ArrayMap<String, SkuDetails>() //key - SKU

    private val purchaseUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->
            Log.d(tag, "PurchasesUpdatedListener")
            if (isOK(billingResult.responseCode) && purchases != null) {
                Log.d(tag, "PurchasesUpdatedListener OK and != null")
                Log.d(tag, "PurchasesUpdatedListener purchase size: ${purchases.size}")
                addPurchaseToList(purchases)
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                Log.d(tag, "PurchasesUpdatedListener USER_CANCELED")
            } else {
                Log.d(tag, "PurchasesUpdatedListener some error")
            }
        }


    fun init(activity: Activity) {
        Log.d(tag, "init")

        billingClient = BillingClient.newBuilder(activity)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                Log.d(tag, "onBillingSetupFinished(), code: ${billingResult.responseCode}")
                if (isOK(billingResult.responseCode)) {
                    querySkuDetails()
                    queryPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {
                Log.d(tag, "onBillingServiceDisconnected()")
            }
        })
    }

    fun querySkuDetails() : LiveData<List<SkuDetails>> {
        val data = MutableLiveData<List<SkuDetails>>()

        if(listSkuDetail.size > 0) {
            data.postValue(listSkuDetail.values.toList())
            return data
        }

        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient.querySkuDetailsAsync(params.build()) { result, list ->
            if(isOK(result.responseCode) && list != null) {
                listSkuDetail.clear()
                list.forEach { listSkuDetail[it.sku] = it }
                data.postValue(list)
            }
        }
        return data
    }

    fun queryPurchases() {

        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ){ bilRes, purscList ->
            addPurchaseToList(purscList)

        }

//        billingClient
//            .queryPurchases(BillingClient.SkuType.SUBS)
//            .purchasesList?.let {
//                Log.d(tag, "queryPurchases() result ${it.size}")
//                addPurchaseToList(it)
//            }
    }


    fun showSku(skuDetail: SkuDetails, activity: Activity) {
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetail)
            .build()
        val responseCode = billingClient.launchBillingFlow(activity, flowParams).responseCode
        if(isOK(responseCode))
            Log.d(tag, "sku show")
        else
            Log.d(tag, "error sku show")
    }

    private fun acknowledgedPurchase(purchase: Purchase) {
        Log.d(tag, "acknowledgedPurchase() state is ${purchase.purchaseState}")
        if (!purchase.isAcknowledged) {
            Log.d(tag, "purchase is not acknowledged")

            val acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(acknowledgePurchaseParams) {
                if(isOK(it.responseCode))
                    Log.d(tag, "purchase acknowledged success")
                else
                    Log.d(tag, "purchase acknowledged error")
            }
        } else {
            Log.d(tag, "purchase is acknowledged")
        }
    }

    fun isSubsActive() : Boolean {
        listSubs.forEach {
            if(isPurchased(it.value))
                return true
        }
        return false
    }

    fun isSubActive(sku: String) : Boolean {
        listSubs[sku]?.let {
            return isPurchased(it)
        }
        return false
    }

    private fun isPurchased(purchase: Purchase) = (purchase.purchaseState == Purchase.PurchaseState.PURCHASED)
    private fun isOK(resultCode: Int) = (resultCode == BillingClient.BillingResponseCode.OK)

    private fun addPurchaseToList(list: List<Purchase>) {
    	 listSubs.clear()
        list.forEach { purchase ->
          
            if(isPurchased(purchase))
                acknowledgedPurchase(purchase)
            listSubs[purchase.orderId] = purchase // ???

        }
    }

}