package snips.collections_aggregation;

import org.junit.Test;
import snips.collections_aggregation.CampaignBidStrategyAdGroupContainer;
import snips.collections_aggregation.CampaignContainer;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CampaignBidStrategyAdGroupContainerTest
{
    @Test
    public void testAssociateCampaign()
    {
        final CampaignBidStrategyAdGroupContainer campaignBidStrategyAdGroupContainer =
                new CampaignBidStrategyAdGroupContainer();
        final String campaign = "FOO";
        final CampaignContainer initial = campaignBidStrategyAdGroupContainer.addCampaign(campaign);
        final CampaignContainer reinsterted = campaignBidStrategyAdGroupContainer.addCampaign(campaign);
        assertThat(initial,is(sameInstance(reinsterted)));
    }
}
