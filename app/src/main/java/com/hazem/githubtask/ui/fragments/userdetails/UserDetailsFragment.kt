package com.hazem.githubtask.ui.fragments.userdetails

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.user_details_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.clear_cash -> clearCash()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun clearCash() = launch {
        viewModel.deleteOldData().await()
        Toast.makeText(requireContext(), "Cash Cleared, Loading New Data", Toast.LENGTH_SHORT)
            .show()
        viewModel.getUserRepos(userName).await()
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
            adapter?.showLoadingMore()
            if (it) adapter?.showLoadingMore() else adapter?.removeLoadingView()
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
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, firstVisibleItemPosition, totalItemCount)
            }
        })

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
            adapter?.removeLoadingView()
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
