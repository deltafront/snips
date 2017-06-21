package snips.collections_aggregation;

import java.util.*;

public class CampaignBidStrategyAggregator
{
    private final RowIndices rowIndices;
    private final String delimiter;
    public CampaignBidStrategyAggregator(RowIndices rowIndices, String delimiter)
    {
        this.rowIndices = rowIndices;
        this.delimiter = delimiter;
    }


    public CampaignBidStrategyAdGroupContainer createCampaignBidStrategyAdGroupAssociations(List<String>dataRows)
    {
        final CampaignBidStrategyAdGroupContainer campaignBidStrategyAdGroupContainer = new CampaignBidStrategyAdGroupContainer();
        dataRows.forEach((dataRow)->{
            final RowValues rowValues = new RowValues(rowIndices,dataRow.split(delimiter));
            addCampaignIfNotPresent(rowValues,campaignBidStrategyAdGroupContainer);
        });
        return campaignBidStrategyAdGroupContainer;
    }

    private void addCampaignIfNotPresent(RowValues rowValues, CampaignBidStrategyAdGroupContainer container){
        final CampaignContainer campaignContainer = container.addCampaign(rowValues.campaignName);
        associateCampaignWithBiddingStrategy(rowValues,campaignContainer);
    }
    private void associateCampaignWithBiddingStrategy(RowValues rowValues, CampaignContainer campaignContainer)
    {
        final BiddingStrategyContainer biddingStrategyContainer = campaignContainer.associateBiddingStrategy(rowValues.bidStrategyName);
        associateAdGroupWithBiddingStrategy(rowValues,biddingStrategyContainer);
    }
    private void associateAdGroupWithBiddingStrategy(RowValues rowValues, BiddingStrategyContainer biddingStrategyContainer){
        biddingStrategyContainer.adGroups.add(rowValues.adGroupName);
    }
}
