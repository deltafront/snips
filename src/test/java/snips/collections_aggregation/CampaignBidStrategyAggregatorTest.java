package snips.collections_aggregation;

import org.junit.Before;
import org.junit.Test;
import snips.collections_aggregation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CampaignBidStrategyAggregatorTest
{
    private Integer most;
    private Integer middle;
    private Integer least;
    private Integer numberOfCampaigns;
    private CampaignBidStrategyAggregator aggregator;

    @Before
    public void before()
    {
        most = 100;
        middle = 75;
        least = 50;
        Integer campaignNameIndex = 0;
        Integer adGroupNameIndex = 1;
        Integer biddingStrategyNameIndex = 2;
        numberOfCampaigns = 10;
        final RowIndices rowIndices = new RowIndices(campaignNameIndex, biddingStrategyNameIndex, adGroupNameIndex);

        aggregator = new CampaignBidStrategyAggregator(rowIndices,",");
    }

    @Test
    public void testAggregateCampaigns()
    {
        final CampaignBidStrategyAdGroupContainer container =
                aggregator.createCampaignBidStrategyAdGroupAssociations(getDataRows());
        validateCampaignContainers(container);
    }

    private void validateCampaignContainers(CampaignBidStrategyAdGroupContainer container)
    {
        final List<CampaignContainer> campaignContainers = container.campaignContainers;
        assertThat(numberOfCampaigns,is(equalTo(campaignContainers.size())));
        campaignContainers.forEach(this::validateCampaignContainer);
    }

    private void validateCampaignContainer(CampaignContainer campaignContainer)
    {
        final List<BiddingStrategyContainer> biddingStrategyContainers = campaignContainer.biddingStrategyContainers;
        assertThat(3,is(equalTo(biddingStrategyContainers.size())));
        validateBiddingStrategyContainers(biddingStrategyContainers);
        validateGettingBiddingStrategyWithTheMostAdGroups(campaignContainer);
    }

    private void validateGettingBiddingStrategyWithTheMostAdGroups(CampaignContainer campaignContainer)
    {
        final String biddingStrategyWithTheMost = campaignContainer.getBiddingStrategyWithTheMostAdGroups();
        assertThat(biddingStrategyWithTheMost.contains(String.format("Bidding Strategy %d", most)), is(true));
        validateAdGroupsNotBelongingToSpecificBiddingStrategy(campaignContainer,biddingStrategyWithTheMost);
    }
    private void validateAdGroupsNotBelongingToSpecificBiddingStrategy(CampaignContainer campaignContainer, String winningBiddingStrategy)
    {
        final CampaignContainer containerWithLeftovers = campaignContainer.getAdGroupsThatDoNotBelongToBiddingStrategy(winningBiddingStrategy);
        final List<BiddingStrategyContainer>biddingStrategyContainers = containerWithLeftovers.biddingStrategyContainers;
        assertThat(2,is(equalTo(biddingStrategyContainers.size())));
        validateLeftoverBiddingStrategies(biddingStrategyContainers);
    }

    private void validateLeftoverBiddingStrategies(List<BiddingStrategyContainer> biddingStrategyContainers)
    {
        final List<Integer>sizes = Arrays.asList(middle, least);
        biddingStrategyContainers.forEach((biddingStrategyContainer)->{
            validateLeftoverBiddingStrategy(sizes, biddingStrategyContainer);
        });
    }

    private void validateLeftoverBiddingStrategy(List<Integer> sizes, BiddingStrategyContainer biddingStrategyContainer)
    {
        final String name = biddingStrategyContainer.name;
        final Integer size = biddingStrategyContainer.adGroups.size();
        assertThat(name.contains(String.format("Bidding Strategy %d",most)),is(false));
        assertThat(sizes.contains(size),is(true));
    }

    private void validateBiddingStrategyContainers(List<BiddingStrategyContainer> biddingStrategyContainers)
    {
        biddingStrategyContainers.forEach(this::validateBiddingStrategyContainer);
    }

    private void validateBiddingStrategyContainer(BiddingStrategyContainer biddingStrategyContainer)
    {
        final List<Integer> amounts = Arrays.asList(least, middle, most);
        final Set<String> adGroups = biddingStrategyContainer.adGroups;
        assertThat(amounts.contains(adGroups.size()),is(true));
    }



    private String getAdGroup(Integer campaignNumber, Integer biddingStrategyNumber, Integer index)
    {
        return String.format("AdGroup %d %s %s",index,getCampaign(campaignNumber), getBiddingStrategy(campaignNumber,biddingStrategyNumber));
    }
    private String getCampaign(Integer campaignNumber)
    {
        return String.format("Campaign %d",campaignNumber);
    }
    private String getBiddingStrategy(Integer campaignNumber, Integer biddingStrategyNumber)
    {
        return String.format("%s Bidding Strategy %d",getCampaign(campaignNumber), biddingStrategyNumber);
    }
    private List<String>getDataRows()
    {
        final List<String>dataRows = new LinkedList<>();
        getCampaigns(dataRows);
        return dataRows;
    }

    private void getCampaigns(List<String> dataRows)
    {
        IntStream.range(0, numberOfCampaigns).forEach((campaignIndex)->{
            getBiddingStrategies(dataRows, campaignIndex);
        });
    }

    private void getBiddingStrategies(List<String> dataRows, int campaignIndex)
    {
        final String campaign = getCampaign(campaignIndex);
        Arrays.asList(least, middle, most).forEach((biddingStrategyIndex)->{
            final String biddingStrategyName = getBiddingStrategy(campaignIndex, biddingStrategyIndex);
            getAdGroups(dataRows, campaignIndex, campaign, biddingStrategyIndex, biddingStrategyName);
        });
    }

    private void getAdGroups(List<String> dataRows, int campaignIndex, String campaign, Integer biddingStrategyIndex, String biddingStrategyName)
    {
        IntStream.range(0, biddingStrategyIndex).forEach((adGroupIndex) -> {
            final String adGroupName = getAdGroup(campaignIndex, biddingStrategyIndex, adGroupIndex);
            final String inner = getRow(campaign, adGroupName, biddingStrategyName);
            dataRows.add(inner);
        });
    }

    private String getRow(String campaign, String adGroup, String biddingStrategy)
    {
        return String.format("%s,%s,%s",campaign,adGroup,biddingStrategy);
    }
}
