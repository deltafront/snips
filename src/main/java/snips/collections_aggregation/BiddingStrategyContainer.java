package snips.collections_aggregation;

import java.util.HashSet;
import java.util.Set;

public class BiddingStrategyContainer
{
    public final Set<String>adGroups;
    public final String name;

    public BiddingStrategyContainer(String name)
    {
        adGroups = new HashSet<>();
        this.name = name;
    }
}
