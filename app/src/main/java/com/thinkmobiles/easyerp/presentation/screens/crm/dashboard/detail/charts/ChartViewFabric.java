package com.thinkmobiles.easyerp.presentation.screens.crm.dashboard.detail.charts;

import com.thinkmobiles.easyerp.data.model.crm.dashboard.detail.DashboardChartType;

/**
 * @author michael.soyma@thinkmobiles.com (Created on 1/24/2017.)
 */

public final class ChartViewFabric {

    private ChartViewFabric() {}

    public static IChartView implementByChartType(final DashboardChartType chartType) {
        switch (chartType) {
            case DONUT: return new DonutChartViewImpl();
            case OVERVIEW: return new OverViewChartViewImpl();
            case TABLE: return new TableChartViewImpl();
            case HORIZONTALBAR: return new HorizontalBarChartViewImpl();
            case REVERSEHORIZONTALBAR:
            case COLORCARDSVIEW:
            default : return null;
        }
    }

}
