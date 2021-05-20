package com.scp.market.ui.myroom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.scp.market.Application
import com.scp.market.databinding.FragmentLocalAdminBinding
import com.scp.market.model.injury.response.Injury
import com.scp.market.model.user.response.UserInfo
import com.scp.market.ui.myroom.adapter.UserInfoAdapter

class LocalAdminFragment : Fragment() {

    private lateinit var binding: FragmentLocalAdminBinding

    private var userInfoAdapter: UserInfoAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocalAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewForUserList()
        addProductListForUserList(Application.instance?.userInfoList)

        initRecyclerViewForInjury()
        addProductListForInjury(Application.instance?.injuryList)

    }

    private fun addProductListForInjury(injuryList: List<Injury>?) {

    }

    private fun initRecyclerViewForInjury() {

    }

    private fun addProductListForUserList(userInfoList: List<UserInfo>?) {
        Log.i("addProductList", userInfoList.toString())
        userInfoAdapter?.addAll(userInfoList)
    }

    private fun initRecyclerViewForUserList() {

        userInfoAdapter = UserInfoAdapter(requireActivity(), activity?.supportFragmentManager!!)

        binding.userInfoRecyclerView.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            layoutManager = LinearLayoutManager(context)
            adapter = userInfoAdapter
//            searchAdapter = SearchAdapter(fragmentManager!!)
        }
    }

}