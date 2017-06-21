package snips.collections_aggregation;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionsAggregator
{
    public Set<String> aggregateSingleColumnValues(String key, List<String> headers, List<String>rows, String delimiter)
    {
        final Set<String>set = new HashSet<>();
        final Integer index = getKeyIndex(key,headers);
        if(-1!=index)
            rows.forEach((row)->set.add(row.split(delimiter)[index]));
        return set;
    }

    private Integer getKeyIndex(String key, List<String>headers)
    {
        return headers.indexOf(key);
    }

}
