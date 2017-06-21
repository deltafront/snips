package snips.collections_aggregation;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CampaignContainer
{
    public final List<BiddingStrategyContainer> biddingStrategyContainers;
    public final String name;

    public CampaignContainer(String name)
    {
        biddingStrategyContainers = new LinkedList<>();
        this.name = name;
    }
    public BiddingStrategyContainer associateBiddingStrategy(String biddingStrategy)
    {
        BiddingStrategyContainer $biddingStrategyContainer;
        final Optional<BiddingStrategyContainer>biddingStrategyContainer =
                biddingStrategyContainers.stream().filter((container)->biddingStrategy.equals(container.name)).findFirst();
        if(biddingStrategyContainer.isPresent()){
            $biddingStrategyContainer = biddingStrategyContainer.get();
        }
        else{
            $biddingStrategyContainer = new BiddingStrategyContainer(biddingStrategy);
            biddingStrategyContainers.add($biddingStrategyContainer);
        }
        return $biddingStrategyContainer;
    }

    public String getBiddingStrategyWithTheMostAdGroups(){
        final AtomicInteger count = new AtomicInteger(0);
        String strategy = null;
        for(BiddingStrategyContainer container : biddingStrategyContainers){
            final Integer size = container.adGroups.size();
            final String name = container.name;
            if(container.adGroups.size() > count.get()){
                count.set(size);
                strategy = name;
            }
        }
        return strategy;
    }
    public CampaignContainer getAdGroupsThatDoNotBelongToBiddingStrategy(String biddingStrategy){
        final CampaignContainer campaignContainer = new CampaignContainer(this.name);
        biddingStrategyContainers.stream().filter((container)->
                !biddingStrategy.equals(container.name)).forEach((strategyContainer)->
                input(campaignContainer, strategyContainer));
        return campaignContainer;
    }

    private void input(CampaignContainer campaignContainer, BiddingStrategyContainer strategyContainer)
    {
        final BiddingStrategyContainer biddingStrategyContainer = campaignContainer.associateBiddingStrategy(strategyContainer.name);
        strategyContainer.adGroups.forEach(biddingStrategyContainer.adGroups::add);
    }
}
