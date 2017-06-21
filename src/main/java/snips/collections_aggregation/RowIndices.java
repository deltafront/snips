package snips.collections_aggregation;

public class RowIndices
{
    public final Integer campaignNameIndex;
    public final Integer bidStrategyNameIndex;
    public final Integer adGroupNameIndex;

    public RowIndices(Integer campaignNameIndex, Integer bidStrategyNameIndex, Integer adGroupNameIndex)
    {
        this.campaignNameIndex = campaignNameIndex;
        this.bidStrategyNameIndex = bidStrategyNameIndex;
        this.adGroupNameIndex = adGroupNameIndex;
    }
}
