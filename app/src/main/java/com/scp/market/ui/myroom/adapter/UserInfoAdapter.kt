package com.scp.market.ui.myroom.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.scp.market.R
import com.scp.market.model.user.response.UserInfo

class UserInfoAdapter(
        val activity: Activity,
        val fm: FragmentManager
): RecyclerView.Adapter<UserInfoAdapter.UserInfoViewHolder>() {

    private var data: ArrayList<UserInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder
    = UserInfoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.item_userinfo,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) =
            holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    fun addAll(list: List<UserInfo>?) {

        if (list != null) {
            Log.i("addAllLog", "1")
            if (data.size > 0) {

                Log.i("addAllLog", "2")
                if (data[0].name != list[0].name) {
                    Log.i("addAllLog", "3")
                    data = ArrayList()
                    data.addAll(list)
                    notifyDataSetChanged()
                } else {
                    Log.i("addAllLog", "3.5, $list")

                    // 뒤로가기 돌아왔을시, 데이터 바인딩을 위한 코드
                    data.clear()
                    notifyDataSetChanged()
                    data.addAll(list)
                    notifyDataSetChanged()
                }

            } else {
                Log.i("addAllLog", "4")
                data.addAll(list)
                notifyDataSetChanged()

            }
        }
    }
    inner class UserInfoViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val userCode: TextView = view.findViewById(R.id.userCode)
        private val userName: TextView = view.findViewById(R.id.userName)
        private val userEmail: TextView = view.findViewById(R.id.userEmail)
        private val userTel: TextView = view.findViewById(R.id.userTel)

        @SuppressLint("SetTextI18n")
        fun bind(item: UserInfo) {
            userCode.text = item.uid
            userEmail.text = item.email
            userTel.text = item.callnum
            userName.text = item.name

        }
    }


}