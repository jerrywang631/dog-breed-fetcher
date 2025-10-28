package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher underlyingFetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;
    public CachingBreedFetcher(BreedFetcher fetcher) {
            this.underlyingFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (cache.containsKey(breed.toLowerCase())) {
            return cache.get(breed.toLowerCase());
        }

        try {
            // 调用底层 fetcher
            List<String> result = underlyingFetcher.getSubBreeds(breed);
            callsMade++;

            // 存入缓存（仅成功结果缓存）
            cache.put(breed.toLowerCase(), result);
            return result;

        } catch (BreedNotFoundException e) {
            // 不缓存失败结果
            callsMade++;
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
    }


