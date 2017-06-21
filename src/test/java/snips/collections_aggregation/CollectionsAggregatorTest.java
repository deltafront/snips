package snips.collections_aggregation;


import org.junit.Before;
import org.junit.Test;
import snips.collections_aggregation.CollectionsAggregator;

import java.util.*;
import java.util.stream.IntStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class CollectionsAggregatorTest
{
    private List<String>rows;
    private  Set<String>masterValues;
    final List<String> headers = Arrays.asList("ONE");
    private Integer upper;
    private CollectionsAggregator collectionsAggregator;

    @Before
    public void before()
    {
        masterValues = new HashSet<>();
        final Random random = new Random();
        upper = 100;
        final List<Integer> values = new LinkedList<>();
        IntStream.range(0,upper).forEach((index)-> values.add(random.nextInt()));
        rows = new LinkedList<>();
        IntStream.range(0,100).forEach((index)->{
            final List<Integer>row = new LinkedList<>();
            final Integer value = random.nextInt(upper);
            masterValues.add(String.valueOf(value));
            rows.add(String.valueOf(value));
        });
        collectionsAggregator = new CollectionsAggregator();
    }
    @Test
    public void testHappyPath()
    {
        final Set<String>actual = collectionsAggregator.aggregateSingleColumnValues("ONE", headers, rows,",");
        assertNotNull(actual);
        assertEquals(masterValues.size(), actual.size());
        masterValues.forEach((value)->assertTrue(actual.contains(value)));
    }
    @Test
    public void testInvalidKey()
    {
        final Set<String>actual = collectionsAggregator.aggregateSingleColumnValues("FOO", headers, rows,",");
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }
}
