package com.scp.market.ui.myroom.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.scp.market.R
import com.scp.market.model.injury.response.Injury
import com.scp.market.model.user.response.UserInfo

class InjuryAdapter(
        val activity: Activity,
        val fm: FragmentManager
): RecyclerView.Adapter<InjuryAdapter.InjuryViewHolder>() {

    private var data: ArrayList<Injury> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InjuryViewHolder
    = InjuryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.item_injury,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: InjuryViewHolder, position: Int) =
            holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    inner class InjuryViewHolder(view: View): RecyclerView.ViewHolder(view) {

//        private val userCode: TextView = view.findViewById(R.id.userCode)
//        private val userName: TextView = view.findViewById(R.id.userName)
//        private val userEmail: TextView = view.findViewById(R.id.userEmail)
//        private val userTel: TextView = view.findViewById(R.id.userTel)

        @SuppressLint("SetTextI18n")
        fun bind(item: Injury) {
//            userCode.text = item.uid
//            userEmail.text = item.email
//            userTel.text = item.callnum
//            userName.text = item.name

        }
    }


}