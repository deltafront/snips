package snips.collections_aggregation;

public class RowValues
{
    public final String campaignName;
    public final String bidStrategyName;
    public final String adGroupName;

    public RowValues(RowIndices indices, String[] values)
    {
        this.adGroupName = values[indices.adGroupNameIndex];
        this.bidStrategyName = values[indices.bidStrategyNameIndex];
        this.campaignName = values[indices.campaignNameIndex];
    }
}
