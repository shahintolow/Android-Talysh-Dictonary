package com.com.talyshdictionary.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.com.talyshdictionary.R
import com.com.talyshdictionary.fragments.FragmentHelpProject
import com.com.talyshdictionary.managers.BillingManager
//import kotlinx.android.synthetic.main.item_sub.view.*
import java.lang.StringBuilder

/*
class RecyclerSubsAdapter(private val activity: Activity,
                          fragment: FragmentHelpProject) : RecyclerView.Adapter<RecyclerSubsAdapter.ViewHolder>() {
    private val items = ArrayList<SkuDetails>()
    private val tag = "recycler_adapter"

    init {
        fragment.showSubsNull(true)
        BillingManager.querySkuDetails().observe(fragment, Observer {
            items.addAll(it)
            fragment.showSubsNull(items.size == 0)
            notifyDataSetChanged()
        })
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.tv_name
        val tvStatus: TextView = view.tv_status
        val tvPrice: TextView = view.tv_price
        val tvDesc: TextView = view.tv_desc
        val bSub: Button = view.b_sub
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.title.substringBefore("(")
        holder.tvDesc.text = item.description
        val isActive = BillingManager.isSubActive(item.sku)

        Log.i(tag, "title ${item.title}")
        Log.i(tag, "type ${item.type}")
        Log.i(tag, "desc ${item.description}")
        Log.i(tag, "price ${item.price}")
        Log.i(tag, "free ${item.freeTrialPeriod}")
        Log.i(tag, "original price ${item.originalPrice}")
        Log.i(tag, "period ${item.subscriptionPeriod}")

        val text = if(isActive)
            activity.getString(R.string.s8)
        else
            activity.getString(R.string.s7)
        holder.tvStatus.text = StringBuilder()
            .append(activity.getString(R.string.s5))
            .append(" ")
            .append(text)

        holder.tvPrice.text = StringBuilder()
            .append(activity.getString(R.string.s11))
            .append(" ")
            .append(item.price)

        if(isActive) {
            holder.bSub.visibility = View.GONE

            holder.bSub.setOnClickListener(null)
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.green))
        } else {
            holder.bSub.visibility = View.VISIBLE

            holder.bSub.setOnClickListener {
                BillingManager.showSku(item, activity)
            }
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.red))
        }
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_sub, parent,
            false)

        return ViewHolder(view)
    }
}

 */