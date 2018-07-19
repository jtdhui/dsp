package redis.clients.jedis;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import redis.clients.util.Hashing;
import redis.clients.util.Pool;

public class DBIndexSelectedShardedJedisPool extends Pool<ShardedJedis> {
    public DBIndexSelectedShardedJedisPool(final GenericObjectPool.Config poolConfig,
            List<JedisShardInfo> shards, int dbIndex) {
        this(poolConfig, shards, Hashing.MURMUR_HASH, dbIndex);
    }

    public DBIndexSelectedShardedJedisPool(final GenericObjectPool.Config poolConfig,
            List<JedisShardInfo> shards, Hashing algo, int dbIndex) {
        this(poolConfig, shards, algo, null, dbIndex);
    }

    public DBIndexSelectedShardedJedisPool(final GenericObjectPool.Config poolConfig,
            List<JedisShardInfo> shards, Pattern keyTagPattern, int dbIndex) {
        this(poolConfig, shards, Hashing.MURMUR_HASH, keyTagPattern, dbIndex);
    }

    public DBIndexSelectedShardedJedisPool(final GenericObjectPool.Config poolConfig,
            List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern, int dbIndex) {
        super(poolConfig, new ShardedJedisFactory(shards, algo, keyTagPattern, dbIndex));
    }

    /**
     * PoolableObjectFactory custom impl.
     */
    @SuppressWarnings("rawtypes")
	private static class ShardedJedisFactory extends BasePoolableObjectFactory {
        private List<JedisShardInfo> shards;
        private Hashing algo;
        private Pattern keyTagPattern;
        private int dbIndex;

        public ShardedJedisFactory(List<JedisShardInfo> shards, Hashing algo,
                Pattern keyTagPattern, int dbIndex) {
            this.shards = shards;
            this.algo = algo;
            this.keyTagPattern = keyTagPattern;
            this.dbIndex = dbIndex;
        }

        public Object makeObject() throws Exception {
            ShardedJedis jedis = new ShardedJedis(shards, algo, keyTagPattern);
            Collection<Jedis> jediss = jedis.getAllShards();
			for (Jedis j : jediss) {
				j.select(dbIndex);
			}
            return jedis;
        }

        public void destroyObject(final Object obj) throws Exception {
            if ((obj != null) && (obj instanceof ShardedJedis)) {
                ShardedJedis shardedJedis = (ShardedJedis) obj;
                for (Jedis jedis : shardedJedis.getAllShards()) {
                    try {
                   		try {
                   			jedis.quit();
                        } catch (Exception e) {

                        }
                        jedis.disconnect();
                    } catch (Exception e) {

                    }
                }
            }
        }

        public boolean validateObject(final Object obj) {
        	try {
                ShardedJedis jedis = (ShardedJedis) obj;
                for (Jedis shard : jedis.getAllShards()) {
                    if (!shard.ping().equals("PONG")) {
                        return false;
                    }
                }
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }
}