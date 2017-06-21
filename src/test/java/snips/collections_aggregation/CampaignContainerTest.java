package snips.collections_aggregation;


import org.junit.Before;
import org.junit.Test;
import snips.collections_aggregation.BiddingStrategyContainer;
import snips.collections_aggregation.CampaignContainer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CampaignContainerTest
{
    private CampaignContainer campaignContainer;
    private Integer most;
    private Integer middle;
    private Integer least;

    @Before
    public void before()
    {
        this.campaignContainer = new CampaignContainer("Campaign");
        most = 100;
        least = 50;
        middle = 75;
        Arrays.asList(most,middle,least).forEach((amount)->associateBiddingStrategy(amount, String.valueOf(amount)));
    }

    @Test
    public void testGetBiddingStrategyWithTheMostAdGroups()
    {
        final String biddingStrategyWithMost = campaignContainer.getBiddingStrategyWithTheMostAdGroups();
        assertThat(String.valueOf(most),is(equalTo(biddingStrategyWithMost)));
    }
    @Test
    public void testGetAdGroupsThatDoNotBelongToBiddingStrategy()
    {
        final String target = String.valueOf(most);
        final CampaignContainer rest = campaignContainer.getAdGroupsThatDoNotBelongToBiddingStrategy(target);
        final List<String> leftovers = Arrays.asList(String.valueOf(least),String.valueOf(middle));
        rest.biddingStrategyContainers.forEach((container)->{
            assertThat(leftovers.contains(container.name),is(true));
            assertThat(target,is(not(equalTo(container.name))));
        });
    }
    @Test
    public void testReinsertingBiddingStrategy()
    {
        final String target = "FOO";
        final BiddingStrategyContainer inital = campaignContainer.associateBiddingStrategy(target);
        final BiddingStrategyContainer reinsterted = campaignContainer.associateBiddingStrategy(target);
        assertThat(inital,is(sameInstance(reinsterted)));
    }
    private void associateBiddingStrategy(Integer amount, String name)
    {
        final List<String> adGroups = new LinkedList<>();
        IntStream.range(0,amount).forEach((index)->adGroups.add(String.format("AdGroup %d",index)));
        final BiddingStrategyContainer biddingStrategyContainer = campaignContainer.associateBiddingStrategy(name);
        biddingStrategyContainer.adGroups.addAll(adGroups);
    }
}
