package com.hazem.githubtask.ui.fragments.userdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hazem.githubtask.R
import com.hazem.githubtask.data.network.REPO_OWNER_NAME
import com.hazem.githubtask.ui.adapters.UserDetailsAdapter
import com.hazem.githubtask.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.user_details_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import androidx.recyclerview.widget.RecyclerView


class UserDetailsFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: UserDetailsViewModelFactory by instance()
    private lateinit var viewModel: UserDetailsViewModel

    private var adapter: UserDetailsAdapter? = null

    private var userName: String = REPO_OWNER_NAME

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_details_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UserDetailsViewModel::class.java)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        user_recycler.addItemDecoration(decoration)
        setupScrollListener()

        initAdapter()
        getUserRepos(userName)

        viewModel.loadMoreLiveData.observe(this.viewLifecycleOwner, Observer {
            load_more_progress.visibility = if (it) View.VISIBLE else View.GONE
        })

    }

    private fun setupScrollListener() {
        val layoutManager = user_recycler.layoutManager as LinearLayoutManager
          user_recycler.addOnScrollListener(object :
              RecyclerView.OnScrollListener() {
              override fun onScrolled(
                  recyclerView: RecyclerView,
                  dx: Int,
                  dy: Int
              ) {
                  super.onScrolled(recyclerView, dx, dy)
                  val totalItemCount = layoutManager.itemCount
                  val visibleItemCount = layoutManager.childCount
                  val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                  val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                  viewModel.listScrolled(visibleItemCount,firstVisibleItemPosition, totalItemCount)
              }
          })

   /*     user_recycler.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore()
            }

        })*/

    }

    private fun initAdapter() {
        adapter = UserDetailsAdapter()
        user_recycler.adapter = adapter
    }

    private fun getUserRepos(userName: String) = launch {
        progress?.visibility = View.VISIBLE
        val userRepos = viewModel.getUserRepos(userName).await()
        userRepos.observe(this@UserDetailsFragment.viewLifecycleOwner, Observer {
            progress.visibility = View.GONE
            load_more_progress.visibility = View.GONE
            Log.d("Activity", "list: ${it?.size}")
            showEmptyList(it == null || it.isEmpty())
            adapter?.submitList(it)
        })
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            user_recycler.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            user_recycler.visibility = View.VISIBLE
        }
    }

}
