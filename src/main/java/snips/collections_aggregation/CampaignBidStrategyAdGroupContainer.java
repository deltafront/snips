package snips.collections_aggregation;


import java.util.*;

public class CampaignBidStrategyAdGroupContainer
{
    public final List<CampaignContainer> campaignContainers;

    public CampaignBidStrategyAdGroupContainer()
    {
        campaignContainers = new LinkedList<>();
    }
    public CampaignContainer addCampaign(String campaign)
    {
        CampaignContainer $container;
        final Optional<CampaignContainer> campaignContainer = campaignContainers.stream().filter((container)->campaign.equals(container.name)).findFirst();
        if(campaignContainer.isPresent()){
            $container = campaignContainer.get();
        }
        else{
            $container = new CampaignContainer(campaign);
            campaignContainers.add($container);
        }
        return $container;
    }

}

