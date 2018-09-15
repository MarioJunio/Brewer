package com.mj.brewer.event.cache;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

public class CacheRemovalListener implements RemovalListener<Object, Object> {

	@Override
	public void onRemoval(Object key, Object value, RemovalCause cause) {
		System.out.format("removal listerner called with key [%s], cause [%s], evicted [%S]\n", key, cause.toString(), cause.wasEvicted());
	}

}
