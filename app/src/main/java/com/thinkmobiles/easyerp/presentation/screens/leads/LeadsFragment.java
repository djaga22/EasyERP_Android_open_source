package com.thinkmobiles.easyerp.presentation.screens.leads;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thinkmobiles.easyerp.R;
import com.thinkmobiles.easyerp.domain.LeadsRepository;
import com.thinkmobiles.easyerp.presentation.adapters.LeadsAdapter;
import com.thinkmobiles.easyerp.presentation.base.BaseFragment;
import com.thinkmobiles.easyerp.presentation.holders.data.LeadDH;
import com.thinkmobiles.easyerp.presentation.listeners.EndlessRecyclerViewScrollListener;
import com.thinkmobiles.easyerp.presentation.screens.home.HomeActivity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.util.ArrayList;

/**
 * Created by Lynx on 1/16/2017.
 */

@EFragment(R.layout.fragment_leads)
public class LeadsFragment extends BaseFragment<HomeActivity> implements LeadsContract.LeadsView {

    private LeadsContract.LeadsPresenter presenter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Bean
    protected LeadsRepository leadsRepository;
    @Bean
    protected LeadsAdapter leadsAdapter;

    @ViewById
    protected SwipeRefreshLayout swipeContainer_FL;
    @ViewById
    protected RecyclerView rvLeads_FL;

    @ColorRes
    protected int colorPrimary;
    @ColorRes
    protected int colorPrimaryDark;

    @AfterInject
    protected void initPresenter() {
        new LeadsPresenter(this, leadsRepository);
    }

    @AfterViews
    protected void initUI() {
        displayProgress(true);
        swipeContainer_FL.setColorSchemeColors(colorPrimary, colorPrimaryDark);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                displayProgress(true);
                presenter.loadLeads(page);
            }
        };
        rvLeads_FL.setLayoutManager(llm);
        rvLeads_FL.setAdapter(leadsAdapter);
        rvLeads_FL.addOnScrollListener(scrollListener);
        leadsAdapter.setOnCardClickListener((view, position, viewType) -> presenter.displayLeadDetails(leadsAdapter.getItem(position).getLeadItem()._id));
        swipeContainer_FL.setOnRefreshListener(() -> {
            scrollListener.resetState();
            leadsAdapter.clear();
            presenter.subscribe();
        });

        presenter.subscribe();
    }

    @Override
    public void displayLeads(ArrayList<LeadDH> leadDHs) {
        displayProgress(false);
        swipeContainer_FL.setRefreshing(false);
        leadsAdapter.addListDH(leadDHs);
    }

    @Override
    public void setPresenter(LeadsContract.LeadsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected boolean needProgress() {
        return true;
    }
}
