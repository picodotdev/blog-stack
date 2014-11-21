package info.blogstack.misc;

import java.util.Map;

public interface Configuration<K,V> {

	Map<K,V> get();
}