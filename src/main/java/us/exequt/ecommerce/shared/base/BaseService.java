package us.exequt.ecommerce.shared.base;

import java.util.List;

public interface BaseService<T, K> {
    T getById(K id);
    List<T> getAll();
}
