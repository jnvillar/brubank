package cache

interface CacheIO<K, V, R> {
    R get(K key)
    R put(K key, V value)
}